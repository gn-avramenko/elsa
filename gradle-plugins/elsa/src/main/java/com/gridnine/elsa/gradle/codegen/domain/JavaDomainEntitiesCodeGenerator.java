/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.common.meta.common.*;
import com.gridnine.elsa.common.meta.domain.*;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.GenEntityDescription;

import java.io.File;
import java.util.Set;

public class JavaDomainEntitiesCodeGenerator {
    public static void generate(DomainMetaRegistry registry, File destDir, Set<File> generatedFiles) throws Exception {
        for(EnumDescription ed : registry.getEnums().values()){
            CodeGeneratorUtils.generateJavaEnumCode(ed, destDir, generatedFiles);
        }
        for(EntityDescription ed: registry.getEntities().values()){
            CodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
        for(DocumentDescription dd: registry.getDocuments().values()){
            var ed = new GenEntityDescription();
            ed.setId(dd.getId());
            ed.setAbstract(dd.isAbstract());
            ed.setExtendsId(dd.getExtendsId() == null? "com.gridnine.elsa.common.core.model.domain.BaseDocument": dd.getExtendsId());
            ed.getMaps().putAll(dd.getMaps());
            ed.getProperties().putAll(dd.getProperties());
            ed.getCollections().putAll(dd.getCollections());
            ed.setToLocalizableStringExpression(dd.getLocalizableCaptionExpression());
            ed.setToStringExpression(dd.getCaptionExpression());
            CodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }

        for(SearchableProjectionDescription spd: registry.getSearchableProjections().values()){
            var ed = createSearchableDescription(spd);
            ed.setExtendsId("com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection<%s>".formatted(spd.getDocument()));
            CodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
        for(AssetDescription ad: registry.getAssets().values()){
            var ed = createSearchableDescription(ad);
            ed.setExtendsId("com.gridnine.elsa.common.core.model.domain.BaseAsset");
            ed.setToStringExpression(ad.getCaptionExpression());
            ed.setToLocalizableStringExpression(ad.getLocalizableCaptionExpression());
            CodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
    }

    private static GenEntityDescription createSearchableDescription(BaseSearchableDescription spd) {
        var ed = new GenEntityDescription();
        ed.setId(spd.getId());
        for(DatabasePropertyDescription prop: spd.getProperties().values()){
            var sp = new StandardPropertyDescription();
            sp.setId(prop.getId());
            sp.setType(switch (prop.getType()){
                case STRING,TEXT -> StandardValueType.STRING;
                case LOCAL_DATE -> StandardValueType.LOCAL_DATE;
                case LOCAL_DATE_TIME -> StandardValueType.LOCAL_DATE_TIME;
                case ENUM -> StandardValueType.ENUM;
                case BOOLEAN -> StandardValueType.BOOLEAN;
                case ENTITY_REFERENCE -> StandardValueType.ENTITY_REFERENCE;
                case LONG -> StandardValueType.LONG;
                case INT -> StandardValueType.INT;
                case BIG_DECIMAL -> StandardValueType.BIG_DECIMAL;
            });
            sp.setNullable(switch (prop.getType()){
                case BOOLEAN, LONG, INT -> true;
                default -> false;
            });
            sp.setClassName(prop.getClassName());
            ed.getProperties().put(prop.getId(), sp);
        }
        for(DatabaseCollectionDescription coll: spd.getCollections().values()){
            var sc = new StandardCollectionDescription();
            sc.setId(coll.getId());
            sc.setElementType(switch (coll.getElementType()){
                case STRING -> StandardValueType.STRING;
                case ENUM -> StandardValueType.ENUM;
                case ENTITY_REFERENCE -> StandardValueType.ENTITY_REFERENCE;
            });
            sc.setUnique(coll.isUnique());
            sc.setElementClassName(coll.getElementClassName());
            ed.getCollections().put(coll.getId(), sc);
        }
        return ed;
    }

}
