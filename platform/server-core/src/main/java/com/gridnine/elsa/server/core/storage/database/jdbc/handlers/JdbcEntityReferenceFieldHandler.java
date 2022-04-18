/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.handlers;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcEntityReferenceFieldHandler implements JdbcFieldHandler {


    private final String fieldName;

    private final boolean indexed;

    private final boolean isAbstract;

    private final boolean storeCaptions;

    private final Class<?> cls;

    private final String typeFieldName;

    private final String captionFieldName;
    public JdbcEntityReferenceFieldHandler(String fieldName, boolean indexed, Class<?> cls, boolean isAbstract, boolean storeCaptions) {
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.isAbstract = isAbstract;
        this.storeCaptions = storeCaptions;
        this.typeFieldName = "%stype".formatted(fieldName);
        this.captionFieldName = "%scaption".formatted(fieldName);
        this.cls = cls;
    }

    @Override
    public Map<String, JdbcFieldType> getColumns() {
        final var result = new LinkedHashMap<String, JdbcFieldType>();
        result.put(fieldName, JdbcFieldType.LONG);
        if(isAbstract){
            result.put(typeFieldName, JdbcFieldType.INT);
        }
        if(storeCaptions){
            result.put(captionFieldName, JdbcFieldType.STRING);
        }
        return result;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        if(!indexed){
            return Collections.emptyMap();
        }
        return Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new JdbcIndexDescription(fieldName, JdbcIndexType.BTREE));
    }

    @SuppressWarnings("unchecked")
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
    public Map<String, Pair<Object, JdbcFieldType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        var result = new LinkedHashMap<String, Pair<Object, JdbcFieldType>>();
        var ref = (EntityReference<?>) value;
        result.put(fieldName, Pair.of(ref == null ? null : ref.getId(), JdbcFieldType.LONG));
        if(isAbstract){
            result.put(typeFieldName, Pair.of(ref == null ? null : classMapper.getId(ref.getType().getName()), JdbcFieldType.INT));
        }
        if(storeCaptions){
            result.put(captionFieldName, Pair.of(ref == null ? null : ref.getCaption(), JdbcFieldType.STRING));
        }

        return result;
    }

    @Override
    public Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value == null? null: ((EntityReference<?>) value).getId(), JdbcFieldType.LONG);
    }

}
