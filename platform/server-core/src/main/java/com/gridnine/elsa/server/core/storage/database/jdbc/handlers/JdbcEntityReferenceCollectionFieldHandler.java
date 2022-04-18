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
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.util.*;

public class JdbcEntityReferenceCollectionFieldHandler implements JdbcFieldHandler {

    private final String fieldName;

    private final boolean indexed;

    private final boolean isAbstract;

    private final boolean storeCaptions;

    private final String typeFieldName;

    private final String captionFieldName;

    private final  Class<?> cls;

    public JdbcEntityReferenceCollectionFieldHandler(String fieldName, boolean indexed, Class<?> cls, boolean isAbstract, boolean storeCaptions) {
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
        result.put(fieldName, JdbcFieldType.LONG_ARRAY);
        if(isAbstract){
            result.put(typeFieldName, JdbcFieldType.INT_ARRAY);
        }
        if(storeCaptions){
            result.put(captionFieldName, JdbcFieldType.STRING_ARRAY);
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
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect
            dialiect) throws Exception {
        var idJdbcValues = rs.getArray(fieldName);
        if(idJdbcValues == null){
            return Collections.emptyList();
        }
        var idValues = Arrays.stream((Object[]) idJdbcValues.getArray()).map(it -> (Long) it)
                .toList();
        var typeValues = new ArrayList<Class<?>>();
        if(isAbstract){
            var typeJdbcValues = rs.getArray(typeFieldName);
            Arrays.stream((Object[]) typeJdbcValues.getArray()).map(it -> (Integer) it)
                    .forEach((it)-> typeValues.add(factory.getClass(classMapper.getName(it))));
        } else {
            for(int n = 0; n < idValues.size(); n++){
                typeValues.add(cls);
            }
        }
        var captionsValues = new ArrayList<String>();
        if(storeCaptions){
            var captionsJdbcValues = rs.getArray(captionFieldName);
            captionsValues.addAll( Arrays.stream((Object[]) idJdbcValues.getArray()).map(it -> (String) it).toList());
        } else {
            for(int n = 0; n < idValues.size(); n++){
                captionsValues.add(null);
            }
        }
        var result = new ArrayList<EntityReference<?>>();
        for(int n = 0; n < idValues.size(); n++){
            result.add(new EntityReference<>(idValues.get(n), (Class<BaseIdentity>) typeValues.get(n), captionsValues.get(n)));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Pair<Object, JdbcFieldType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        var result = new LinkedHashMap<String, Pair<Object, JdbcFieldType>>();
        var ref = (List<EntityReference<?>>) value;
        result.put(fieldName, Pair.of(ref.stream().map(EntityReference::getId).toList(), JdbcFieldType.LONG_ARRAY));
        if(isAbstract){
            result.put(typeFieldName, Pair.of(ref.stream().map(it -> classMapper.getId(it.getType().getName())).toList(), JdbcFieldType.INT_ARRAY));
        }
        if(storeCaptions){
            result.put(typeFieldName, Pair.of(ref.stream().map(EntityReference::getCaption).toList(), JdbcFieldType.STRING_ARRAY));
        }
        return result;
    }

    @Override
    public Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value == null ? null : ((EntityReference<?>) value).getId(), JdbcFieldType.LONG);
    }

}
