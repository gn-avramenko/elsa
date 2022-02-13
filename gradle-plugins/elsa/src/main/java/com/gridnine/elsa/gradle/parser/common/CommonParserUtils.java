/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.common;

import com.gridnine.elsa.common.meta.common.*;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public final class CommonParserUtils {

    private static final Pattern languagePattern = Pattern.compile(".*_([a-z]+)\\.properties");

    public static String getIdAttribute(BuildXmlNode node){
        var id = node.getAttribute("id");
        if(BuildTextUtils.isBlank(id)){
            throw new IllegalArgumentException("id attribute of element %s".formatted(node.getName()));
        }
        return id;
    }

    public static MetaDataParsingResult parse(File file) throws Exception {
        var content = Files.readAllBytes(file.toPath());
        var node = BuildXmlUtils.parseXml(content);
        var baseName = file.getName().substring(0, file.getName().lastIndexOf("."));
        var dir = new File(file.getParentFile(), "l10n");
        var localizations = new LinkedHashMap<String, Map<Locale,String>>();
        if(dir.exists()){
            for(File lf: Objects.requireNonNull(dir.listFiles())){
                if(lf.getName().contains(baseName)) {
                    readLocalizations(lf, localizations);
                }
            }
        }
       return new MetaDataParsingResult(node, localizations);
    }

    private static void readLocalizations(File file, Map<String, Map<Locale,String>> localizations) throws IOException {
        var m =  languagePattern.matcher(file.getName());
        if(!m.find()){
            throw new IllegalArgumentException("unable to detect language from filename %s".formatted(file.getName()));
        }
        var localeStr = m.group(1);
        var locale = new Locale(localeStr);
        var content = Files.readAllBytes(file.toPath());
        var props = new Properties();
        props.load(new ByteArrayInputStream(content));
        props.forEach((key, value) -> localizations.computeIfAbsent((String) key,
                k  -> new LinkedHashMap<>()).put(locale, (String) value));
    }

    public static<T extends BaseModelElementDescription> void updateLocalizationsOfChild(T description ,  Map<String, Map<Locale, String>>  localizations, String parentId){
        var id = parentId == null? "%s.name".formatted(description.getId()) : "%s.%s.name".formatted(parentId, description.getId());
        updateLocalizations(description, localizations, id);
    }


    private static<T extends BaseModelElementDescription> void  updateLocalizations(T description , Map<String, Map<Locale, String>>  localizations, String id) {
        var locs = localizations.get(id);
        if(locs == null){
            return;
        }
        locs.forEach((locale, displayName) -> description.getDisplayNames().put(locale, displayName));
    }

    public static void fillEntityDescription(BuildXmlNode elm, EntityDescription description) {
        description.setAbstract("true".equals(elm.getAttribute("abstract")));
        description.setExtendsId(elm.getAttribute("extends"));
        elm.getChildren("property").forEach(prop ->{
            var pd = description.getProperties().computeIfAbsent(getIdAttribute(prop), StandardPropertyDescription::new);
            pd.setClassName(prop.getAttribute("class-name"));
            pd.setNullable("true".equals(prop.getAttribute("nullable")));
            pd.setType(StandardValueType.valueOf(prop.getAttribute("type")));
        });
        elm.getChildren("collection").forEach(coll ->{
            var cd = description.getCollections().computeIfAbsent(getIdAttribute(coll), StandardCollectionDescription::new);
            cd.setElementClassName(coll.getAttribute("element-class-name"));
            cd.setElementType(StandardValueType.valueOf(coll.getAttribute("element-type")));
            cd.setUnique("true".equals(coll.getAttribute("unique")));
        });
        elm.getChildren("map").forEach(map ->{
            var md = description.getMaps().computeIfAbsent(getIdAttribute(map), StandardMapDescription::new);
            md.setKeyClassName(map.getAttribute("key-class-name"));
            md.setKeyType(StandardValueType.valueOf(map.getAttribute("key-type")));
            md.setValueClassName(map.getAttribute("value-class-name"));
            md.setValueType(StandardValueType.valueOf(map.getAttribute("key-type")));
        });
        updateParameters(elm, description);

    }

    public static EntityDescription updateEntity(Map<String, EntityDescription> entities, BuildXmlNode node){
        var entityDescr = entities.computeIfAbsent(getIdAttribute(node), EntityDescription::new);
        fillEntityDescription(node, entityDescr);
        return entityDescr;
    }

    public static void updateEnum(Map<String, EnumDescription> enums, BuildXmlNode node, Map<String, Map<Locale, String>> localizations){
        var ed = enums.computeIfAbsent(getIdAttribute(node), EnumDescription::new);
        node.getChildren("enum-item").forEach(item ->{
            var id = getIdAttribute(item);
            var ei = ed.getItems().computeIfAbsent(id, EnumItemDescription::new);
            if(localizations != null){
                updateLocalizationsOfChild(ei, localizations, ed.getId());
            }
        });
    }

    public static void updateParameters(BuildXmlNode node, BaseModelElementDescription elm){
        node.getChildren("parameter").forEach(child -> elm.getParameters().put(child.getAttribute("name"), child.getAttribute("value")));
    }

}
