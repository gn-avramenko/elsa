/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.model;

import com.gridnine.elsa.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.core.model.common.BaseIdentity;
import com.gridnine.elsa.core.model.common.Xeption;
import com.gridnine.elsa.core.model.domain.BaseProjection;
import com.gridnine.elsa.core.model.domain.VersionInfo;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.storage.repository.RepositoryObjectData;
import com.gridnine.elsa.server.storage.repository.RepositoryProjectionWrapper;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandlerFactory;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcBlobFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcEntityReferenceFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcIntFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcIntIdFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcLocalDateTimeFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcLongFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcLongIdFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcStringFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.JdbcTextFieldHandler;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


public class JdbcDatabaseMetadataProvider {

    private final Map<String, JdbcTableDescription> descriptionsMap = new LinkedHashMap<>();

    private final Map<String, JdbcSequenceDescription> sequencesMap = new LinkedHashMap<>();

    public final static String OBJECT_ID_COLUMN = "objectId";

    public Map<String, JdbcTableDescription> getDescriptions() {
        return descriptionsMap;
    }

    public Map<String, JdbcSequenceDescription> getSequencesMap() {
        return sequencesMap;
    }

    public void init(){
        sequencesMap.put("intid", new JdbcSequenceDescription("intid", JdbcSequenceType.INT));
        sequencesMap.put("longid", new JdbcSequenceDescription("longid", JdbcSequenceType.LONG));
        {
            var classMapping = new JdbcTableDescription("identifiers");
            classMapping.getFields().put("id", new JdbcLongIdFieldHandler());
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        {
            var classMapping = new JdbcTableDescription("classmapping");
            classMapping.getFields().put("id", new JdbcIntIdFieldHandler());
            classMapping.getFields().put("classname", new JdbcStringFieldHandler("classname", true));
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        for(String enumId: DomainMetaRegistry.get().getEnumsIds()){
            EnumDescription en = SerializableMetaRegistry.get().getEnums().get(enumId);
            var enumTable = new JdbcTableDescription(JdbcUtils.getTableName(en.getId()));
            enumTable.getFields().put("id", new JdbcIntIdFieldHandler());
            enumTable.getFields().put("enumconstant", new JdbcStringFieldHandler("enumconstant", false));
            for(Locale loc : SupportedLocalesProvider.get().getSupportedLocales()){
                var id = "%sName".formatted(loc.getLanguage());
                enumTable.getFields().put(id, new JdbcStringFieldHandler(id, true));
            }
            descriptionsMap.put(enumTable.getName(), enumTable);
        }
        for(String documentId: DomainMetaRegistry.get().getDocumentsIds()){
            var doc = SerializableMetaRegistry.get().getEntities().get(documentId);
            if("true".equals(doc.getAttributes().get("abstract"))){
                continue;
            }
            {
                var docTable = new JdbcTableDescription(JdbcUtils.getTableName(doc.getId()));
                docTable.getFields().put(BaseIdentity.Fields.id, new JdbcLongIdFieldHandler());
                docTable.getFields().put(RepositoryObjectData.Fields.data, new JdbcBlobFieldHandler(RepositoryObjectData.Fields.data));
                docTable.getFields().put(VersionInfo.Fields.revision, new JdbcIntFieldHandler(VersionInfo.Fields.revision, false));
                docTable.getFields().put(VersionInfo.Fields.modifiedBy, new JdbcStringFieldHandler(VersionInfo.Fields.modifiedBy, false));
                docTable.getFields().put(VersionInfo.Fields.modified, new JdbcLocalDateTimeFieldHandler(VersionInfo.Fields.modified, false));
                docTable.getFields().put(VersionInfo.Fields.comment, new JdbcStringFieldHandler(VersionInfo.Fields.comment, false));
                docTable.getFields().put(VersionInfo.Fields.versionNumber, new JdbcIntFieldHandler(VersionInfo.Fields.versionNumber, false));
                descriptionsMap.put(docTable.getName(), docTable);
            }
            {
                var versionTable = createVersionTable(doc.getId());
                descriptionsMap.put(versionTable.getName(), versionTable);
            }
            {
                var captionTable = createCaptionTable(doc.getId(), doc.getAttributes().get("localizable-caption-expression") != null);
                descriptionsMap.put(captionTable.getName(), captionTable);
            }
        }
        for(String assetId: DomainMetaRegistry.get().getAssetsIds()){
            var asset = SerializableMetaRegistry.get().getEntities().get(assetId);
            if("true".equals(asset.getAttributes().get("abstract"))){
                continue;
            }
            {
                var assetTable = new JdbcTableDescription(JdbcUtils.getTableName(asset.getId()));
                assetTable.getFields().put(BaseIdentity.Fields.id, new JdbcLongIdFieldHandler());
                assetTable.getFields().put(VersionInfo.Fields.revision, new JdbcIntFieldHandler(VersionInfo.Fields.revision, false));
                assetTable.getFields().put(VersionInfo.Fields.modifiedBy, new JdbcStringFieldHandler(VersionInfo.Fields.modifiedBy, false));
                assetTable.getFields().put(VersionInfo.Fields.modified, new JdbcLocalDateTimeFieldHandler(VersionInfo.Fields.modified, false));
                assetTable.getFields().put(VersionInfo.Fields.comment, new JdbcStringFieldHandler(VersionInfo.Fields.comment, false));
                assetTable.getFields().put(VersionInfo.Fields.versionNumber, new JdbcIntFieldHandler(VersionInfo.Fields.versionNumber, false));
                var parent = asset.getAttributes().get("extends");
                while (parent != null){
                    var parentDescr = SerializableMetaRegistry.get().getEntities().get(parent);
                    if(parentDescr == null){
                        throw Xeption.forDeveloper("unknown object type %s".formatted(parent));
                    }
                    fillBaseSearchableFields(assetTable, parentDescr);
                    parent = parentDescr.getAttributes().get("extends");
                }
                fillBaseSearchableFields(assetTable, asset);
                descriptionsMap.put(assetTable.getName(), assetTable);
            }
            {
                var versionTable = createVersionTable(asset.getId());
                descriptionsMap.put(versionTable.getName(), versionTable);
            }
            {
                var captionTable = createCaptionTable(asset.getId(), asset.getAttributes().get("localizable-caption-expression") != null);
                descriptionsMap.put(captionTable.getName(), captionTable);
            }
        }
        for(String projectionId: DomainMetaRegistry.get().getProjectionsIds()){
            var proj = SerializableMetaRegistry.get().getEntities().get(projectionId);
            var projTable = new JdbcTableDescription(JdbcUtils.getTableName(proj.getId()));
            projTable.getFields().put(BaseProjection.Fields.navigationKey, new JdbcIntFieldHandler("navigationkey", false));
            projTable.getFields().put(BaseProjection.Fields.document, new JdbcEntityReferenceFieldHandler(projectionId, BaseProjection.Fields.document, true));
            fillBaseSearchableFields(projTable, proj);
            descriptionsMap.put(projTable.getName(), projTable);
        }

    }



    private JdbcTableDescription createCaptionTable(String docId, boolean localizable) {
        var captionTable = new JdbcTableDescription(JdbcUtils.getCaptionTableName(docId));
        captionTable.getFields().put(BaseIdentity.Fields.id, new JdbcLongIdFieldHandler());
        if(localizable){
            SupportedLocalesProvider.get().getSupportedLocales().forEach(loc ->{
                var id = "%sCaption".formatted(loc.getCountry());
                captionTable.getFields().put(id, new JdbcStringFieldHandler(id, true));
            });
        } else {
            var id = "caption";
            captionTable.getFields().put(id, new JdbcStringFieldHandler(id, true));
        }
        return captionTable;
    }

    private JdbcTableDescription createVersionTable(String id) {
        var versionTable = new JdbcTableDescription(JdbcUtils.getVersionTableName(id));
        versionTable.getFields().put(OBJECT_ID_COLUMN, new JdbcLongFieldHandler(OBJECT_ID_COLUMN, true));
        versionTable.getFields().put(VersionInfo.Fields.versionNumber, new JdbcIntFieldHandler(VersionInfo.Fields.versionNumber, true));
        versionTable.getFields().put(RepositoryObjectData.Fields.data, new JdbcBlobFieldHandler(RepositoryObjectData.Fields.data));
        versionTable.getFields().put(VersionInfo.Fields.modifiedBy, new JdbcStringFieldHandler(VersionInfo.Fields.modifiedBy, false));
        versionTable.getFields().put(VersionInfo.Fields.modified, new JdbcLocalDateTimeFieldHandler(VersionInfo.Fields.modified, false));
        versionTable.getFields().put(VersionInfo.Fields.comment, new JdbcStringFieldHandler(VersionInfo.Fields.comment, false));
        return versionTable;
    }

    private void fillBaseSearchableFields(JdbcTableDescription table, EntityDescription descr) {
        table.getFields().put(RepositoryProjectionWrapper.Fields.aggregatedData, new JdbcTextFieldHandler(RepositoryProjectionWrapper.Fields.aggregatedData, true));
        for(PropertyDescription pd: descr.getProperties().values()){
            JdbcFieldHandlerFactory factory = JdbcRegistry.get().getFieldHandlerFactory(pd.getTagName());
            table.getFields().put(pd.getId(), factory.createHandler(descr.getId(), pd.getId(), true));
        }
    }

    public static JdbcDatabaseMetadataProvider get(){
        return Environment.getPublished(JdbcDatabaseMetadataProvider.class);
    }
}
