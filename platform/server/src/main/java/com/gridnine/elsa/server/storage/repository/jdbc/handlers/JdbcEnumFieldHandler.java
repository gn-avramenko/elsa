/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.EnumMapper;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class JdbcEnumFieldHandler extends BaseJdbcSingleFieldHandler {

    private final Class<Enum<?>> enumClass;

    public JdbcEnumFieldHandler(String entityId, String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlTypeIntHandler.type);
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(entityId);
        PropertyDescription pd = ed.getProperties().get(fieldName);
        DatabaseTagDescription dtd = DomainTypesRegistry.get().getDatabaseTags().get(pd.getTagName());
        String classNameAttribute = dtd.getObjectIdAttributeName();
        String className = pd.getAttributes().get(classNameAttribute);
        this.enumClass = ReflectionFactory.get().getClass(className);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : ReflectionFactory.get().safeGetEnum(enumClass, EnumMapper.get().getName(rs.getInt(fieldName), enumClass));
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) {
        return Collections.singletonMap(fieldName, new Pair<>(value == null? null : EnumMapper.get().getId((Enum<?>) value), SqlTypeIntHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) {
        return new Pair<>(value == null? null : EnumMapper.get().getId((Enum<?>) value), SqlTypeIntHandler.type);
    }
}
