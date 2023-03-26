/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.elsa.common.model.domain.BaseAsset;
import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.BaseProjection;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.search.*;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.storage.repository.Repository;
import com.gridnine.elsa.server.storage.repository.RepositoryAssetWrapper;
import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.RepositoryObjectData;
import com.gridnine.elsa.server.storage.repository.RepositoryProjectionWrapper;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcDatabaseMetadataProvider;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcTableDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;


public class JdbcRepository implements Repository {


    @Override
    public <A extends BaseAsset> RepositoryAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) {
        var description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(aClass.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.emptySet());
        var sql = "select %s from %s where %s = ?".formatted(TextUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = JdbcUtils.queryForSingleValue(sql, ps -> ps.setLong(1, id), rs -> ExceptionUtils.wrapException(() -> {
            var asset = aClass.getDeclaredConstructor().newInstance();
            var handler = new AssetWrapperReflectionHandler<>(asset);
            fillObject(rs, handler, description, Collections.emptySet(), Collections.emptySet());
            return handler;
        }));
        return result == null ? null : result.getWrapper();
    }


    @Override
    public <A extends BaseAsset> void saveAsset(RepositoryAssetWrapper<A> assetWrapper, RepositoryAssetWrapper<A> oldAsset) throws Exception {
        if (oldAsset != null) {
            Set<String> changedProperties = new LinkedHashSet<>();
            var handler = new AssetWrapperReflectionHandler<>(assetWrapper);
            var oldHandler = new AssetWrapperReflectionHandler<>(oldAsset);
            var description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
            entry:
            for (var entry : description.getFields().entrySet()) {
                var newValues = entry.getValue().getSqlValues(handler.getValue(entry.getKey()));
                var oldValues = entry.getValue().getSqlValues(oldHandler.getValue(entry.getKey()));
                if (newValues.size() != oldValues.size()) {
                    changedProperties.add(entry.getKey());
                    continue;
                }
                for (var entry2 : oldValues.entrySet()) {
                    var newValue = newValues.get(entry2.getKey());
                    if (newValue == null ||
                            !JdbcUtils.isEquals(entry2.getValue().key(), newValue.key())) {
                        changedProperties.add(entry.getKey());
                        continue entry;
                    }
                }
            }
            if (!changedProperties.isEmpty()) {
                var columnsValues = new LinkedHashMap<String, Pair<Object, String>>();
                for (var it : changedProperties) {
                    var fh = description.getFields().get(it);
                    columnsValues.putAll(fh.getSqlValues(handler.getValue(it)));
                }
                var query = "update %s set %s where id = ?".formatted(description.getName(),
                        TextUtils.join(columnsValues.keySet().stream().map("%s = ?"::formatted).toList(), ", "));
                JdbcUtils.update(query, (ps) -> {
                    int idx = 1;
                    for (Map.Entry<String, Pair<Object, String>> entry : columnsValues.entrySet()) {
                        var value = entry.getValue().key();
                        if (value == null) {
                            ps.setNull(idx, getSqlType(entry.getValue().value()));
                        } else {
                            setValue(ps, idx, entry.getValue().value(), entry.getValue().key());
                        }
                        idx++;
                    }
                    ps.setLong(columnsValues.size() + 1, assetWrapper.getAsset().getId());
                });
            }
            return;
        }
        insert(new AssetWrapperReflectionHandler<>(assetWrapper),
                JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
    }


    @Override
    public <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, RepositoryBinaryData data, VersionInfo vi) throws Exception {
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
        return searchObjects(cls, () -> new AssetWrapperReflectionHandler<>(cls.getDeclaredConstructor().newInstance()),
                updateQuery, properties, Collections.singleton(RepositoryAssetWrapper.Fields.aggregatedData)).stream().map(it -> it.wrapper.getAsset()).toList();
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
            var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(TextUtils.join(fields, ","),
                    JdbcUtils.getVersionTableName(cls.getName()), JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN);
            result.addAll(JdbcUtils.queryForList(selectSql, (ps) -> ps.setLong(1, id), (rs) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionInfo();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object;
            })));
        }
        {
            var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(TextUtils.join(fields, ","),
                    JdbcUtils.getTableName(cls.getName()), BaseIdentity.Fields.id);
            result.addAll(JdbcUtils.queryForList(selectSql, (ps) -> ps.setLong(1, id), (rs) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionInfo();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object;
            })));
        }
        return result;
    }

    @Override
    public <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) {
        JdbcUtils.update("delete from %s where id = ?".formatted(JdbcUtils.getTableName(aClass.getName())), (ps) -> ps.setLong(1, id));
    }

    @Override
    public RepositoryObjectData loadVersion(Class<?> cls, long id, int number) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Fields.versionNumber.toLowerCase());
        fields.add(VersionInfo.Fields.comment.toLowerCase());
        fields.add(VersionInfo.Fields.modified.toLowerCase());
        fields.add(VersionInfo.Fields.modifiedBy.toLowerCase());
        fields.add(RepositoryObjectData.Fields.data.toLowerCase());
        var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
        var selectSql = "select %s from %s where %s = ? and %s = ?".formatted(TextUtils.join(fields, ","),
                JdbcUtils.getVersionTableName(cls.getName()), JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN,
                VersionInfo.Fields.versionNumber);
        return JdbcUtils.queryForSingleValue(selectSql, (ps) -> {
            ps.setLong(1, id);
            ps.setInt(2, number);
        }, (rs) -> ExceptionUtils.wrapException(() -> {
            var object = new RepositoryObjectData();
            fillObject(rs, object, descr, fields, Collections.emptySet());
            return object;
        }));
    }

    @Override
    public void deleteVersion(Class<?> cls, long id, int number) {
        var version = loadVersion(cls, id, number);
        TransactionManager.get().withTransaction((ctx) ->{
            JdbcUtils.update("delete from %s where id = ? and number = ?".formatted(JdbcUtils.getVersionTableName(cls.getName())), (ps) -> {
                ps.setLong(1, id);
                ps.setInt(2, number);
            });
            withConnection((cnn) -> JdbcDialect.get().deleteBlob(cnn, version.getData().id()));
        });
    }

    @Override
    public <A extends BaseAsset> A loadAsset(Class<A> cls, long id) {
        var description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.singleton(RepositoryAssetWrapper.Fields.aggregatedData));
        var sql = "select %s from %s where %s = ?".formatted(TextUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = JdbcUtils.queryForSingleValue(sql, ps -> ps.setLong(1, id), rs -> ExceptionUtils.wrapException(() -> {
            var asset = cls.getDeclaredConstructor().newInstance();
            var handler = new AssetWrapperReflectionHandler<>(asset);
            fillObject(rs, handler, description, Collections.emptySet(), Collections.singleton(RepositoryAssetWrapper.Fields.aggregatedData));
            return handler;
        }));
        return result == null ? null : result.getWrapper().getAsset();
    }

    @Override
    public <D extends BaseDocument> RepositoryObjectData loadDocumentData(Class<D> cls, long id) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Fields.versionNumber);
        fields.add(VersionInfo.Fields.comment);
        fields.add(VersionInfo.Fields.modified);
        fields.add(VersionInfo.Fields.modifiedBy);
        fields.add(VersionInfo.Fields.revision);
        fields.add(RepositoryObjectData.Fields.data);
        var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var selectSql = "select %s from %s where %s = ?".formatted(TextUtils.join(fields, ","),
                JdbcUtils.getTableName(cls.getName()), BaseIdentity.Fields.id);
        return JdbcUtils.queryForSingleValue(selectSql, (ps) -> ps.setLong(1, id), (rs) -> ExceptionUtils.wrapException(() -> {
            var object = new RepositoryObjectData();
            fillObject(rs, object, descr, fields, Collections.emptySet());
            return object;
        }));
    }

    @Override
    public <D extends BaseDocument, I extends BaseProjection<D>> List<I> searchDocuments(Class<I> projClass, SearchQuery query) throws Exception {
        Set<String> properties = new LinkedHashSet<>();
        if (!query.getPreferredFields().isEmpty()) {
            properties.add(BaseIdentity.Fields.id);
            properties.add(BaseProjection.Fields.document);
            properties.add(BaseProjection.Fields.navigationKey);
            properties.addAll(query.getPreferredFields());
        }
        return searchObjects(projClass, () ->
                        new RepositoryProjectionWrapper<>(ReflectionFactory.get().newInstance(projClass), null),
                query, properties, Collections.singleton(RepositoryProjectionWrapper.Fields.aggregatedData)).stream().map(
                RepositoryProjectionWrapper::getProjection
        ).toList();
    }

    @Override
    public <A extends BaseAsset> List<List<Object>> searchAssets(Class<A> cls, AggregationQuery updateQuery) throws Exception {
        return aggregationSearchObjects(cls, updateQuery);
    }

    @Override
    public <D extends BaseDocument, I extends BaseProjection<D>> List<List<Object>> searchDocuments(Class<I> cls, AggregationQuery updateQuery) throws Exception {
        return aggregationSearchObjects(cls, updateQuery);
    }

    @Override
    public void updateProjections(Class<BaseProjection<BaseDocument>> projectionClass, long id, ArrayList<RepositoryProjectionWrapper<BaseDocument, BaseProjection<BaseDocument>>> wrappers, boolean update) throws Exception {
        var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(projectionClass.getName()));
        if (!update) {
            for (var proj : wrappers) {
                insert(proj,
                        JdbcUtils.getTableName(projectionClass.getName()));
            }
            return;
        }
        var selectSql = "select %s from %s where document = ?".formatted(
                TextUtils.join(getColumnNames(descr, Collections.emptySet(), Collections.emptySet()), ", "),
                descr.getName());
        var existingProjections = JdbcUtils.queryForList(selectSql, (ps) -> ps.setLong(1, id), (rs) -> ExceptionUtils.wrapException(() -> {
            var res = new RepositoryProjectionWrapper<>(ReflectionFactory.get().newInstance(projectionClass), null);
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
                JdbcUtils.update("delete from %s where document = ?".formatted(descr.getName()), (ps) -> ps.setLong(1, id));
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
                var newValues = entry.getValue().getSqlValues(wrapper.getValue(entry.getKey()));
                var oldValues = entry.getValue().getSqlValues(existing.getValue(entry.getKey()));
                if (newValues.size() != oldValues.size()) {
                    changedProperties.add(entry.getKey());
                    continue;
                }
                for (var entry2 : oldValues.entrySet()) {
                    var newValue = newValues.get(entry2.getKey());
                    if (newValue == null ||
                            !JdbcUtils.isEquals(entry2.getValue().key(), newValue.key())) {
                        changedProperties.add(entry.getKey());
                        continue entry;
                    }
                }
            }
            if (!changedProperties.isEmpty()) {
                var columnsValues = new LinkedHashMap<String, Pair<Object, String>>();
                for (var it : changedProperties) {
                    var fh = descr.getFields().get(it);
                    columnsValues.putAll(fh.getSqlValues(wrapper.getValue(it)));
                }
                var query = "update %s set %s where %s = ? and %s".formatted(descr.getName(),
                        TextUtils.join(columnsValues.keySet().stream().map("%s = ?"::formatted).toList(), ", "),
                        BaseProjection.Fields.document, existing.getProjection().getNavigationKey() == null ?
                                "%s is null".formatted(BaseProjection.Fields.navigationKey) :
                                "%s = ?".formatted(BaseProjection.Fields.navigationKey));
                JdbcUtils.update(query, (ps) -> {
                    int idx = 1;
                    for (Map.Entry<String, Pair<Object, String>> entry : columnsValues.entrySet()) {
                        var value = entry.getValue().key();
                        if (value == null) {
                            ps.setNull(idx, getSqlType(entry.getValue().value()));
                        } else {
                            setValue(ps, idx, entry.getValue().value(), entry.getValue().key());
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
            JdbcUtils.update("delete from %s where %s = ? and %s %s".formatted(
                            descr.getName(), BaseProjection.Fields.navigationKey, BaseProjection.Fields.navigationKey,
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
    public <D extends BaseDocument> void saveDocument(long id, Class<D> aClass, RepositoryObjectData obj, RepositoryObjectData oldDocument) throws Exception {
        if (oldDocument != null) {
            TransactionManager.get().withTransaction((ctx) ->{
                JdbcUtils.update("delete from %s where %s = ?".formatted(JdbcUtils.getTableName(aClass.getName()), BaseIdentity.Fields.id)
                        , (ps -> ps.setLong(1, id)));
                withConnection((cnn) -> JdbcDialect.get().deleteBlob(cnn, oldDocument.getData().id()));
            });
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
    public <D extends BaseDocument> void saveDocumentVersion(Class<D> aClass, long id, RepositoryObjectData version, Long oldVersionDataId) throws Exception {
        if (oldVersionDataId != null) {
            TransactionManager.get().withTransaction((ctx) ->{
                JdbcUtils.update("delete from %s where %s = ? and %s = ?"
                                .formatted(JdbcUtils.getVersionTableName(aClass.getName()),
                                        JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN, VersionInfo.Fields.versionNumber)
                        , ps -> {
                            ps.setLong(1, id);
                            ps.setInt(2, version.getVersionNumber());
                        });
                withConnection((cnn) -> JdbcDialect.get().deleteBlob(cnn, oldVersionDataId));
            });
        }
        var handler = new VersionWrapperReflectionHandler(version, version.getData(), id);
        insert(handler, JdbcUtils.getVersionTableName(aClass.getName()));
    }

    @Override
    public <D extends BaseDocument> void deleteDocument(Class<D> aClass, long id, Long oid) {
        TransactionManager.get().withTransaction((ctx) -> {
            JdbcUtils.update("delete from %s where %s = ?"
                            .formatted(JdbcUtils.getTableName(aClass.getName()),
                                    BaseIdentity.Fields.id)
                    , ps -> ps.setLong(1, id));
            withConnection((cnn) -> JdbcDialect.get().deleteBlob(cnn, oid));
        });
    }

    @Override
    public <D extends BaseIdentity> List<EntityReference<D>> searchCaptions(Class<D> cls, String pattern, int limit, Locale locale) {
        var localizable = SerializableMetaRegistry.get().getEntities().get(cls.getName()).getAttributes().containsKey("localizable-caption-expression");
        var column = "caption";
        if (localizable) {
            column = "%sCaption".formatted(locale.getLanguage());
        }
        //noinspection unchecked
        return JdbcUtils.queryForList("select %s, %s from %s %s order by %s asc limit %s".formatted(BaseIdentity.Fields.id, column,
                JdbcUtils.getCaptionTableName(cls.getName()), TextUtils.isBlank(pattern) ? "" :
                        "where %s %s '%s%%'".formatted(column, JdbcDialect.get().getIlikeFunctionName(), pattern.toLowerCase().trim()), column, limit), (rs) -> (EntityReference<D>) ExceptionUtils.wrapException(() -> {
                            var ref = new EntityReference<>();
                            ref.setId(rs.getLong(1));
            //noinspection unchecked
            ref.setType((Class<BaseIdentity>) cls);
                            ref.setCaption(rs.getString(2));
                            return ref;
                        }));
    }

    @Override
    public <D extends BaseDocument, I extends BaseProjection<D>> void deleteProjections(Class<I> projectionClass, long id) {
        JdbcUtils.update("delete from %s where %s = ?".formatted(JdbcUtils.getTableName(projectionClass.getName()), BaseProjection.Fields.document), (ps) -> ps.setLong(1, id));
    }

    @Override
    public <I extends BaseIdentity> String getCaption(Class<I> type, long id, Locale locale) {
        return JdbcUtils.queryForSingleValue("select %sCaption from %s where %s = ?".formatted(locale.getLanguage(),
                JdbcUtils.getCaptionTableName(type.getName()), BaseIdentity.Fields.id),  (ps) -> ps.setLong(1, id), (rs) -> rs.getString(1));
    }

    @Override
    public <I extends BaseIdentity> String getCaption(Class<I> type, long id) {
        return  JdbcUtils.queryForSingleValue("select caption from %s where %s = ?".formatted(JdbcUtils.getCaptionTableName(type.getName()),
                BaseIdentity.Fields.id),  (ps) -> ps.setLong(1, id), (rs) -> rs.getString(1));
    }

    private <E extends BaseIntrospectableObject> List<List<Object>> aggregationSearchObjects(
            Class<E> cls, AggregationQuery query) throws Exception {
        var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var wherePart = prepareWherePart(query.getCriterions(), query.getFreeText(), descr);
        var selectPart = prepareProjectionSelectPart(query);
        var groupByPart = prepareProjectionGroupByPart(query);
        var selectSql = "select %s from %s %s%s".formatted(selectPart, descr.getName(), wherePart.sql, groupByPart);
        var searchStatement = createPreparedStatementSetter(wherePart);
        return JdbcUtils.queryForList(selectSql, searchStatement, (rs) -> {
            var result = new ArrayList<>();
            for (int n = 0; n < query.getAggregations().size(); n++) {
                result.add(rs.getObject(n + 1));
            }
            return result;
        });
    }

    private String prepareProjectionGroupByPart(AggregationQuery query) {
        return TextUtils.join(query.getGroupBy().stream().map("group by %s"::formatted).toList(), ", ");
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
        return TextUtils.join(selectProperties, ", ");
    }

    private void insert(BaseIntrospectableObject obj, String tableName) throws Exception {
        var description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(tableName);
        var properties = description.getFields().keySet();
        var columnsValues = new LinkedHashMap<String, Pair<Object, String>>();
        for (var entry : description.getFields().entrySet()) {
            columnsValues.putAll(entry.getValue().getSqlValues(obj.getValue(entry.getKey())));
        }
        var query = "insert into %s (%s) values (%s)".formatted(description.getName(),
                TextUtils.join(columnsValues.keySet(), ", "),
                TextUtils.join(columnsValues.keySet().stream().map(it -> "?").toList(), ", "));
        JdbcUtils.update(query, (ps) -> {
            int idx = 1;
            for (Map.Entry<String, Pair<Object, String>> entry : columnsValues.entrySet()) {
                var value = entry.getValue().key();
                if (value == null) {
                    ps.setNull(idx, getSqlType(entry.getValue().value()));
                } else {
                    setValue(ps, idx, entry.getValue().value(), entry.getValue().key());
                }
                idx++;
            }
        });
    }

    @Override
    public void updateCaptions(Class<?> aClass, long id, String caption, boolean insert) {
        if (insert) {
            JdbcUtils.update("insert into %s(id, caption) values (?,?)".formatted(JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> {
                ps.setLong(1, id);
                ps.setString(2, caption);
            });
            return;
        }
        JdbcUtils.update("update %s set caption = ? where id = ?".formatted(
                JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> {
            ps.setString(1, caption);
            ps.setLong(2, id);
        });
    }

    @Override
    public void deleteCaptions(Class<?> aClass, long id) {
        JdbcUtils.update("delete from %s where id = ?".formatted(JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> ps.setLong(1, id));
    }

    @Override
    public void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) {
        if (insert) {
            JdbcUtils.update("insert into %s(id, %s) values (?, %s)".formatted(JdbcUtils.getCaptionTableName(aClass.getName()),
                    TextUtils.join(captions.keySet().stream().map(it -> "%sname".formatted(it.getLanguage())).toList(), ", "),
                    TextUtils.join(captions.keySet().stream().map(it -> "?").toList(), ", ")
            ), (ps) -> {
                ps.setLong(1, id);
                var cpts = captions.values().stream().toList();
                for (int n = 0; n < captions.size(); n++) {
                    ps.setString(2 + n, cpts.get(n));
                }
            });
            return;
        }
        JdbcUtils.update("update %s set %s where id = ?".formatted(
                JdbcUtils.getCaptionTableName(aClass.getName()),
                TextUtils.join(captions.keySet().stream().map(it -> "%sname = ?".formatted(it.getLanguage())).toList(), ", ")
        ), (ps) -> {
            var cpts = captions.values().stream().toList();
            for (int n = 0; n < captions.size(); n++) {
                ps.setString(1 + n, cpts.get(n));
            }
            ps.setLong(cpts.size(), id);
        });
    }

    private void setValue(PreparedStatement ps, int idx, String key, Object value) {
        ExceptionUtils.wrapException(() -> JdbcRegistry.get().getSqlTypeHandler(key).setValue(ps, idx, value));
    }

    private int getSqlType(String key) {
        return JdbcRegistry.get().getSqlTypeHandler(key).getSqlType();
    }

    private void fillObject(ResultSet rs, BaseIntrospectableObject object, JdbcTableDescription description,
                            Set<String> includedProperties, Set<String> excludedProperties) throws Exception {
        for (Map.Entry<String, JdbcFieldHandler> entry : description.getFields().entrySet()) {
            if (!isIncluded(entry.getKey(), includedProperties, excludedProperties)) {
                continue;
            }
            entry.getValue().setValue(object, entry.getKey(), entry.getValue().getModelValue(rs));
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
        var descr = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var wherePart = prepareWherePart(query.getCriterions(), query.getFreeText(), descr);
        var joinPart = prepareJoinPart(query.getOrders(), cls);
        var orderPart = prepareOrderPart(query.getOrders(), cls);
        var limitPart = prepareLimitPart(query);
        var selectSql = "select %s from %s ".formatted(TextUtils.join(getColumnNames(descr, properties, excludedProperties), ", ")
                , JdbcUtils.getTableName(cls.getName())) +
                joinPart +
                wherePart.sql +
                orderPart +
                limitPart;
        return JdbcUtils.queryForList(selectSql, createPreparedStatementSetter(wherePart), (rs) -> ExceptionUtils.wrapException(() -> {
            var object = factory.call();
            fillObject(rs, object, descr, properties, excludedProperties);
            return object;
        }));
    }

    private JdbcUtils.PreparedStatementSetter createPreparedStatementSetter(WherePartData wherePart) {
        return ps -> {
            for (int n = 0; n < wherePart.values().size(); n++) {
                var item = wherePart.values.get(n);
                setValue(ps, n + 1, item.value(), item.key());
            }
        };
    }

    private String prepareLimitPart(SearchQuery query) {
        return (query.getLimit() > 0 ? " limit %s".formatted(query.getLimit()) : "") + (query.getOffset() > 0 ? " offset %s".formatted(query.getOffset()) : "");
    }

    private String prepareOrderPart(Map<String, SortOrder> orders, Class<?> cls) {
        var descr = SerializableMetaRegistry.get().getEntities().get(cls.getName());
        var sb = new StringBuilder();
        for (var key : orders.keySet()) {
            var prop = descr.getProperties().get(key);
            if("enum-property".equals(prop.getTagName())){
                if (sb.length() == 0) {
                    sb.append("order by ");
                } else {
                    sb.append(", ");
                }
                sb.append("%sjoin.%sname".formatted(key.toLowerCase(), LocaleUtils.getCurrentLocale().getLanguage()));
                continue;
            }
            if("entity-reference-property".equals(prop.getTagName())){
                if (sb.length() == 0) {
                    sb.append("order by ");
                } else {
                    sb.append(", ");
                }
                EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(prop.getAttributes().get("class-name"));
                if (ed.getAttributes().get("localizable-caption-expression") != null) {
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
        var descr = SerializableMetaRegistry.get().getEntities().get(cls.getName());
        var sb = new StringBuilder();
        for (var key : orders.keySet()) {
            var prop = descr.getProperties().get(key);
            if("enum-property".equals(prop.getTagName())){
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                String className = prop.getAttributes().get("class-name");
                sb.append("left join %s as %s on %s = %2$s.id".formatted(JdbcUtils.getCaptionTableName(className),
                        "%sjoin".formatted(key.toLowerCase()), key.toLowerCase()));
                continue;
            }
            if("entity-reference-property".equals(prop.getTagName())){
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                String className = prop.getAttributes().get("class-name");
                sb.append("left join %s as %s on %s = %2$s.id".formatted(JdbcUtils.getCaptionTableName(className),
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
                    criterions.add(new SimpleCriterion(RepositoryProjectionWrapper.Fields.aggregatedData, SimpleCriterion.Operation.ILIKE, "%%%s%%".formatted(ptt.toLowerCase())));
                }
            }
        }
        if (criterions.isEmpty()) {
            return new WherePartData(Collections.emptyList(), "");
        }
        var values = new ArrayList<Pair<Object, String>>();
        var sql = new StringBuilder();
        var indexOfSQL = new AtomicReference<>(0);
        prepareWherePartInternal(sql, values, indexOfSQL, criterions, descr);
        return new WherePartData(values, "where %s".formatted(sql));
    }

    private String makeAndToken(int currentSQLIndex) {
        return currentSQLIndex > 0 ? " and " : "";
    }

    private void prepareWherePartInternal(StringBuilder sql,
                                          List<Pair<Object, String>> values, AtomicReference<Integer> indexOfSQL,
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
                    case ILIKE -> addSimpleCriterion(sql, values, sc.property, JdbcDialect.get().getIlikeFunctionName(),
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
        var subQuery = "%s%s %s 0".formatted(makeAndToken(currentSQLIndex), JdbcDialect.get().getCardinalitySql(property), operation);
        sql.insert(currentSQLIndex, subQuery);
        indexOfSql.set(currentSQLIndex + subQuery.length());
    }

    private void addSimpleCriterion(StringBuilder sql,
                                    List<Pair<Object, String>> values,
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

    private Pair<Object, String> getSqlQueryValue(Object value, JdbcTableDescription descr, String propertyName) throws Exception {
        return descr.getFields().get(propertyName).getSqlQueryValue(value);
    }

    private void withConnection(RunnableWithExceptionAndArgument<Connection> callback) throws Exception {
        Connection con = JdbcUtils.getConnection();
        callback.run(con);
    }

    static class AssetWrapperReflectionHandler<A extends BaseAsset> extends BaseIntrospectableObject {
        private final RepositoryAssetWrapper<A> wrapper;
        private final JdbcTableDescription description;

        AssetWrapperReflectionHandler(RepositoryAssetWrapper<A> wrapper) {
            this.wrapper = wrapper;
            description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(wrapper.getAsset().getClass().getName()));
        }

        AssetWrapperReflectionHandler(A asset) {
            this.wrapper = new RepositoryAssetWrapper<>();
            wrapper.setAsset(asset);
            wrapper.getAsset().setVersionInfo(new VersionInfo());
            description = JdbcDatabaseMetadataProvider.get().getDescriptions().get(JdbcUtils.getTableName(asset.getClass().getName()));
        }

        public RepositoryAssetWrapper<A> getWrapper() {
            return wrapper;
        }

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
            if (RepositoryAssetWrapper.Fields.aggregatedData.equals(propertyName)) {
                wrapper.setAggregatedData((String) value);
                return;
            }
            if (BaseIdentity.Fields.id.equals(propertyName)) {
                wrapper.getAsset().setId((Long) value);
                return;
            }
            description.getFields().get(propertyName).setValue(wrapper.getAsset(), propertyName, value);
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
            if (RepositoryAssetWrapper.Fields.aggregatedData.equals(propertyName)) {
                return wrapper.getAggregatedData();
            }
            return wrapper.getAsset().getValue(propertyName);
        }
    }


    static class DocumentWrapperReflectionHandler extends RepositoryObjectData {
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

        private RepositoryBinaryData data;

        private long objectid;

        VersionWrapperReflectionHandler(VersionInfo info, RepositoryBinaryData data, long objectid) {
            this.info = info;
            this.data = data;
            this.objectid = objectid;
        }

        @Override
        public void setValue(String propertyName, Object value) {
            if (RepositoryObjectData.Fields.data.equals(propertyName)) {
                data = (RepositoryBinaryData) value;
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
            if (RepositoryObjectData.Fields.data.equals(propertyName)) {
                return data;
            }
            if (JdbcDatabaseMetadataProvider.OBJECT_ID_COLUMN.equals(propertyName)) {
                return objectid;
            }
            return info.getValue(propertyName);
        }
    }


    record WherePartData(List<Pair<Object, String>> values, String sql) {
    }

}
