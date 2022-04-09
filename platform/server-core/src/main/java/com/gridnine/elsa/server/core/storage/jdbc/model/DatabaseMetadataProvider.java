/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import com.gridnine.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DatabaseMetadataProvider {
    public final  static String AGGREGATED_DATA_COLUMN= "aggregateddata";
    public final static String OBJECT_ID_COLUMN= "objectid";
    public final static String DATA_COLUMN= "data";

    private DomainMetaRegistry metaRegistry;
    private SupportedLocalesProvider supportedLocalesProvider;

    private final Map<String, DatabaseTableDescription> descriptionsMap = new LinkedHashMap<>();

    private final Map<String, SequenceDescription> sequencesMap = new LinkedHashMap<>();

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    public void setMetaRegistry(DomainMetaRegistry metaRegistry){
        this.metaRegistry = metaRegistry;
    }

    @Autowired
    public void setSupportedLocalesProvider(SupportedLocalesProvider supportedLocalesProvider){
        this.supportedLocalesProvider = supportedLocalesProvider;
    }

    public Map<String, DatabaseTableDescription> getDescriptions() {
        return descriptionsMap;
    }

    public Map<String, SequenceDescription> getSequencesMap() {
        return sequencesMap;
    }

    @PostConstruct
    public void init(){
        sequencesMap.put("intid", new SequenceDescription("intid", SequenceType.INT));
        sequencesMap.put("longid", new SequenceDescription("longid", SequenceType.LONG));
        {
            var classMapping = new DatabaseTableDescription("identifiers");
            classMapping.getFields().put("id", new IdDatabaseFieldHandler( true));
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        {
            var classMapping = new DatabaseTableDescription("classmapping");
            classMapping.getFields().put("id", new IdDatabaseFieldHandler( false));
            classMapping.getFields().put("classname", new StringDatabaseFieldHandler("classname", true));
            descriptionsMap.put(classMapping.getName(), classMapping);
        }
        metaRegistry.getEnums().values().forEach(en ->{
            var enumTable = new DatabaseTableDescription(JdbcUtils.getTableName(en.getId()));
            enumTable.getFields().put("id", new IdDatabaseFieldHandler( false));
            enumTable.getFields().put("enumconstant", new StringDatabaseFieldHandler("enumconstant", false));
            supportedLocalesProvider.getSupportedLocales().forEach(loc ->{
                var id = "%sName".formatted(loc.getLanguage());
                enumTable.getFields().put(id, new StringDatabaseFieldHandler(id, true));
            });
            descriptionsMap.put(enumTable.getName(), enumTable);
        });
        metaRegistry.getDocuments().values().forEach(doc ->{
            if(!doc.isAbstract()) {
                {
                    var docTable = new DatabaseTableDescription(JdbcUtils.getTableName(doc.getId()));
                    docTable.getFields().put(BaseIdentity.Fields.id, new IdDatabaseFieldHandler(true));
                    docTable.getFields().put(DATA_COLUMN, new BlobDatabaseFieldHandler(DATA_COLUMN));
                    docTable.getFields().put(VersionInfo.Properties.revision, new IntDatabaseFieldHandler(VersionInfo.Properties.revision, false));
                    docTable.getFields().put(VersionInfo.Properties.modifiedBy, new StringDatabaseFieldHandler(VersionInfo.Properties.modifiedBy, false));
                    docTable.getFields().put(VersionInfo.Properties.modified, new LocalDateTimeDatabaseFieldHandler(VersionInfo.Properties.modified, false));
                    docTable.getFields().put(VersionInfo.Properties.comment, new StringDatabaseFieldHandler(VersionInfo.Properties.comment, false));
                    docTable.getFields().put(VersionInfo.Properties.versionNumber, new IntDatabaseFieldHandler(VersionInfo.Properties.versionNumber, false));
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
                    var assetTable = new DatabaseTableDescription(JdbcUtils.getTableName(asset.getId()));
                    assetTable.getFields().put(BaseIdentity.Fields.id, new IdDatabaseFieldHandler( true));
                    assetTable.getFields().put(VersionInfo.Properties.revision, new IntDatabaseFieldHandler(VersionInfo.Properties.revision, false));
                    assetTable.getFields().put(VersionInfo.Properties.modifiedBy, new StringDatabaseFieldHandler(VersionInfo.Properties.modifiedBy, false));
                    assetTable.getFields().put(VersionInfo.Properties.modified, new LocalDateTimeDatabaseFieldHandler(VersionInfo.Properties.modified, false));
                    assetTable.getFields().put(VersionInfo.Properties.comment, new StringDatabaseFieldHandler(VersionInfo.Properties.comment, false));
                    assetTable.getFields().put(VersionInfo.Properties.versionNumber, new IntDatabaseFieldHandler(VersionInfo.Properties.versionNumber, false));
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
                var projTable = new DatabaseTableDescription(JdbcUtils.getTableName(proj.getId()));
                projTable.getFields().put(BaseSearchableProjection.Fields.navigationKey, new IntDatabaseFieldHandler("navigationkey", false));

                projTable.getFields().put(BaseSearchableProjection.Fields.document, new EntityReferenceDatabaseFieldHandler(BaseSearchableProjection.Fields.document, true,reflectionFactory.getClass(proj.getDocument()),
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

    private DatabaseTableDescription createCaptionTable(String docId, boolean localizable) {
        var captionTable = new DatabaseTableDescription(JdbcUtils.getCaptionTableName(docId));
        captionTable.getFields().put(BaseIdentity.Fields.id, new LongDatabaseFieldHandler(BaseIdentity.Fields.id, true));
        if(localizable){
            supportedLocalesProvider.getSupportedLocales().forEach(loc ->{
                var id = "%sCaption".formatted(loc.getCountry());
                captionTable.getFields().put(id, new StringDatabaseFieldHandler(id, true));
            });
        } else {
            var id = "caption";
            captionTable.getFields().put(id, new StringDatabaseFieldHandler(id, true));
        }
        return captionTable;
    }

    private DatabaseTableDescription createVersionTable(String id) {
        var versionTable = new DatabaseTableDescription(JdbcUtils.getVersionTableName(id));
        versionTable.getFields().put(OBJECT_ID_COLUMN, new LongDatabaseFieldHandler(OBJECT_ID_COLUMN, true));
        versionTable.getFields().put(VersionInfo.Properties.versionNumber, new IntDatabaseFieldHandler(VersionInfo.Properties.versionNumber, true));
        versionTable.getFields().put(DATA_COLUMN, new BlobDatabaseFieldHandler(DATA_COLUMN));
        versionTable.getFields().put(VersionInfo.Properties.modifiedBy, new StringDatabaseFieldHandler(VersionInfo.Properties.modifiedBy, false));
        versionTable.getFields().put(VersionInfo.Properties.modified, new LocalDateTimeDatabaseFieldHandler(VersionInfo.Properties.modified, false));
        versionTable.getFields().put(VersionInfo.Properties.comment, new StringDatabaseFieldHandler(VersionInfo.Properties.comment, false));
        return versionTable;
    }

    private void fillBaseSearchableFields(DatabaseTableDescription table, BaseSearchableDescription search) {
        table.getFields().put(AGGREGATED_DATA_COLUMN, new TextDatabaseFieldHandler(AGGREGATED_DATA_COLUMN, true));
        search.getProperties().values().forEach(prop ->{
            var propId = prop.getId();
            var handler = switch (prop.getType()){
                case STRING -> new StringDatabaseFieldHandler(propId, true);
                case TEXT -> new TextDatabaseFieldHandler(propId, true);
                case LOCAL_DATE -> new LocalDateDatabaseFieldHandler(propId, true);
                case LOCAL_DATE_TIME -> new LocalDateTimeDatabaseFieldHandler(propId, true);
                case ENUM -> new EnumDatabaseFieldHandler(propId, reflectionFactory.getClass(prop.getClassName()), true);
                case BOOLEAN ->  new BooleanDatabaseFieldHandler(propId, true);
                case ENTITY_REFERENCE ->  new EntityReferenceDatabaseFieldHandler(propId, true, reflectionFactory.getClass(prop.getClassName()),
                        isAbstract(prop.getClassName()), isNotCachedCaption(prop.getClassName()));
                case LONG -> new LongDatabaseFieldHandler(propId, true);
                case INT -> new IntDatabaseFieldHandler(propId, true);
                case BIG_DECIMAL -> new BigDecimalDatabaseFieldHandler(propId, true);
            };
            table.getFields().put(propId, handler);
        });
        search.getCollections().values().forEach(coll ->{
            var collId = coll.getId();
            var handler = switch (coll.getElementType()){
                case STRING -> new StringCollectionDatabaseFieldHandler(collId, true);
                case ENUM -> new EnumDatabaseFieldHandler(collId, reflectionFactory.getClass(coll.getElementClassName()), true);
                case ENTITY_REFERENCE ->  new EntityReferenceDatabaseFieldHandler(collId, true, reflectionFactory.getClass(coll.getElementClassName()),
                        isAbstract(coll.getElementClassName()), isNotCachedCaption(coll.getElementClassName()));
            };
            table.getFields().put(collId, handler);
        });
    }
}
