/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.server.core.storage.jdbc.JdbcDialect;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityReferenceDatabaseFieldHandler  implements DatabaseFieldHandler{


    private final String fieldName;

    private final boolean indexed;

    private final boolean isAbstract;

    private final boolean storeCaptions;

    private final Class<?> cls;

    private final String typeFieldName;

    private final String captionFieldName;
    public EntityReferenceDatabaseFieldHandler(String fieldName, boolean indexed,  Class<?> cls, boolean isAbstract, boolean storeCaptions) {
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.isAbstract = isAbstract;
        this.storeCaptions = storeCaptions;
        this.typeFieldName = "%stype".formatted(fieldName);
        this.captionFieldName = "%scaption".formatted(fieldName);
        this.cls = cls;
    }

    @Override
    public Map<String, SqlType> getColumns() {
        final var result = new LinkedHashMap<String,SqlType>();
        result.put(fieldName, SqlType.LONG);
        if(isAbstract){
            result.put(typeFieldName, SqlType.INT);
        }
        if(storeCaptions){
            result.put(captionFieldName, SqlType.STRING);
        }
        return result;
    }

    @Override
    public Map<String, IndexDescription> getIndexes(String tableName) {
        if(!indexed){
            return Collections.emptyMap();
        }
        return Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new IndexDescription(fieldName, IndexType.BTREE));
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws SQLException {
        if(JdbcUtils.isNull(rs, fieldName)){
            return null;
        }
        var id = rs.getLong(fieldName);
        var cls = isAbstract? factory.getClass(classMapper.getName(rs.getInt(typeFieldName))): this.cls;
        var caption = storeCaptions? rs.getString(captionFieldName): null;
        return new EntityReference<>(id, (Class<BaseIdentity>) cls, caption);
    }

    @Override
    public Map<String, Pair<Object, SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        var result = new LinkedHashMap<String, Pair<Object, SqlType>>();
        var ref = (EntityReference<?>) value;
        result.put(fieldName, Pair.of(ref == null ? null : ref.getId(),SqlType.LONG));
        if(isAbstract){
            result.put(typeFieldName, Pair.of(ref == null ? null : classMapper.getId(ref.getType().getName()),SqlType.INT));
        }
        if(storeCaptions){
            result.put(captionFieldName, Pair.of(ref == null ? null : ref.getCaption(),SqlType.STRING));
        }

        return result;
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value == null? null: ((EntityReference<?>) value).getId(), SqlType.LONG);
    }

}
