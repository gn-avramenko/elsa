/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.EnumMapper;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JdbcEnumCollectionFieldHandler implements JdbcFieldHandler {

    private final String fieldName;
    private final boolean indexed;
    private final Class<Enum<?>> enumClass;

    public JdbcEnumCollectionFieldHandler(String entityId, String fieldName, boolean indexed){
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(entityId);
        PropertyDescription pd = ed.getProperties().get(fieldName);
        DatabaseTagDescription dtd = DomainTypesRegistry.get().getDatabaseTags().get(pd.getTagName());
        String classNameAttribute = dtd.getGenerics().get(0).getObjectIdAttributeName();
        String className = pd.getAttributes().get(classNameAttribute);
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.enumClass = ReflectionFactory.get().getClass(className);
    }

    @Override
    public Map<String, String> getColumns() {
        return Collections.singletonMap(fieldName, SqlTypeIntArrayHandler.type) ;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new JdbcIndexDescription(fieldName, JdbcIndexType.BTREE)) :
                Collections.emptyMap();
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        var jdbcValue = rs.getArray(fieldName);
        if(jdbcValue == null){
            return Collections.emptyList();
        }
        return Arrays.stream((Object[]) jdbcValue.getArray()).map(it -> (Integer) it).map(it -> ReflectionFactory.get().safeGetEnum(enumClass, EnumMapper.get().getName(it, enumClass))).toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(((List<Enum<?>>) value).stream().map(it -> EnumMapper.get().getId(it)).toList(), SqlTypeIntArrayHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(EnumMapper.get().getId((Enum<?>) value), SqlTypeIntHandler.type);
    }

    @Override
    public void setValue(BaseIntrospectableObject obj, String propertyName, Object value) {
        Collection<Object> coll = (Collection<Object>) obj.getValue(propertyName);
        coll.clear();
        if(value != null) {
            coll.addAll((Collection<Object>) value);
        }
    }

}
