/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.common;

import com.gridnine.elsa.gradle.utils.BuildTextUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.common.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public final class CommonParserUtils {

    private static final Pattern languagePattern = Pattern.compile(".*_([a-z]+)\\.properties");

    public static String getIdAttribute(BuildXmlNode node) {
        var id = node.getAttribute("id");
        if (BuildTextUtils.isBlank(id)) {
            throw new IllegalArgumentException("id attribute of element %s".formatted(node.getName()));
        }
        return id;
    }

    public static MetaDataParsingResult parse(File file) throws Exception {
        var content = Files.readAllBytes(file.toPath());
        var node = BuildXmlUtils.parseXml(content);
        var baseName = file.getName().substring(0, file.getName().lastIndexOf("."));
        var dir = new File(file.getParentFile(), "l10n");
        var localizations = new LinkedHashMap<String, Map<Locale, String>>();
        if (dir.exists()) {
            for (File lf : Objects.requireNonNull(dir.listFiles())) {
                if (lf.getName().contains(baseName)) {
                    readLocalizations(lf, localizations);
                }
            }
        }
        return new MetaDataParsingResult(node, localizations);
    }

    private static void readLocalizations(File file, Map<String, Map<Locale, String>> localizations) throws IOException {
        var m = languagePattern.matcher(file.getName());
        if (!m.find()) {
            throw new IllegalArgumentException("unable to detect language from filename %s".formatted(file.getName()));
        }
        var localeStr = m.group(1);
        var locale = new Locale(localeStr);
        var content = Files.readAllBytes(file.toPath());
        var props = new Properties();
        props.load(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
        props.forEach((key, value) -> localizations.computeIfAbsent((String) key,
                k -> new LinkedHashMap<>()).put(locale, (String) value));
    }

    public static void updateBaseElement(BaseElement elm, BuildXmlNode node, String fullId, Map<String, Map<Locale, String>> localization) {
        node.getAttributes().forEach((key, value) -> {
            if ("caption".equals(key)) {
                elm.getDisplayNames().put(Locale.ROOT, value);
                return;
            }
            if("id".equals(key)){
                return;
            }
            elm.getAttributes().put(key, value);
        });
        for (BuildXmlNode child : node.getChildren("parameter")) {
            elm.getParameters().put(child.getAttribute("name"), child.getAttribute("value"));
        }
        if (fullId != null && localization != null) {
            String key = "%s.name".formatted(fullId);
            var locs = localization.get(key);
            if (locs != null) {
                locs.forEach((locale, name) -> {
                    elm.getDisplayNames().put(locale, name);
                });
            }
        }
    }

    public static void updateEnum(Set<String> enumIds, Map<String, EnumDescription> enums, BuildXmlNode node, Map<String, Map<Locale, String>> localizations) {
        var ed = enums.computeIfAbsent(getIdAttribute(node), EnumDescription::new);
        enumIds.add(ed.getId());
        updateBaseElement(ed, node, ed.getId(), localizations);
        node.getChildren("enum-item").forEach(item -> {
            var ei = ed.getItems().computeIfAbsent(getIdAttribute(item), EnumItemDescription::new);
            updateBaseElement(ei, item, "%s.%s".formatted(ed.getId(), ei.getId()), localizations);
        });
    }

    public static String updateEntity(Set<String> entityIds, Map<String, EntityDescription> enums, BuildXmlNode node, Map<String, Map<Locale, String>> localizations) {
        var ed = enums.computeIfAbsent(getIdAttribute(node), EntityDescription::new);
        entityIds.add(ed.getId());
        updateBaseElement(ed, node, ed.getId(), localizations);
        node.getChildren().forEach(item -> {
            if ("caption-expression".equals(item.getName())) {
                ed.getAttributes().put(item.getName(), item.getValue());
                return;
            }
            if ("localizable-caption-expression".equals(item.getName())) {
                ed.getAttributes().put(item.getName(), item.getValue());
                return;
            }
            var pd = ed.getProperties().computeIfAbsent(getIdAttribute(item), PropertyDescription::new);
            pd.setTagName(item.getName());
            updateBaseElement(pd, item, "%s.%s".formatted(ed.getId(), pd.getId()), localizations);
        });
        return ed.getId();
    }

    public static void addAttribute(Map<String, AttributeDescription> assetAttributes, BuildXmlNode child) {
        var name = child.getAttribute("name");
        var attr = assetAttributes.computeIfAbsent(name, (it) -> {
            var res = new AttributeDescription();
            res.setName(name);
            return res;
        });
        attr.setType(AttributeType.valueOf(child.getAttribute("type")));
        attr.setDefaultValue(child.getAttribute("default-value"));
        var svs = child.getChildren("select-values");
        if (!svs.isEmpty()) {
            for (BuildXmlNode sv : svs.get(0).getChildren("value")) {
                attr.getSelectValues().add(sv.getValue());
            }
        }
    }

    public static void addTag(Map<String, TagDescription> entityTags, BuildXmlNode child) {
        var name = child.getAttribute("tag-name");
        var tag = entityTags.computeIfAbsent(name, (it) -> {
            var res = new TagDescription();
            res.setTagName(name);
            return res;
        });
        tag.setType(child.getAttribute("type"));
        tag.setObjectIdAttributeName(child.getAttribute("object-id-attribute-name"));
        for (BuildXmlNode attr : child.getChildren("attribute")) {
            addAttribute(tag.getAttributes(), attr);
        }
        processGenerics(tag.getGenerics(), child);
    }

    public static void processGenerics(List<GenericDescription> generics, BuildXmlNode child) {
        for (BuildXmlNode attr : child.getChildren("generic")) {
            var gen = new GenericDescription();
            gen.setType(attr.getAttribute("type"));
            gen.setId(attr.getAttribute("id"));
            gen.setObjectIdAttributeName(attr.getAttribute("object-id-attribute-name"));
            generics.add(gen);
            processGenerics(gen.getNestedGenerics(), attr);
        }
    }

}
