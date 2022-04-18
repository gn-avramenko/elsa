/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc;

import com.gridnine.elsa.common.core.model.common.*;
import com.gridnine.elsa.common.core.model.domain.*;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.search.*;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.core.utils.TextUtils;
import com.gridnine.elsa.common.meta.domain.AssetDescription;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.storage.database.*;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.handlers.JdbcFieldHandler;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcDatabaseMetadataProvider;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcTableDescription;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;


@SuppressWarnings("ClassCanBeRecord")
public class JdbcDatabase implements Database {
    private final JdbcTemplate template;

    private final JdbcDatabaseMetadataProvider dbMetadataProvider;

    private final EnumMapper enumMapper;

    private final ClassMapper classMapper;

    private final JdbcDialect dialect;

    private final  DomainMetaRegistry domainMetaRegistry;

    private final ReflectionFactory reflectionFactory;

    public JdbcDatabase(JdbcTemplate template, JdbcDatabaseMetadataProvider dbMetadataProvider, EnumMapper enumMapper, ClassMapper classMapper, 
                        JdbcDialect dialect, DomainMetaRegistry domainMetaRegistry, ReflectionFactory reflectionFactory) {
        this.template = template;
        this.dbMetadataProvider = dbMetadataProvider;
        this.enumMapper = enumMapper;
        this.classMapper = classMapper;
        this.dialect = dialect;
        this.domainMetaRegistry = domainMetaRegistry;
        this.reflectionFactory = reflectionFactory;
    }

