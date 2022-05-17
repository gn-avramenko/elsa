/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.model;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.storage.database.DatabaseSearchableProjectionWrapper;
import com.gridnine.elsa.server.core.storage.database.DatabaseObjectData;
import com.gridnine.elsa.server.core.storage.database.jdbc.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;


public class JdbcDatabaseMetadataProvider {

    private DomainMetaRegistry metaRegistry;
    private SupportedLocalesProvider supportedLocalesProvider;

    private final Map<String, JdbcTableDescription> descriptionsMap = new LinkedHashMap<>();

    private final Map<String, JdbcSequenceDescription> sequencesMap = new LinkedHashMap<>();

    @Autowired
    private ReflectionFactory reflectionFactory;

    public final static String OBJECT_ID_COLUMN = "objectId";

    @Autowired
    public void setMetaRegistry(DomainMetaRegistry metaRegistry){
        this.metaRegistry = metaRegistry;
    }

    @Autowired
    public void setSupportedLocalesProvider(SupportedLocalesProvider supportedLocalesProvider){
        this.supportedLocalesProvider = supportedLocalesProvider;
    }

    public Map<String, JdbcTableDescription> getDescriptions() {
        return descriptionsMap;
    }

    public Map<String, JdbcSequenceDescription> getSequencesMap() {
        return sequencesMap;
    }

