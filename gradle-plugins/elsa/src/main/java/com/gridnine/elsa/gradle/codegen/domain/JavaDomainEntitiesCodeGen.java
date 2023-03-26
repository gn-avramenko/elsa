/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Set;

public class JavaDomainEntitiesCodeGen {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, DomainTypesRegistry dtReg, File destDir, Set<File> generatedFiles) throws Exception {
        for(String enumId: registry.getEnumsIds()){
            JavaCodeGeneratorUtils.generateEnum(sRegistry.getEnums().get(enumId), destDir, generatedFiles);
        }
        for(String entityId: registry.getEntitiesIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(entityId, sRegistry,dtReg.getEntityTags());
            JavaCodeGeneratorUtils.generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
        }
        for(String entityId: registry.getDocumentsIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(entityId, sRegistry,dtReg.getEntityTags());
            ged.setExtendsId(ged.getExtendsId() == null? "com.gridnine.elsa.common.model.domain.BaseDocument": ged.getExtendsId());
            var ed = sRegistry.getEntities().get(ged.getId());
            ged.setToLocalizableStringExpression(ed.getAttributes().get("localizable-caption-expression"));
            ged.setToStringExpression(ed.getAttributes().get("caption-expression"));
            JavaCodeGeneratorUtils.generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
        }
        for(String projectionId: registry.getProjectionsIds()){
            var ed = sRegistry.getEntities().get(projectionId);
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(projectionId, sRegistry,dtReg.getDatabaseTags());
            ged.setExtendsId("com.gridnine.elsa.common.model.domain.BaseProjection<%s>".formatted(ed.getAttributes().get("document")));
            JavaCodeGeneratorUtils.generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
        }
        for(String assetId: registry.getAssetsIds()){
            var ed = sRegistry.getEntities().get(assetId);
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(assetId, sRegistry,dtReg.getDatabaseTags());
            ged.setExtendsId(ged.getExtendsId() != null? ged.getExtendsId(): "com.gridnine.elsa.common.model.domain.BaseAsset");
            ged.setToLocalizableStringExpression(ed.getAttributes().get("localizable-caption-expression"));
            ged.setToStringExpression(ed.getAttributes().get("caption-expression"));
            JavaCodeGeneratorUtils.generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
        }
    }
}
