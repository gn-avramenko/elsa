/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.CaptionProvider;
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
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

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
    public JdbcEntityReferenceFieldHandler(String entityId, String fieldName, boolean indexed) {
        EntityDescription ed = SerializableMetaRegistry.get().getEntities().get(entityId);
        String className = null;
        EntityDescription referencedEntity =null;
        if("document".equals(fieldName)) {
            className = ed.getAttributes().get("document");
        }else {
            PropertyDescription pd = ed.getProperties().get(fieldName);
            DatabaseTagDescription dtd = DomainTypesRegistry.get().getDatabaseTags().get(pd.getTagName());
            String classNameAttribute = dtd.getGenerics().get(0).getObjectIdAttributeName();
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
        result.put(fieldName, SqlTypeLongHandler.type);
        if(isAbstract){
            result.put(typeFieldName, SqlTypeIntHandler.type);
        }
        if(storeCaptions){
            result.put(captionFieldName, SqlTypeStringHandler.type);
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
    public Object getModelValue(ResultSet rs) throws SQLException {
        if(JdbcUtils.isNull(rs, fieldName)){
            return null;
        }
        var id = rs.getLong(fieldName);
        var cls = isAbstract? ReflectionFactory.get().getClass(ClassMapper.get().getName(rs.getInt(typeFieldName))): this.cls;
        var caption = storeCaptions? rs.getString(captionFieldName): null;
        var result = new EntityReference<>(id, (Class<BaseIdentity>) cls, caption);
        if(!storeCaptions){
            result.setCaption(CaptionProvider.get().getCaption(result));
        }
        return result;
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) {
        var result = new LinkedHashMap<String, Pair<Object, String>>();
        var ref = (EntityReference<?>) value;
        result.put(fieldName, new Pair<>(ref == null ? null : ref.getId(), SqlTypeLongHandler.type));
        if(isAbstract){
            result.put(typeFieldName, new Pair<>(ref == null ? null : ClassMapper.get().getId(ref.getType().getName()), SqlTypeIntHandler.type));
        }
        if(storeCaptions){
            result.put(captionFieldName, new Pair<>(ref == null ? null : ref.getCaption(), SqlTypeStringHandler.type));
        }

        return result;
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value == null? null: ((EntityReference<?>) value).getId(), SqlTypeLongHandler.type);
    }

}