    @PostConstruct
    public void init(){
        sequencesMap.put("intid", new JdbcSequenceDescription("intid", JdbcSequenceType.INT));
        sequencesMap.put("longid", new JdbcSequenceDescription("longid", JdbcSequenceType.LONG));
        {
            var classMapping = new JdbcTableDescription("identifiers");
            classMapping.getFields().put("id", new JdbcIdFieldHandler( true));
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        {
            var classMapping = new JdbcTableDescription("classmapping");
            classMapping.getFields().put("id", new JdbcIdFieldHandler( false));
            classMapping.getFields().put("classname", new JdbcStringFieldHandler("classname", true));
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        metaRegistry.getEnums().values().forEach(en ->{
            var enumTable = new JdbcTableDescription(JdbcUtils.getTableName(en.getId()));
            enumTable.getFields().put("id", new JdbcIdFieldHandler( false));
            enumTable.getFields().put("enumconstant", new JdbcStringFieldHandler("enumconstant", false));
            supportedLocalesProvider.getSupportedLocales().forEach(loc ->{
                var id = "%sName".formatted(loc.getLanguage());
                enumTable.getFields().put(id, new JdbcStringFieldHandler(id, true));
            });
            descriptionsMap.put(enumTable.getName(), enumTable);
        });
        metaRegistry.getDocuments().values().forEach(doc ->{
            if(!doc.isAbstract()) {
                {
                    var docTable = new JdbcTableDescription(JdbcUtils.getTableName(doc.getId()));
                    docTable.getFields().put(BaseIdentity.Fields.id, new JdbcIdFieldHandler(true));
                    docTable.getFields().put(DatabaseObjectData.Fields.data, new JdbcBlobFieldHandler(DatabaseObjectData.Fields.data));
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
                    var captionTable = createCaptionTable(doc.getId(), doc.getLocalizableCaptionExpression() != null);
                    descriptionsMap.put(captionTable.getName(), captionTable);
                }
            }
        });
        metaRegistry.getAssets().values().forEach(asset ->{
            if(!asset.isAbstract()) {
                {
                    var assetTable = new JdbcTableDescription(JdbcUtils.getTableName(asset.getId()));
                    assetTable.getFields().put(BaseIdentity.Fields.id, new JdbcIdFieldHandler( true));
                    assetTable.getFields().put(VersionInfo.Fields.revision, new JdbcIntFieldHandler(VersionInfo.Fields.revision, false));
                    assetTable.getFields().put(VersionInfo.Fields.modifiedBy, new JdbcStringFieldHandler(VersionInfo.Fields.modifiedBy, false));
                    assetTable.getFields().put(VersionInfo.Fields.modified, new JdbcLocalDateTimeFieldHandler(VersionInfo.Fields.modified, false));
                    assetTable.getFields().put(VersionInfo.Fields.comment, new JdbcStringFieldHandler(VersionInfo.Fields.comment, false));
                    assetTable.getFields().put(VersionInfo.Fields.versionNumber, new JdbcIntFieldHandler(VersionInfo.Fields.versionNumber, false));
                    var parent = asset.getExtendsId();
                    while (parent != null){
                        var parentDescr = metaRegistry.getAssets().get(parent);
                        if(parentDescr == null){
                            throw Xeption.forDeveloper("unknown object type %s".formatted(parent));
                        }
                        fillBaseSearchableFields(assetTable, parentDescr);
                        parent = asset.getExtendsId();
                    }
                    fillBaseSearchableFields(assetTable, asset);
                    descriptionsMap.put(assetTable.getName(), assetTable);
                }
                {
                    var versionTable = createVersionTable(asset.getId());
                    descriptionsMap.put(versionTable.getName(), versionTable);
                }
                {
                    var captionTable = createCaptionTable(asset.getId(), asset.getLocalizableCaptionExpression() != null);
                    descriptionsMap.put(captionTable.getName(), captionTable);
                }
            }
        });
        metaRegistry.getSearchableProjections().values().forEach(proj ->{
            {
                var projTable = new JdbcTableDescription(JdbcUtils.getTableName(proj.getId()));
                projTable.getFields().put(BaseSearchableProjection.Fields.navigationKey, new JdbcIntFieldHandler("navigationkey", false));

                projTable.getFields().put(BaseSearchableProjection.Fields.document, new JdbcEntityReferenceFieldHandler(BaseSearchableProjection.Fields.document, true,reflectionFactory.getClass(proj.getDocument()),
                        isAbstract(proj.getDocument()), isNotCachedCaption(proj.getDocument())));
                fillBaseSearchableFields(projTable, proj);
                descriptionsMap.put(projTable.getName(), projTable);
            }
        });

    }

    private boolean isAbstract(String document) {
        var docDescr = metaRegistry.getDocuments().get(document);
        if(docDescr != null){
            return docDescr.isAbstract();
        }
        var assetDescr = metaRegistry.getAssets().get(document);
        if(assetDescr != null){
            return assetDescr.isAbstract();
        }
        throw Xeption.forDeveloper("unknown object type %s".formatted(document));
    }

    private boolean isNotCachedCaption(String document) {
        var docDescr = metaRegistry.getDocuments().get(document);
        if(docDescr != null){
            return !docDescr.isCacheCaption() && !docDescr.isCacheResolve();
        }
        var assetDescr = metaRegistry.getAssets().get(document);
        if(assetDescr != null){
            return !assetDescr.isCacheCaption() && !assetDescr.isCacheResolve();
        }
        throw Xeption.forDeveloper("unknown object type %s".formatted(document));
    }

    private JdbcTableDescription createCaptionTable(String docId, boolean localizable) {
        var captionTable = new JdbcTableDescription(JdbcUtils.getCaptionTableName(docId));
        captionTable.getFields().put(BaseIdentity.Fields.id, new JdbcIdFieldHandler(true));
        if(localizable){
            supportedLocalesProvider.getSupportedLocales().forEach(loc ->{
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
        versionTable.getFields().put(DatabaseObjectData.Fields.data, new JdbcBlobFieldHandler(DatabaseObjectData.Fields.data));
        versionTable.getFields().put(VersionInfo.Fields.modifiedBy, new JdbcStringFieldHandler(VersionInfo.Fields.modifiedBy, false));
        versionTable.getFields().put(VersionInfo.Fields.modified, new JdbcLocalDateTimeFieldHandler(VersionInfo.Fields.modified, false));
        versionTable.getFields().put(VersionInfo.Fields.comment, new JdbcStringFieldHandler(VersionInfo.Fields.comment, false));
        return versionTable;
    }

    private void fillBaseSearchableFields(JdbcTableDescription table, BaseSearchableDescription search) {
        table.getFields().put(DatabaseSearchableProjectionWrapper.Fields.aggregatedData, new JdbcTextFieldHandler(DatabaseSearchableProjectionWrapper.Fields.aggregatedData, true));
        search.getProperties().values().forEach(prop ->{
            var propId = prop.getId();
            var handler = switch (prop.getType()){
                case STRING -> new JdbcStringFieldHandler(propId, true);
                case TEXT -> new JdbcTextFieldHandler(propId, true);
                case LOCAL_DATE -> new JdbcLocalDateFieldHandler(propId, true);
                case LOCAL_DATE_TIME -> new JdbcLocalDateTimeFieldHandler(propId, true);
                case ENUM -> new JdbcEnumFieldHandler(propId, reflectionFactory.getClass(prop.getClassName()), true);
                case BOOLEAN ->  new JdbcBooleanFieldHandler(propId, true);
                case ENTITY_REFERENCE ->  new JdbcEntityReferenceFieldHandler(propId, true, reflectionFactory.getClass(prop.getClassName()),
                        isAbstract(prop.getClassName()), isNotCachedCaption(prop.getClassName()));
                case LONG -> new JdbcLongFieldHandler(propId, true);
                case INT -> new JdbcIntFieldHandler(propId, true);
                case BIG_DECIMAL -> new JdbcBigDecimalFieldHandler(propId, true);
            };
            table.getFields().put(propId, handler);
        });
        search.getCollections().values().forEach(coll ->{
            var collId = coll.getId();
            var handler = switch (coll.getElementType()){
                case STRING -> new JdbcStringCollectionFieldHandler(collId, true);
                case ENUM -> new JdbcEnumCollectionFieldHandler(collId, reflectionFactory.getClass(coll.getElementClassName()), true);
                case ENTITY_REFERENCE ->  new JdbcEntityReferenceCollectionFieldHandler(collId, true, reflectionFactory.getClass(coll.getElementClassName()),
                        isAbstract(coll.getElementClassName()), isNotCachedCaption(coll.getElementClassName()));
            };
            table.getFields().put(collId, handler);
        });
    }
}
