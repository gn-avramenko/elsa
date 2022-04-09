/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.common.core.search.*;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.core.utils.TextUtils;
import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.server.core.storage.*;
import com.gridnine.elsa.server.core.storage.jdbc.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JdbcDatabase implements Database {
    private JdbcTemplate template;

    @Autowired
    private DatabaseMetadataProvider dbMetadataProvider;

    @Autowired
    private EnumMapper enumMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private JdbcDialect dialect;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    public void setDataSource(DataSource ds) {
        template = new JdbcTemplate(ds);
    }

    @Override
    public <A extends BaseAsset> DatabaseAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) throws Exception {
        var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(aClass.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.emptySet());
        var sql = "select %s from %s where %s = ?".formatted(StringUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = template.query(sql, ps -> ps.setLong(1, id), rs -> {
            if(!rs.next()){
                return null;
            }
            return ExceptionUtils.wrapException(() -> {
                var asset = aClass.getDeclaredConstructor().newInstance();
                var handler = new AssetWrapperReflectionHandler<>(asset);
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
            var handler = new AssetWrapperReflectionHandler<>(assetWrapper);
            var oldHandler = new AssetWrapperReflectionHandler<>(oldAsset);
            var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
            description.getFields().keySet().forEach(it -> {
                if (!JdbcUtils.isEquals(handler.getValue(it), oldHandler.getValue(it))) {
                    changedProperties.add(it);
                }
            });
            if (!changedProperties.isEmpty()) {
                var columnsValues = new LinkedHashMap<String, Pair<Object, SqlType>>();
                for(var it: changedProperties){
                    var fh = description.getFields().get(it);
                    columnsValues.putAll(fh.getSqlValues(handler.getValue(it), enumMapper, classMapper, reflectionFactory));
                }
                var query = "update %s set %s where id = ?".formatted(description.getName(),
                        StringUtils.join(columnsValues.keySet().stream().map("%s = ?"::formatted).toList(), ", "));
                template.update(query, (ps) -> {
                    int idx = 1;
                    for (Map.Entry<String, Pair<Object,SqlType>> entry : columnsValues.entrySet()) {
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
        insert(new AssetWrapperReflectionHandler<>(assetWrapper),
                JdbcUtils.getTableName(assetWrapper.getAsset().getClass().getName()));
    }


    @Override
    public <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, BlobWrapper data, VersionInfo vi) throws Exception {
        var handler = new VersionWrapperReflectionHandler(vi, data, id);
        insert(handler, JdbcUtils.getVersionTableName(aClass.getName()));
    }

    @Override
    public <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception {
        Set<String> properties = new LinkedHashSet<>();
        properties.add(BaseIdentity.Fields.id);
        properties.add(VersionInfo.Properties.revision);
        properties.add(VersionInfo.Properties.comment);
        properties.add(VersionInfo.Properties.modified);
        properties.add(VersionInfo.Properties.modifiedBy);
        properties.add(VersionInfo.Properties.versionNumber);
        properties.addAll(updateQuery.getPreferredFields());
        return searchObjects(cls, () -> new AssetWrapperReflectionHandler<>(cls.getDeclaredConstructor().newInstance()),
                updateQuery, properties).stream().map(it -> it.wrapper.getAsset()).toList();
    }

    @Override
    public List<VersionMetadata> getVersionsMetadata(Class<?> cls, long id) {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Properties.versionNumber.toLowerCase());
        fields.add(VersionInfo.Properties.comment.toLowerCase());
        fields.add(VersionInfo.Properties.modified.toLowerCase());
        fields.add(VersionInfo.Properties.modifiedBy.toLowerCase());
        var result = new ArrayList<VersionMetadata>();
        {
            var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(StringUtils.join(fields, ","),
                    JdbcUtils.getVersionTableName(cls.getName()), DatabaseMetadataProvider.OBJECT_ID_COLUMN);
            result.addAll(template.query(selectSql, (ps) -> {
                ps.setLong(1, id);
            }, (rs, idx) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionMetadataReflectionHandler();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object.metadata;
            })));
        }
        {
            var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
            var selectSql = "select %s from %s where %s = ?".formatted(StringUtils.join(fields, ","),
                    JdbcUtils.getTableName(cls.getName()), BaseIdentity.Fields.id);
            result.addAll(template.query(selectSql, (ps) -> {
                ps.setLong(1, id);
            }, (rs, idx) -> ExceptionUtils.wrapException(() -> {
                var object = new VersionMetadataReflectionHandler();
                fillObject(rs, object, descr, fields, Collections.emptySet());
                return object.metadata;
            })));
        }
        result.sort(Comparator.comparing(VersionMetadata::getVersionNumber));
        return result;
    }

    @Override
    public <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) throws Exception {
        template.update("delete from %s where id = ?".formatted(JdbcUtils.getTableName(aClass.getName())), (ps) -> {
            ps.setLong(1, id);
        });
    }

    @Override
    public VersionData loadVersion(Class<?> cls, long id, int number) throws Exception {
        var fields = new LinkedHashSet<String>();
        fields.add(VersionInfo.Properties.versionNumber.toLowerCase());
        fields.add(VersionInfo.Properties.comment.toLowerCase());
        fields.add(VersionInfo.Properties.modified.toLowerCase());
        fields.add(VersionInfo.Properties.modifiedBy.toLowerCase());
        fields.add(VersionData.Fields.data.toLowerCase());
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getVersionTableName(cls.getName()));
        var selectSql = "select %s from %s where %s = ? and %s = ?".formatted(StringUtils.join(fields, ","),
                JdbcUtils.getVersionTableName(cls.getName()), DatabaseMetadataProvider.OBJECT_ID_COLUMN,
                VersionInfo.Properties.versionNumber);
        var res = template.query(selectSql, (ps) -> {
            ps.setLong(1, id);
            ps.setInt(2, number);
        }, (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var object = new VersionDataReflectionHandler();
            fillObject(rs, object, descr, fields, Collections.emptySet());
            return object.data;
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
        dialect.deleteBlob(version.getData().id());
    }

    @Override
    public <A extends BaseAsset> A loadAsset(Class<A> cls, long id) throws Exception {
        var description = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        Objects.requireNonNull(description);
        var columnNames = getColumnNames(description, Collections.emptySet(), Collections.singleton(DatabaseMetadataProvider.AGGREGATED_DATA_COLUMN));
        var sql = "select %s from %s where %s = ?".formatted(StringUtils.join(columnNames, ", "), description.getName(), BaseIdentity.Fields.id);
        var result = template.query(sql, ps -> ps.setLong(1, id), rs -> {
            if(!rs.next()){
                return null;
            }
            return ExceptionUtils.wrapException(() -> {
                var asset = cls.getDeclaredConstructor().newInstance();
                var handler = new AssetWrapperReflectionHandler<>(asset);
                fillObject(rs, handler, description, Collections.emptySet(), Collections.singleton(DatabaseMetadataProvider.AGGREGATED_DATA_COLUMN));
                return handler;
            });
        });
        return result == null ? null : result.getWrapper().getAsset();
    }

    private void insert(BaseIntrospectableObject obj, String tableName) throws Exception {
        var description = dbMetadataProvider.getDescriptions().get(tableName);
        var properties = description.getFields().keySet();
        var columnsValues = new LinkedHashMap<String, Pair<Object,SqlType>>();
        for(var entry: description.getFields().entrySet()){
            columnsValues.putAll(entry.getValue().getSqlValues(obj.getValue(entry.getKey()), enumMapper, classMapper,reflectionFactory));
        }
        var query = "insert into %s (%s) values (%s)".formatted(description.getName(),
                StringUtils.join(columnsValues.keySet(), ", "),
                StringUtils.join(columnsValues.keySet().stream().map(it -> "?").toList(), ", "));
        template.update(query, (ps) -> {
            int idx = 1;
            for (Map.Entry<String, Pair<Object,SqlType>> entry : columnsValues.entrySet()) {
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
    public void updateCaptions(Class<?> aClass, long id, String caption, boolean insert) throws Exception {
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
    public void deleteCaptions(Class<?> aClass, long id) throws Exception {
        template.update("delete from %s where id = ?".formatted(JdbcUtils.getCaptionTableName(aClass.getName())), (ps) -> {
            ps.setLong(1, id);
        });
    }

    @Override
    public void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) throws Exception {
        if (insert) {
            template.update("insert into %s(id, %s) values (?, %s)".formatted(JdbcUtils.getCaptionTableName(aClass.getName()),
               StringUtils.join(captions.keySet().stream().map(it -> "%sname".formatted(it.getLanguage())), ", "),
                    StringUtils.join(captions.keySet().stream().map(it -> "?"), ", ")
            ), (ps) -> {
                ps.setLong(1, id);
                var cpts = captions.values().stream().toList();
                for(int n = 0; n < captions.size(); n++){
                    ps.setString(2+n, cpts.get(n));
                }
            });
            return;
        }
        template.update("update %s set %s where id = ?".formatted(
                JdbcUtils.getCaptionTableName(aClass.getName()),
                StringUtils.join(captions.keySet().stream().map(it -> "%sname = ?".formatted(it.getLanguage())), ", ")
        ), (ps) -> {
            var cpts = captions.values().stream().toList();
            for(int n = 0; n < captions.size(); n++){
                ps.setString(1+n, cpts.get(n));
            }
            ps.setLong(cpts.size(), id);
        });
    }

    private void setValue(PreparedStatement ps, int idx, SqlType key, Object value) {
        ExceptionUtils.wrapException(() -> {
            switch (key) {
                case LONG_ID, LONG -> ps.setLong(idx, (long) value);
                case LONG_ARRAY -> ps.getConnection().createArrayOf("bigint", ((Collection<Object>) value).toArray());
                case INT_ID, INT -> ps.setInt(idx, (int) value);
                case INT_ARRAY -> ps.getConnection().createArrayOf("integer", ((Collection<Object>) value).toArray());
                case STRING, TEXT -> ps.setString(idx, (String) value);
                case STRING_ARRAY -> ps.getConnection().createArrayOf("varchar", ((Collection<Object>) value).toArray());
                case BOOLEAN -> ps.setBoolean(idx, (boolean) value);
                case DATE -> ps.setDate(idx, (Date) value);
                case DATE_TIME -> ps.setTimestamp(idx, (Timestamp) value);
                case BIG_DECIMAL -> ps.setBigDecimal(idx, (BigDecimal) value);
                case BLOB -> dialect.setBlob(ps, idx, (BlobWrapper) value);
            }
            ;
        });

    }

    private int getSqlType(SqlType key) {
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

    private void fillObject(ResultSet rs, BaseIntrospectableObject handler, DatabaseTableDescription description,
                            Set<String> includedProperties, Set<String> excludedProperties) throws Exception {
        for (Map.Entry<String, DatabaseFieldHandler> entry : description.getFields().entrySet()) {
            if (!isIncluded(entry.getKey(), includedProperties, excludedProperties)) {
                continue;
            }
            handler.setValue(entry.getKey(), entry.getValue().getModelValue(rs, enumMapper, classMapper, reflectionFactory, dialect));
        }
    }

    private Set<String> getColumnNames(DatabaseTableDescription description, Set<String> includedProperties, Set<String> excludedProperties) {
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
                                                                       Set<String> properties) throws Exception {
        var descr = dbMetadataProvider.getDescriptions().get(JdbcUtils.getTableName(cls.getName()));
        var wherePart = prepareWherePart(query.getCriterions(), query.getFreeText(), descr);
        var joinPart = prepareJoinPart(query.getOrders(), cls);
        var orderPart = prepareOrderPart(query.getOrders(), cls);
        var limitPart = prepareLimitPart(query);
        var selectSql = "select %s from %s ".formatted(StringUtils.join(getColumnNames(descr, properties, Collections.emptySet()), ", ")
                , JdbcUtils.getTableName(cls.getName())) +
                joinPart +
                wherePart.sql +
                orderPart +
                limitPart;
        return template.query(selectSql, createPreparedStatementSetter(wherePart), (rs, idx) -> ExceptionUtils.wrapException(() -> {
            var object = factory.call();
            fillObject(rs, object, descr, properties, Collections.emptySet());
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
                                           DatabaseTableDescription descr) throws Exception {
        var criterions = new ArrayList<>(crits);
        if (!TextUtils.isBlank(freeTextPattern)) {
            for (var ptt : freeTextPattern.split(" ")) {
                if (!TextUtils.isBlank(ptt)) {
                    criterions.add(new SimpleCriterion(DatabaseMetadataProvider.AGGREGATED_DATA_COLUMN, SimpleCriterion.Operation.ILIKE, "%%%s%%".formatted(ptt.toLowerCase())));
                }
            }
        }
        if (criterions.isEmpty()) {
            return new WherePartData(Collections.emptyList(), "");
        }
        var values = new ArrayList<Pair<Object, SqlType>>();
        var sql = new StringBuilder();
        var indexOfSQL = new AtomicReference<>(0);
        prepareWherePartInternal(sql, values, indexOfSQL, criterions, descr);
        return new WherePartData(values, "where %s".formatted(sql));
    }

    private String makeAndToken(int currentSQLIndex) {
        return currentSQLIndex > 0 ? " and " : "";
    }

    private void prepareWherePartInternal(StringBuilder sql,
                                          List<Pair<Object, SqlType>> values, AtomicReference<Integer> indexOfSQL,
                                          List<SearchCriterion> criterions,
                                          DatabaseTableDescription descr) throws Exception {
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

    private void addNullCheckCriterion(StringBuilder sql, String property, boolean isNull, AtomicReference<Integer> indexOfSQL, DatabaseTableDescription descr) {
        var currentSQLIndex = indexOfSQL.get();
        var subQuery = "%s%s is %s".formatted(makeAndToken(currentSQLIndex), property, isNull ? "null" : "not null");
        sql.insert(currentSQLIndex, subQuery);
        indexOfSQL.set(currentSQLIndex + subQuery.length());
    }


    private void addCollectionCheckCriterion(StringBuilder sql,
                                             String property,
                                             String operation,
                                             AtomicReference<Integer> indexOfSql,
                                             DatabaseTableDescription descr) {
        var currentSQLIndex = indexOfSql.get();
        var subQuery = "%s%s %s 0".formatted(makeAndToken(currentSQLIndex), dialect.getCardinalitySql(property), operation);
        sql.insert(currentSQLIndex, subQuery);
        indexOfSql.set(currentSQLIndex + subQuery.length());
    }

    private void addSimpleCriterion(StringBuilder sql,
                                    List<Pair<Object,SqlType>> values,
                                    String property,
                                    String operation,
                                    Object value,
                                    AtomicReference<Integer> indexOfSql,
                                    DatabaseTableDescription descr) throws Exception {
        var currentSQLIndex = indexOfSql.get();
        var subQuery = "%s%s %s ?".formatted(makeAndToken(currentSQLIndex), property, operation);
        sql.insert(currentSQLIndex, subQuery);
        indexOfSql.set(currentSQLIndex + subQuery.length());
        values.add(getSqlQueryValue(value, descr, property));
    }

    private Pair<Object, SqlType> getSqlQueryValue(Object value, DatabaseTableDescription descr, String propertyName) throws Exception {
        return descr.getFields().get(propertyName).getSqlQueryValue(value, enumMapper, classMapper,reflectionFactory);
    }

    static class VersionDataReflectionHandler extends BaseIntrospectableObject {
        final VersionData data = new VersionData();

        @Override
        public void setValue(String propertyName, Object value) {
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                data.setComment((String) value);
                return;
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                data.setModified((LocalDateTime) value);
                return;
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                data.setModifiedBy((String) value);
                return;
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                data.setVersionNumber((int) value);
                return;
            }
            if (VersionData.Fields.data.equals(propertyName)) {
                data.setData((BlobWrapper) value);
                return;
            }
            super.setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                return data.getComment();
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                return data.getModified();
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                return data.getModifiedBy();
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                return data.getVersionNumber();
            }
            if (VersionData.Fields.data.equals(propertyName)) {
                return data.getData();
            }
            throw new IllegalArgumentException("unsuppoorted propertyName " + propertyName);
        }
    }


    static class AssetWrapperReflectionHandler<A extends BaseAsset> extends BaseIntrospectableObject {
        private final DatabaseAssetWrapper<A> wrapper;

        AssetWrapperReflectionHandler(DatabaseAssetWrapper<A> wrapper) {
            this.wrapper = wrapper;
        }

        AssetWrapperReflectionHandler(A asset) {
            this.wrapper = new DatabaseAssetWrapper<>();
            wrapper.setAsset(asset);
            wrapper.getAsset().setVersionInfo(new VersionInfo());
        }

        public DatabaseAssetWrapper<A> getWrapper() {
            return wrapper;
        }

        @Override
        public void setValue(String propertyName, Object value) {
            if (VersionInfo.Properties.revision.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setValue("revision", value);
                return;
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setModifiedBy((String) value);
                return;
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setModified((LocalDateTime) value);
                return;
            }
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setComment((String) value);
                return;
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                wrapper.getAsset().getVersionInfo().setVersionNumber((Integer) value);
                return;
            }
            if (DatabaseMetadataProvider.AGGREGATED_DATA_COLUMN.equals(propertyName)) {
                wrapper.setAggregatedData((String) value);
                return;
            }
            wrapper.getAsset().setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (VersionInfo.Properties.revision.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getRevision();
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getModifiedBy();
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getModified();
            }
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getComment();
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                return wrapper.getAsset().getVersionInfo().getVersionNumber();
            }
            if (DatabaseMetadataProvider.AGGREGATED_DATA_COLUMN.equals(propertyName)) {
                return wrapper.getAggregatedData();
            }
            return wrapper.getAsset().getValue(propertyName);
        }
    }

    static class VersionMetadataReflectionHandler extends BaseIntrospectableObject {
        final VersionMetadata metadata = new VersionMetadata();

        @Override
        public void setValue(String propertyName, Object value) {
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                metadata.setComment((String) value);
                return;
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                metadata.setModified((LocalDateTime) value);
                return;
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                metadata.setModifiedBy((String) value);
                return;
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                metadata.setVersionNumber((int) value);
                return;
            }
            super.setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (VersionInfo.Properties.comment.equals(propertyName)) {
                return metadata.getComment();
            }
            if (VersionInfo.Properties.modified.equals(propertyName)) {
                return metadata.getModified();
            }
            if (VersionInfo.Properties.modifiedBy.equals(propertyName)) {
                return metadata.getModifiedBy();
            }
            if (VersionInfo.Properties.versionNumber.equals(propertyName)) {
                return metadata.getVersionNumber();
            }
            throw new IllegalArgumentException("unsuppoorted propertyName " + propertyName);
        }
    }

    static class VersionWrapperReflectionHandler extends BaseIntrospectableObject {
        private final VersionInfo info;

        private BlobWrapper data;

        private long objectid;

        VersionWrapperReflectionHandler(VersionInfo info, BlobWrapper data, long objectid) {
            this.info = info;
            this.data = data;
            this.objectid = objectid;
        }

        @Override
        public void setValue(String propertyName, Object value) {
            if (DatabaseMetadataProvider.DATA_COLUMN.equals(propertyName)) {
                data = (BlobWrapper) value;
                return;
            }
            if (DatabaseMetadataProvider.OBJECT_ID_COLUMN.equals(propertyName)) {
                objectid = (long) value;
                return;
            }
            info.setValue(propertyName, value);
        }

        @Override
        public Object getValue(String propertyName) {
            if (DatabaseMetadataProvider.DATA_COLUMN.equals(propertyName)) {
                return data;
            }
            if (DatabaseMetadataProvider.OBJECT_ID_COLUMN.equals(propertyName)) {
                return objectid;
            }
            return info.getValue(propertyName);
        }
    }


    record WherePartData(List<Pair<Object, SqlType>> values, String sql) {
    }

}