    @Override
    public <A extends BaseAsset> DatabaseAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) {
        var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(aClass.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.emptySet());
        var sql = "select %s from %s where %s = ?".formatted(StringUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = template.query(sql, ps -> ps.setLong(1, id), rs -> {
            if (!rs.next()) {
                return null;
            }
            return ExceptionUtils.wrapException(() -> {
                var asset = aClass.getDeclaredConstructor().newInstance();
                var handler = new AssetWrapperReflectionHandler<>(asset, domainMetaRegistry);
                fillObject(rs, handler, description, Collections.emptySet(), Collections.emptySet());
                return handler;
            });
        });
        return result == null ? null : result.getWrapper();
    }


    @Override
    public <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> assetWrapper, DatabaseAssetWrapper<A> oldAsset) throws Exception {
        if (oldAsset != null) {
            Set<String> changedProperties = new LinkedHashSet<>();
            var handler = new AssetWrapperReflectionHandler<>(assetWrapper, domainMetaRegistry);
            var oldHandler = new AssetWrapperReflectionHandler<>(oldAsset, domainMetaRegistry);
            var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
            entry:
            for (var entry : description.getFields().entrySet()) {
                var newValues = entry.getValue().getSqlValues(handler.getValue(entry.getKey()), enumMapper, classMapper, reflectionFactory);
                var oldValues = entry.getValue().getSqlValues(oldHandler.getValue(entry.getKey()), enumMapper, classMapper, reflectionFactory);
                if (newValues.size() != oldValues.size()) {
                    changedProperties.add(entry.getKey());
                    continue;
                }
                for (var entry2 : oldValues.entrySet()) {
                    var newValue = newValues.get(entry2.getKey());
                    if (newValue == null ||
                            !JdbcUtils.isEquals(entry2.getValue().getKey(), newValue.getKey())) {
                        changedProperties.add(entry.getKey());
                        continue entry;
                    }
                }
            }
            if (!changedProperties.isEmpty()) {
                var columnsValues = new LinkedHashMap<String, Pair<Object, JdbcFieldType>>();
                for (var it : changedProperties) {
                    var fh = description.getFields().get(it);
                    columnsValues.putAll(fh.getSqlValues(handler.getValue(it), enumMapper, classMapper, reflectionFactory));
                }
                var query = "update %s set %s where id = ?".formatted(description.getName(),
                        StringUtils.join(columnsValues.keySet().stream().map("%s = ?"::formatted).toList(), ", "));
                template.update(query, (ps) -> {
                    int idx = 1;
                    for (Map.Entry<String, Pair<Object, JdbcFieldType>> entry : columnsValues.entrySet()) {
                        var value = entry.getValue().getKey();
                        if (value == null) {
                            ps.setNull(idx, getSqlType(entry.getValue().getValue()));
                        } else {
                            setValue(ps, idx, entry.getValue().getValue(), entry.getValue().getKey());
                        }
                        idx++;
                    }
                    ps.setLong(columnsValues.size() + 1, assetWrapper.getAsset().getId());
                });
            }
            return;
        }
        insert(new AssetWrapperReflectionHandler<>(assetWrapper, domainMetaRegistry),
                JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
    }


    @Override
    public <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, DatabaseBinaryData data, VersionInfo vi) throws Exception {
        var handler = new VersionWrapperReflectionHandler(vi, data, id);
        insert(handler, JdbcUtils.getVersionTableName(aClass.getName()));
    }

    @Override
    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception {
        Set<String> properties = new LinkedHashSet<>();
        if (!updateQuery.getPreferredFields().isEmpty()) {
            properties.add(BaseIdentity.Fields.id);
            properties.add(VersionInfo.Fields.revision);
            properties.add(VersionInfo.Fields.comment);
            properties.add(VersionInfo.Fields.modified);
            properties.add(VersionInfo.Fields.modifiedBy);
            properties.add(VersionInfo.Fields.versionNumber);
            properties.addAll(updateQuery.getPreferredFields());
        }
        return searchObjects(cls, () -> new AssetWrapperReflectionHandler<>(cls.getDeclaredConstructor().newInstance(), domainMetaRegistry),
                updateQuery, properties, Collections.singleton(DatabaseAssetWrapper.Fields.aggregatedData)).stream().map(it -> it.wrapper.getAsset()).toList();
    }

    @Override
    public List<VersionInfo> getVersionsMetadata(Class<?> cls, long id) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Fields.versionNumber.toLowerCase());
        fields.add(VersionInfo.Fields.comment.toLowerCase());
        fields.add(VersionInfo.Fields.modified.toLowerCase());
        fields.add(VersionInfo.Fields.modifiedBy.toLowerCase());
        var result = new ArrayList<VersionInfo>();
        {
            var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(StringUtils.join(fields, ","),
                    JdbcUtils.getVersionTableName(cls.getName()), JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN);
            result.addAll(template.query(selectSql, (ps) -> ps.setLong(1, id), (rs, idx) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionInfo();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object;
            })));
        }
        {
            var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(StringUtils.join(fields, ","),
                    JdbcUtils.getTableName(cls.getName()), BaseIdentity.Fields.id);
            result.addAll(template.query(selectSql, (ps) -> ps.setLong(1, id), (rs, idx) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionInfo();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object;
            })));
        }
        return result;
    }

    @Override
    public <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) {
        template.update("delete from %s where id = ?".formatted(JdbcUtils.getTableName(aClass.getName())), (ps) -> ps.setLong(1, id));
    }

    @Override
    public DatabaseObjectData loadVersion(Class<?> cls, long id, int number) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Fields.versionNumber.toLowerCase());
        fields.add(VersionInfo.Fields.comment.toLowerCase());
        fields.add(VersionInfo.Fields.modified.toLowerCase());
        fields.add(VersionInfo.Fields.modifiedBy.toLowerCase());
        fields.add(DatabaseObjectData.Fields.data.toLowerCase());
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
        var selectSql = "select %s from %s where %s = ? and %s = ?".formatted(StringUtils.join(fields, ","),
                JdbcUtils.getVersionTableName(cls.getName()), JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN,
                VersionInfo.Fields.versionNumber);
        var res = template.query(selectSql, (ps) -> {
            ps.setLong(1, id);
            ps.setInt(2, number);
        }, (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var object = new DatabaseObjectData();
            fillObject(rs, object, descr, fields, Collections.emptySet());
            return object;
        }));
        return res.get(0);
    }

    @Override
    public void deleteVersion(Class<?> cls, long id, int number) throws Exception {
        var version = loadVersion(cls, id, number);
        template.update("delete from %s where id = ? and number = ?".formatted(JdbcUtils.getVersionTableName(cls.getName())), (ps) -> {
            ps.setLong(1, id);
            ps.setInt(2, number);
        });
        withConnection((cnn) -> dialect.deleteBlob(cnn, version.getData().id()));
    }

    @Override
    public <A extends BaseAsset> A loadAsset(Class<A> cls, long id) {
        var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.singleton(DatabaseAssetWrapper.Fields.aggregatedData));
        var sql = "select %s from %s where %s = ?".formatted(StringUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = template.query(sql, ps -> ps.setLong(1, id), rs -> {
            if (!rs.next()) {
                return null;
            }
            return ExceptionUtils.wrapException(() -> {
                var asset = cls.getDeclaredConstructor().newInstance();
                var handler = new AssetWrapperReflectionHandler<>(asset, domainMetaRegistry);
                fillObject(rs, handler, description, Collections.emptySet(), Collections.singleton(DatabaseAssetWrapper.Fields.aggregatedData));
                return handler;
            });
        });
        return result == null ? null : result.getWrapper().getAsset();
    }

    @Override
    public <D extends BaseDocument> DatabaseObjectData loadDocumentData(Class<D> cls, long id) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Fields.versionNumber);
        fields.add(VersionInfo.Fields.comment);
        fields.add(VersionInfo.Fields.modified);
        fields.add(VersionInfo.Fields.modifiedBy);
        fields.add(VersionInfo.Fields.revision);
        fields.add(DatabaseObjectData.Fields.data);
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var selectSql = "select %s from %s where %s = ?".formatted(StringUtils.join(fields, ","),
                JdbcUtils.getTableName(cls.getName()), BaseIdentity.Fields.id);
        var res = template.query(selectSql, (ps) -> ps.setLong(1, id), (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var object = new DatabaseObjectData();
            fillObject(rs, object, descr, fields, Collections.emptySet());
            return object;
        }));
        return res.isEmpty() ? null : res.get(0);
    }

    @Override
    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<I> searchDocuments(Class<I> projClass, SearchQuery query) throws Exception {
        Set<String> properties = new LinkedHashSet<>();
        if (!query.getPreferredFields().isEmpty()) {
            properties.add(BaseIdentity.Fields.id);
            properties.add(BaseSearchableProjection.Fields.document);
            properties.add(BaseSearchableProjection.Fields.navigationKey);
            properties.addAll(query.getPreferredFields());
        }

        return searchObjects(projClass, () ->
                        new DatabaseSearchableProjectionWrapper<>(reflectionFactory.newInstance(projClass), domainMetaRegistry, null),
                query, properties, Collections.singleton(DatabaseSearchableProjectionWrapper.Fields.aggregatedData)).stream().map(
                DatabaseSearchableProjectionWrapper::getProjection
        ).toList();
    }

    @Override
    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) throws Exception {
        return aggregationSearchObjects(cls, updateQuery);
    }

    @Override
    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) throws Exception {
        return aggregationSearchObjects(cls, updateQuery);
    }

    @Override
    public void updateProjections(Class<BaseSearchableProjection<BaseDocument>> projectionClass, long id, ArrayList<DatabaseSearchableProjectionWrapper<BaseDocument, BaseSearchableProjection<BaseDocument>>> wrappers, boolean update) throws Exception {
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(projectionClass.getName()));
        if (!update) {
            for (var proj : wrappers) {
                insert(proj,
                        JdbcUtils.getTableName(projectionClass.getName()));
            }
            return;
        }
        var selectSql = "select %s from %s where document = ?".formatted(
                StringUtils.join(getColumnNames(descr, Collections.emptySet(), Collections.emptySet()), ", "),
                descr.getName());
        var existingProjections = template.query(selectSql, (ps) -> ps.setLong(1, id), (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var res = new DatabaseSearchableProjectionWrapper<>(reflectionFactory.newInstance(projectionClass), domainMetaRegistry, null);
            fillObject(rs, res, descr, Collections.emptySet(), Collections.emptySet());
            return res;
        }));
        if (!existingProjections.isEmpty()) {
            var navKeys = new HashSet<Integer>();
            var hasAmbiquity = new AtomicReference<>(false);
            existingProjections.forEach(it -> {
                var navKey = it.getProjection().getNavigationKey();
                if (navKeys.contains(navKey)) {
                    hasAmbiquity.set(true);
                }
                navKeys.add(navKey);
            });
            if (hasAmbiquity.get()) {
                template.update("delete from %s where document = ?".formatted(descr.getName()), (ps) -> ps.setLong(1, id));
                existingProjections.clear();
            }
        }
        var toDeleteNavigationKeys = new ArrayList<>(existingProjections.stream().map(it -> it.getProjection().getNavigationKey()).toList());
        for (var wrapper : wrappers) {
            var existing = existingProjections.stream()
                    .filter(it -> Objects.equals(it.getProjection().getNavigationKey(), wrapper.getProjection().getNavigationKey())).findFirst().orElse(null);
            if (existing == null) {
                insert(wrapper,
                        JdbcUtils.getTableName(projectionClass.getName()));
                continue;
            }
            toDeleteNavigationKeys.remove(existing.getProjection().getNavigationKey());
            Set<String> changedProperties = new LinkedHashSet<>();
            entry:
            for (var entry : descr.getFields().entrySet()) {
                var newValues = entry.getValue().getSqlValues(wrapper.getValue(entry.getKey()), enumMapper, classMapper, reflectionFactory);
                var oldValues = entry.getValue().getSqlValues(existing.getValue(entry.getKey()), enumMapper, classMapper, reflectionFactory);
                if (newValues.size() != oldValues.size()) {
                    changedProperties.add(entry.getKey());
                    continue;
                }
                for (var entry2 : oldValues.entrySet()) {
                    var newValue = newValues.get(entry2.getKey());
                    if (newValue == null ||
                            !JdbcUtils.isEquals(entry2.getValue().getKey(), newValue.getKey())) {
                        changedProperties.add(entry.getKey());
                        continue entry;
                    }
                }
            }
            if (!changedProperties.isEmpty()) {
                var columnsValues = new LinkedHashMap<String, Pair<Object, JdbcFieldType>>();
                for (var it : changedProperties) {
                    var fh = descr.getFields().get(it);
                    columnsValues.putAll(fh.getSqlValues(wrapper.getValue(it), enumMapper, classMapper, reflectionFactory));
                }
                var query = "update %s set %s where %s = ? and %s".formatted(descr.getName(),
                        StringUtils.join(columnsValues.keySet().stream().map("%s = ?"::formatted).toList(), ", "),
                        BaseSearchableProjection.Fields.document, existing.getProjection().getNavigationKey() == null ?
                                "%s is null".formatted(BaseSearchableProjection.Fields.navigationKey) :
                                "%s = ?".formatted(BaseSearchableProjection.Fields.navigationKey));
                template.update(query, (ps) -> {
                    int idx = 1;
                    for (Map.Entry<String, Pair<Object, JdbcFieldType>> entry : columnsValues.entrySet()) {
                        var value = entry.getValue().getKey();
                        if (value == null) {
                            ps.setNull(idx, getSqlType(entry.getValue().getValue()));
                        } else {
                            setValue(ps, idx, entry.getValue().getValue(), entry.getValue().getKey());
                        }
                        idx++;
                    }
                    ps.setLong(columnsValues.size() + 1, id);
                    if (existing.getProjection().getNavigationKey() != null) {
                        ps.setLong(columnsValues.size() + 2, existing.getProjection().getNavigationKey());
                    }
                });
            }
        }
        for (var navKey : toDeleteNavigationKeys) {
            template.update("delete from %s where %s = ? and %s %s".formatted(
                            descr.getName(), BaseSearchableProjection.Fields.navigationKey, BaseSearchableProjection.Fields.navigationKey,
                            navKey == null ? "is null" : "= ?"), ps -> {
                        ps.setLong(1, id);
                        if (navKey != null) {
                            ps.setInt(2, navKey);
                        }
                    }
            );
        }
    }

    @Override
    public <D extends BaseDocument> void saveDocument(long id, Class<D> aClass, DatabaseObjectData obj, DatabaseObjectData oldDocument) throws Exception {
        if (oldDocument != null) {
            template.update("delete from %s where %s = ?".formatted(JdbcUtils.getTableName(aClass.getName()), BaseIdentity.Fields.id)
                    , (ps -> ps.setLong(1, id)));
            withConnection((cnn) -> dialect.deleteBlob(cnn, oldDocument.getData().id()));
        }
        var wrapper = new DocumentWrapperReflectionHandler(id);
        wrapper.setData(obj.getData());
        wrapper.setComment(obj.getComment());
        wrapper.setRevision(obj.getRevision());
        wrapper.setModified(obj.getModified());
        wrapper.setModifiedBy(obj.getModifiedBy());
        wrapper.setVersionNumber(obj.getVersionNumber());
        insert(wrapper, JdbcUtils.getTableName(aClass.getName()));
    }

    @Override
    public <D extends BaseDocument> void saveDocumentVersion(Class<D> aClass, long id, DatabaseObjectData version, Long oldVersionDataId) throws Exception {
        if (oldVersionDataId != null) {
            template.update("delete from %s where %s = ? and %s = ?"
                            .formatted(JdbcUtils.getVersionTableName(aClass.getName()),
                                    JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN, VersionInfo.Fields.versionNumber)
                    , ps -> {
                        ps.setLong(1, id);
                        ps.setInt(2, version.getVersionNumber());
                    });
            withConnection((cnn) -> dialect.deleteBlob(cnn, oldVersionDataId));
        }
        var handler = new VersionWrapperReflectionHandler(version, version.getData(), id);
        insert(handler, JdbcUtils.getVersionTableName(aClass.getName()));
    }

    @Override
    public <D extends BaseDocument> void deleteDocument(Class<D> aClass, long id, Long oid) throws Exception {
        template.update("delete from %s where %s = ?"
                        .formatted(JdbcUtils.getTableName(aClass.getName()),
                                BaseIdentity.Fields.id)
                , ps -> ps.setLong(1, id));
        withConnection((cnn) -> dialect.deleteBlob(cnn, oid));
    }

    @Override
    public <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit, Locale locale) {
        var localizable = false;
        var descr = domainMetaRegistry.getDocuments().get(cls.getName());
        if (descr != null) {
            localizable = descr.getLocalizableCaptionExpression() != null;
        } else {
            localizable = domainMetaRegistry.getAssets().get(cls.getName()).getLocalizableCaptionExpression() != null;
        }
        var column = "caption";
        if (localizable) {
            column = "%sCaption".formatted(locale.getLanguage());
        }
        //noinspection unchecked
        return template.query("select %s, %s from %s %s order by %s asc limit %s".formatted(BaseIdentity.Fields.id, column,
                JdbcUtils.getCaptionTableName(cls.getName()), TextUtils.isBlank(pattern) ? "" :
                        "where %s %s '%s%%'".formatted(column, dialect.getIlikeFunctionName(), pattern.toLowerCase().trim()), column, limit), (rs,idx) -> (EntityReference<D>) ExceptionUtils.wrapException(() -> {
                            var ref = new EntityReference<>();
                            ref.setId(rs.getLong(1));
            //noinspection unchecked
            ref.setType((Class<BaseIdentity>) cls);
                            ref.setCaption(rs.getString(2));
                            return ref;
                        }));
    }

    @Override
    public <D extends BaseDocument, I extends BaseSearchableProjection<D>> void deleteProjections(Class<I> projectionClass, long id) {
        template.update("delete from %s where %s = ?".formatted(JdbcUtils.getTableName(projectionClass.getName()), BaseSearchableProjection.Fields.document), (ps) -> ps.setLong(1, id));
    }

    private <E extends BaseIntrospectableObject> List<List<Object>> aggregationSearchObjects(
            Class<E> cls, AggregationQuery query) throws Exception {
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var wherePart = prepareWherePart(query.getCriterions(), query.getFreeText(), descr);
        var selectPart = prepareProjectionSelectPart(query);
        var groupByPart = prepareProjectionGroupByPart(query);
        var selectSql = "select %s from %s %s%s".formatted(selectPart, descr.getName(), wherePart.sql, groupByPart);
        var searchStatement = createPreparedStatementSetter(wherePart);
        return template.query(selectSql, searchStatement, (rs, idx) -> {
            var result = new ArrayList<>();
            for (int n = 0; n < query.getAggregations().size(); n++) {
                result.add(rs.getObject(n + 1));
            }
            return result;
        });
    }

    private String prepareProjectionGroupByPart(AggregationQuery query) {
        return StringUtils.join(query.getGroupBy().stream().map("group by %s"::formatted).toList(), ", ");
    }

    private String prepareProjectionSelectPart(AggregationQuery query) {
        var selectProperties = new ArrayList<String>();
        for (var proj : query.getAggregations()) {
            switch (proj.operation()) {
                case MAX -> selectProperties.add("max(%s)".formatted(proj.property()));
                case MIN -> selectProperties.add("min(%s)".formatted(proj.property()));
                case AVG -> selectProperties.add("avg(%s)".formatted(proj.property()));
                case SUM -> selectProperties.add("sum(%s)".formatted(proj.property()));
                case COUNT -> selectProperties.add("count(%s)".formatted(proj.property()));
                case PROPERTY -> selectProperties.add(proj.property());
            }
        }
        return StringUtils.join(selectProperties, ", ");
    }

    private void insert(BaseIntrospectableObject obj, String tableName) throws Exception {
        var description = dbMetadataProvider.getDescriptions().get(tableName);
        var properties = description.getFields().keySet();
        var columnsValues = new LinkedHashMap<String, Pair<Object, JdbcFieldType>>();
        for (var entry : description.getFields().entrySet()) {
            columnsValues.putAll(entry.getValue().getSqlValues(obj.getValue(entry.getKey()), enumMapper, classMapper, reflectionFactory));
        }
        var query = "insert into %s (%s) values (%s)".formatted(description.getName(),
                StringUtils.join(columnsValues.keySet(), ", "),
                StringUtils.join(columnsValues.keySet().stream().map(it -> "?").toList(), ", "));
        template.update(query, (ps) -> {
            int idx = 1;
            for (Map.Entry<String, Pair<Object, JdbcFieldType>> entry : columnsValues.entrySet()) {
                var value = entry.getValue().getKey();
                if (value == null) {
                    ps.setNull(idx, getSqlType(entry.getValue().getValue()));
                } else {
                    setValue(ps, idx, entry.getValue().getValue(), entry.getValue().getKey());
                }
                idx++;
            }
        });
    }

    @Override
    public void updateCaptions(Class<?> aClass, long id, String caption, boolean insert) {
        if (insert) {
            template.update("insert into %s(id, caption) values (?,?)".formatted(JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> {
                ps.setLong(1, id);
                ps.setString(2, caption);
            });
            return;
        }
        template.update("update %s set caption = ? where id = ?".formatted(
                JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> {
            ps.setString(1, caption);
            ps.setLong(2, id);
        });
    }

    @Override
    public void deleteCaptions(Class<?> aClass, long id) {
        template.update("delete from %s where id = ?".formatted(JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> ps.setLong(1, id));
    }

    @Override
    public void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) {
        if (insert) {
            template.update("insert into %s(id, %s) values (?, %s)".formatted(JdbcUtils.getCaptionTableName(aClass.getName()),
                    StringUtils.join(captions.keySet().stream().map(it -> "%sname".formatted(it.getLanguage())), ", "),
                    StringUtils.join(captions.keySet().stream().map(it -> "?"), ", ")
            ), (ps) -> {
                ps.setLong(1, id);
                var cpts = captions.values().stream().toList();
                for (int n = 0; n < captions.size(); n++) {
                    ps.setString(2 + n, cpts.get(n));
                }
            });
            return;
        }
        template.update("update %s set %s where id = ?".formatted(
                JdbcUtils.getCaptionTableName(aClass.getName()),
                StringUtils.join(captions.keySet().stream().map(it -> "%sname = ?".formatted(it.getLanguage())), ", ")
        ), (ps) -> {
            var cpts = captions.values().stream().toList();
            for (int n = 0; n < captions.size(); n++) {
                ps.setString(1 + n, cpts.get(n));
            }
            ps.setLong(cpts.size(), id);
        });
    }

    @SuppressWarnings("unchecked")
    private void setValue(PreparedStatement ps, int idx, JdbcFieldType key, Object value) {
        ExceptionUtils.wrapException(() -> {
            switch (key) {
                case LONG_ID, LONG -> ps.setLong(idx, (long) value);
                case LONG_ARRAY -> {
                    var array = ps.getConnection().createArrayOf("bigint", ((Collection<Object>) value).toArray());
                    ps.setArray(idx, array);
                }
                case INT_ID, INT -> ps.setInt(idx, (int) value);
                case INT_ARRAY -> ps.setArray(idx, ps.getConnection().createArrayOf("integer", ((Collection<Object>) value).toArray()));
                case STRING, TEXT -> ps.setString(idx, (String) value);
                case STRING_ARRAY -> ps.setArray(idx, ps.getConnection().createArrayOf("varchar", ((Collection<Object>) value).toArray()));
                case BOOLEAN -> ps.setBoolean(idx, (boolean) value);
                case DATE -> ps.setDate(idx, (Date) value);
                case DATE_TIME -> ps.setTimestamp(idx, (Timestamp) value);
                case BIG_DECIMAL -> ps.setBigDecimal(idx, (BigDecimal) value);
                case BLOB -> dialect.setBlob(ps, idx, (DatabaseBinaryData) value);
            }
        });

    }

    private int getSqlType(JdbcFieldType key) {
        return switch (key) {
            case LONG_ID, LONG, LONG_ARRAY -> Types.BIGINT;
            case INT_ID, INT, INT_ARRAY -> Types.INTEGER;
            case STRING, STRING_ARRAY -> Types.VARCHAR;
            case BOOLEAN -> Types.BOOLEAN;
            case TEXT -> Types.LONGNVARCHAR;
            case DATE -> Types.DATE;
            case DATE_TIME -> Types.TIMESTAMP;
            case BIG_DECIMAL -> Types.NUMERIC;
            case BLOB -> Types.LONGVARBINARY;
        };
    }

    private void fillObject(ResultSet rs, BaseIntrospectableObject handler, JdbcTableDescription description,
                            Set<String> includedProperties, Set<String> excludedProperties) throws Exception {
        for (Map.Entry<String, JdbcFieldHandler> entry : description.getFields().entrySet()) {
            if (!isIncluded(entry.getKey(), includedProperties, excludedProperties)) {
                continue;
            }
            handler.setValue(entry.getKey(), entry.getValue().getModelValue(rs, enumMapper, classMapper, reflectionFactory, dialect));
        }
    }

    private Set<String> getColumnNames(JdbcTableDescription description, Set<String> includedProperties, Set<String> excludedProperties) {
        var result = new LinkedHashSet<String>();
        description.getFields().forEach((id, handler) -> {
            if (isIncluded(id, includedProperties, excludedProperties)) {
                result.addAll(handler.getColumns().keySet());
            }
        });
        return result;
    }

    private boolean isIncluded(String id, Set<String> includedProperties, Set<String> excludedProperties) {
        if (!includedProperties.isEmpty() && !includedProperties.contains(id)) {
            return false;
        }
        return excludedProperties.isEmpty() || !excludedProperties.contains(id);
    }

    private <A extends BaseIntrospectableObject> List<A> searchObjects(Class<?> cls, Callable<A> factory, SearchQuery query,
                                                                       Set<String> properties, Set<String> excludedProperties) throws Exception {
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var wherePart = prepareWherePart(query.getCriterions(), query.getFreeText(), descr);
        var joinPart = prepareJoinPart(query.getOrders(), cls);
        var orderPart = prepareOrderPart(query.getOrders(), cls);
        var limitPart = prepareLimitPart(query);
        var selectSql = "select %s from %s ".formatted(StringUtils.join(getColumnNames(descr, properties, excludedProperties), ", ")
                , JdbcUtils.getTableName(cls.getName())) +
                joinPart +
                wherePart.sql +
                orderPart +
                limitPart;
        return template.query(selectSql, createPreparedStatementSetter(wherePart), (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var object = factory.call();
            fillObject(rs, object, descr, properties, excludedProperties);
            return object;
        }));
    }

    private PreparedStatementSetter createPreparedStatementSetter(WherePartData wherePart) {
        return ps -> {
            for (int n = 0; n < wherePart.values().size(); n++) {
                var item = wherePart.values.get(n);
                setValue(ps, n + 1, item.getValue(), item.getKey());
            }
        };
    }

    private String prepareLimitPart(SearchQuery query) {
        return (query.getLimit() > 0 ? " limit %s".formatted(query.getLimit()) : "") + (query.getOffset() > 0 ? " offset %s".formatted(query.getOffset()) : "");
    }

    private String prepareOrderPart(Map<String, SortOrder> orders, Class<?> cls) {
        BaseSearchableDescription descr = domainMetaRegistry.getSearchableProjections().get(cls.getName());
        if (descr == null) {
            descr = domainMetaRegistry.getAssets().get(cls.getName());
        }
        var sb = new StringBuilder();
        for (var key : orders.keySet()) {
            var prop = descr.getProperties().get(key);
            if (prop != null && prop.getType() == DatabasePropertyType.ENUM) {
                if (sb.length() == 0) {
                    sb.append("order by ");
                } else {
                    sb.append(", ");
                }
                sb.append("%sjoin.%sname".formatted(key.toLowerCase(), LocaleUtils.getCurrentLocale().getLanguage()));
                continue;
            }
            if (prop != null && prop.getType() == DatabasePropertyType.ENTITY_REFERENCE) {
                if (sb.length() == 0) {
                    sb.append("order by ");
                } else {
                    sb.append(", ");
                }
                if (domainMetaRegistry.getDocuments().get(prop.getClassName()).getLocalizableCaptionExpression() != null) {
                    sb.append("%sjoin.%scaption".formatted(key, LocaleUtils.getCurrentLocale().getLanguage()));
                } else {
                    sb.append("%sjoin.caption".formatted(key));
                }
                sb.append(" %s".formatted(orders.get(key) == SortOrder.ASC ? "asc" : "desc"));
                continue;
            }
            if (sb.length() == 0) {
                sb.append("order by ");
            } else {
                sb.append(", ");
            }
            sb.append("%s %s".formatted(key.toLowerCase(), orders.get(key) == SortOrder.ASC ? "asc" : "desc"));
        }
        return sb.toString();
    }

    private String prepareJoinPart(Map<String, SortOrder> orders, Class<?> cls) {
        BaseSearchableDescription descr = domainMetaRegistry.getSearchableProjections().get(cls.getName());
        if (descr == null) {
            descr = domainMetaRegistry.getAssets().get(cls.getName());
        }
        if (descr == null) {
            return "";
        }
        var sb = new StringBuilder();
        for (var key : orders.keySet()) {
            var prop = descr.getProperties().get(key);
            if (prop != null && prop.getType() == DatabasePropertyType.ENUM) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append("left join %s as %s on %s = %2$s.id".formatted(JdbcUtils.getCaptionTableName(prop.getClassName()),
                        "%sjoin".formatted(key.toLowerCase()), key.toLowerCase()));
            }
            if (prop != null && prop.getType() == DatabasePropertyType.ENTITY_REFERENCE) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append("left join %s as %s on %s = %2$s.id".formatted(JdbcUtils.getCaptionTableName(prop.getClassName()),
                        "%sjoin".formatted(key.toLowerCase()), key.toLowerCase()));
            }
        }

        return sb.toString();
    }

    private WherePartData prepareWherePart(List<SearchCriterion> crits, String freeTextPattern,
                                           JdbcTableDescription descr) throws Exception {
        var criterions = new ArrayList<>(crits);
        if (!TextUtils.isBlank(freeTextPattern)) {
            for (var ptt : freeTextPattern.split(" ")) {
                if (!TextUtils.isBlank(ptt)) {
                    criterions.add(new SimpleCriterion(DatabaseSearchableProjectionWrapper.Fields.aggregatedData, SimpleCriterion.Operation.ILIKE, "%%%s%%".formatted(ptt.toLowerCase())));
                }
            }
        }
        if (criterions.isEmpty()) {
            return new WherePartData(Collections.emptyList(), "");
        }
        var values = new ArrayList<Pair<Object, JdbcFieldType>>();
        var sql = new StringBuilder();
        var indexOfSQL = new AtomicReference<>(0);
        prepareWherePartInternal(sql, values, indexOfSQL, criterions, descr);
        return new WherePartData(values, "where %s".formatted(sql));
    }

    private String makeAndToken(int currentSQLIndex) {
        return currentSQLIndex > 0 ? " and " : "";
    }

    private void prepareWherePartInternal(StringBuilder sql,
                                          List<Pair<Object, JdbcFieldType>> values, AtomicReference<Integer> indexOfSQL,
                                          List<SearchCriterion> criterions,
                                          JdbcTableDescription descr) throws Exception {
        for (var criterion : criterions) {
            if (criterion instanceof BetweenCriterion bc) {
                var currentSQLIndex = indexOfSQL.get();
                var subQuery = "%s%s between ? and ?".formatted(makeAndToken(currentSQLIndex), bc.property);
                sql.insert(currentSQLIndex, subQuery);
                indexOfSQL.set(currentSQLIndex + subQuery.length());
                values.add(getSqlQueryValue(bc.lo, descr, bc.property));
                continue;
            }
            if (criterion instanceof CheckCriterion cc) {
                switch (cc.check) {
                    case IS_EMPTY -> addCollectionCheckCriterion(sql, cc.property, "=", indexOfSQL, descr);
                    case NOT_EMPTY -> addCollectionCheckCriterion(sql, cc.property, ">", indexOfSQL, descr);
                    case IS_NULL -> addNullCheckCriterion(sql, cc.property, true, indexOfSQL, descr);
                    case IS_NOT_NULL -> addNullCheckCriterion(sql, cc.property, false, indexOfSQL, descr);
                }
                continue;
            }
            if (criterion instanceof InCriterion<?> ic) {
                var inBuilder = new StringBuilder();
                for (var value : ic.values) {
                    if (inBuilder.length() > 0) {
                        inBuilder.append(", ");
                    }
                    inBuilder.append("?");
                    values.add(getSqlQueryValue(value, descr, ic.property));
                }
                var currentSQLIndex = indexOfSQL.get();
                var subQuery = "%s%s in(%s)".formatted(makeAndToken(currentSQLIndex), ic.property, inBuilder);
                sql.insert(currentSQLIndex, subQuery);
                indexOfSQL.set(currentSQLIndex + subQuery.length());
                continue;
            }
            if (criterion instanceof NotCriterion nc) {
                var currentSQLIndex = indexOfSQL.get();
                var subSQL = new StringBuilder();
                var subIndexOfSql = new AtomicReference<>(0);
                prepareWherePartInternal(subSQL, values, subIndexOfSql,
                        List.of(nc.criterion), descr);
                sql.insert(currentSQLIndex, "%snot(%s)".formatted(makeAndToken(currentSQLIndex), subSQL));
                indexOfSQL.set(currentSQLIndex + subSQL.length());
                continue;
            }
            if (criterion instanceof JunctionCriterion jc) {
                var operation = jc.disjunction ? "or" : "and";
                var currentSQLIndex = indexOfSQL.get();
                var subSQL = new StringBuilder();
                for (var subCrit : jc.criterions) {
                    if (subSQL.length() > 0) {
                        subSQL.append(" %s ".formatted(operation));
                    }
                    var subSubSql = new StringBuilder();
                    var subIndexOfSql = new AtomicReference<>(0);
                    prepareWherePartInternal(subSubSql, values, subIndexOfSql,
                            List.of(subCrit),
                            descr);
                    subSQL.append(subSubSql);
                }
                String subSqlStr = "%s(%s)".formatted(makeAndToken(currentSQLIndex), subSQL);
                sql.insert(currentSQLIndex, subSqlStr);
                indexOfSQL.set(currentSQLIndex + subSqlStr.length());
                continue;
            }
            if (criterion instanceof SimpleCriterion sc) {
                switch (sc.operation) {
                    case EQ -> addSimpleCriterion(sql, values, sc.property, "=",
                            sc.value, indexOfSQL, descr);
                    case NE -> addSimpleCriterion(sql, values, sc.property, "!=",
                            sc.value, indexOfSQL, descr);
                    case LIKE -> addSimpleCriterion(sql, values, sc.property, "like",
                            sc.value, indexOfSQL, descr);
                    case ILIKE -> addSimpleCriterion(sql, values, sc.property, dialect.getIlikeFunctionName(),
                            sc.value, indexOfSQL, descr);
                    case GT -> addSimpleCriterion(sql, values, sc.property, ">",
                            sc.value, indexOfSQL, descr);
                    case LT -> addSimpleCriterion(sql, values, sc.property, "<",
                            sc.value, indexOfSQL, descr);
                    case GE -> addSimpleCriterion(sql, values, sc.property, ">=",
                            sc.value, indexOfSQL, descr);
                    case LE -> addSimpleCriterion(sql, values, sc.property, "<=",
                            sc.value, indexOfSQL, descr);
                    case CONTAINS -> {
                        var currentSQLIndex = indexOfSQL.get();
                        var subQuery = "%s ? = any(%s)".formatted(makeAndToken(currentSQLIndex), sc.property);
                        sql.insert(currentSQLIndex, subQuery);
                        indexOfSQL.set(currentSQLIndex + subQuery.length());
                        values.add(getSqlQueryValue(sc.value, descr, sc.property));
                    }
                }
                continue;
            }
            throw new IllegalArgumentException("unsupported criterion type " + criterion.getClass().getName());
        }
    }

    private void addNullCheckCriterion(StringBuilder sql, String property, boolean isNull, AtomicReference<Integer> indexOfSQL, JdbcTableDescription descr) {
        var currentSQLIndex = indexOfSQL.get();
        var subQuery = "%s%s is %s".formatted(makeAndToken(currentSQLIndex), property, isNull ? "null" : "not null");
        sql.insert(currentSQLIndex, subQuery);
        indexOfSQL.set(currentSQLIndex + subQuery.length());
    }


    private void addCollectionCheckCriterion(StringBuilder sql,
                                             String property,
                                             String operation,
                                             AtomicReference<Integer> indexOfSql,
                                             JdbcTableDescription descr) {
        var currentSQLIndex = indexOfSql.get();
        var subQuery = "%s%s %s 0".formatted(makeAndToken(currentSQLIndex), dialect.getCardinalitySql(property), operation);
        sql.insert(currentSQLIndex, subQuery);
        indexOfSql.set(currentSQLIndex + subQuery.length());
    }

    private void addSimpleCriterion(StringBuilder sql,
                                    List<Pair<Object, JdbcFieldType>> values,
                                    String property,
                                    String operation,
                                    Object value,
                                    AtomicReference<Integer> indexOfSql,
                                    JdbcTableDescription descr) throws Exception {
        var currentSQLIndex = indexOfSql.get();
        var subQuery = "%s%s %s ?".formatted(makeAndToken(currentSQLIndex), property, operation);
        sql.insert(currentSQLIndex, subQuery);
        indexOfSql.set(currentSQLIndex + subQuery.length());
        values.add(getSqlQueryValue(value, descr, property));
    }

    private Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, JdbcTableDescription descr, String propertyName) throws Exception {
        return descr.getFields().get(propertyName).getSqlQueryValue(value, enumMapper, classMapper, reflectionFactory);
    }

    private void withConnection(RunnableWithExceptionAndArgument<Connection> callback) throws Exception {
        Connection con = DataSourceUtils.getConnection(template.getDataSource());
        try {
            callback.run(con);
        } finally {
            DataSourceUtils.releaseConnection(con, template.getDataSource());
        }
    }

    static class AssetWrapperReflectionHandler<A extends BaseAsset> extends BaseIntrospectableObject {
        private final DatabaseAssetWrapper<A> wrapper;

        private final AssetDescription assetDescr;

        AssetWrapperReflectionHandler(DatabaseAssetWrapper<A> wrapper, DomainMetaRegistry registry) {
            this.wrapper = wrapper;
            this.assetDescr = registry.getAssets().get(wrapper.getAsset().getClass().getName());
        }

        AssetWrapperReflectionHandler(A asset, DomainMetaRegistry registry) {
            this.wrapper = new DatabaseAssetWrapper<>();
            wrapper.setAsset(asset);
            this.assetDescr = registry.getAssets().get(wrapper.getAsset().getClass().getName());
            wrapper.getAsset().setVersionInfo(new VersionInfo());
        }

        public DatabaseAssetWrapper<A> getWrapper() {
            return wrapper;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setValue(String propertyName, Object value) {
            if (VersionInfo.Fields.revision.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setValue("revision", value);
                return;
            }
            if (VersionInfo.Fields.modifiedBy.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setModifiedBy((String) value);
                return;
            }
            if (VersionInfo.Fields.modified.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setModified((LocalDateTime) value);
                return;
            }
            if (VersionInfo.Fields.comment.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setComment((String) value);
                return;
            }
            if (VersionInfo.Fields.versionNumber.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setVersionNumber((Integer) value);
                return;
            }
            if (DatabaseAssetWrapper.Fields.aggregatedData.equals(propertyName)) {
                wrapper.setAggregatedData((String) value);
                return;
            }
            if (BaseIdentity.Fields.id.equals(propertyName)) {
                wrapper.getAsset().setId((Long) value);
                return;
            }
            if (assetDescr.getProperties().containsKey(propertyName)) {
                wrapper.getAsset().setValue(propertyName, value);
                return;
            }

            var coll = (Collection<Object>) wrapper.getAsset().getCollection(propertyName);
            coll.clear();
            coll.addAll((Collection<Object>) value);

        }

        @Override
        public Object getValue(String propertyName) {
            if (VersionInfo.Fields.revision.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getRevision();
            }
            if (VersionInfo.Fields.modifiedBy.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getModifiedBy();
            }
            if (VersionInfo.Fields.modified.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getModified();
            }
            if (VersionInfo.Fields.comment.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getComment();
            }
            if (VersionInfo.Fields.versionNumber.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getVersionNumber();
            }
            if (BaseIdentity.Fields.id.equals(propertyName)) {
               return wrapper.getAsset().getId();
            }
            if (DatabaseAssetWrapper.Fields.aggregatedData.equals(propertyName)) {
                return wrapper.getAggregatedData();
            }
            return wrapper.getAsset().getValue(propertyName);
        }
    }


    static class DocumentWrapperReflectionHandler extends DatabaseObjectData {
        private long id;

        DocumentWrapperReflectionHandler(long id) {
            this.id = id;
        }

        @Override
        public void setValue(String propertyName, Object value) {
            if (BaseIdentity.Fields.id.equals(propertyName)) {
                id = (long) value;
                return;
            }
            super.setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (BaseIdentity.Fields.id.equals(propertyName)) {
                return id;
            }
            return super.getValue(propertyName);
        }
    }

    static class VersionWrapperReflectionHandler extends BaseIntrospectableObject {
        private final VersionInfo info;

        private DatabaseBinaryData data;

        private long objectid;

        VersionWrapperReflectionHandler(VersionInfo info, DatabaseBinaryData data, long objectid) {
            this.info = info;
            this.data = data;
            this.objectid = objectid;
        }

        @Override
        public void setValue(String propertyName, Object value) {
            if (DatabaseObjectData.Fields.data.equals(propertyName)) {
                data = (DatabaseBinaryData) value;
                return;
            }
            if (JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN.equals(propertyName)) {
                objectid = (long) value;
                return;
            }
            info.setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (DatabaseObjectData.Fields.data.equals(propertyName)) {
                return data;
            }
            if (JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN.equals(propertyName)) {
                return objectid;
            }
            return info.getValue(propertyName);
        }
    }


    record WherePartData(List<Pair<Object, JdbcFieldType>> values, String sql) {
    }

}
