/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.ClassMapper;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.model.domain.EntityReference;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcEntityReferenceCollectionFieldHandler implements JdbcFieldHandler {


    private final String fieldName;

    private final boolean indexed;

    private final boolean isAbstract;

    private final boolean storeCaptions;

    private final Class<?> cls;

    private final String typeFieldName;

    private final String captionFieldName;

    public JdbcEntityReferenceCollectionFieldHandler(String entityId, String fieldName, boolean indexed) {
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(entityId);
        String className = null;
        EntityDescription referencedEntity =null;
        if("document".equals(fieldName)) {
            className = ed.getAttributes().get("document");
        }else {
            PropertyDescription pd = ed.getProperties().get(fieldName);
            DatabaseTagDescription dtd = DomainTypesRegistry.get().getDatabaseTags().get(pd.getTagName());
            String classNameAttribute = dtd.getGenerics().get(0).getNestedGenerics().get(0).getObjectIdAttributeName();
            className = pd.getAttributes().get(classNameAttribute);
        }
        referencedEntity = SerializableMetaRegistry.get().getEntities().get(className);
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.isAbstract = "true".equals(referencedEntity.getAttributes().get("abstract"));
        this.storeCaptions = !"true".equals(referencedEntity.getAttributes().get("cache-caption"));
        this.typeFieldName = "%stype".formatted(fieldName);
        this.captionFieldName = "%scaption".formatted(fieldName);
        this.cls = ReflectionFactory.get().getClass(className);
    }

    @Override
    public Map<String, String> getColumns() {
        final var result = new LinkedHashMap<String, String>();
        result.put(fieldName, SqlTypeLongArrayHandler.type);
        if(isAbstract){
            result.put(typeFieldName, SqlTypeIntArrayHandler.type);
        }
        if(storeCaptions){
            result.put(captionFieldName, SqlTypeStringArrayHandler.type);
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
    public Object getModelValue(ResultSet rs) throws Exception {
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
                    .forEach((it)-> typeValues.add(ReflectionFactory.get().getClass(ClassMapper.get().getName(it))));
        } else {
            for(int n = 0; n < idValues.size(); n++){
                typeValues.add(cls);
            }
        }
        var captionsValues = new ArrayList<String>();
        if(storeCaptions){
            var captionsJdbcValues = rs.getArray(captionFieldName);
            captionsValues.addAll( Arrays.stream((Object[]) captionsJdbcValues.getArray()).map(it -> (String) it).toList());
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
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        var result = new LinkedHashMap<String, Pair<Object, String>>();
        var ref = (List<EntityReference<?>>) value;
        result.put(fieldName, new Pair<>(ref.stream().map(EntityReference::getId).toList(), SqlTypeLongArrayHandler.type));
        if(isAbstract){
            result.put(typeFieldName, new Pair<>(ref.stream().map(it -> ClassMapper.get().getId(it.getType().getName())).toList(), SqlTypeIntArrayHandler.type));
        }
        if(storeCaptions){
            result.put(captionFieldName, new Pair<>(ref.stream().map(EntityReference::getCaption).toList(), SqlTypeStringArrayHandler.type));
        }
        return result;
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value == null ? null : ((EntityReference<?>) value).getId(), SqlTypeLongHandler.type);
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
