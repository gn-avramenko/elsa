/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.gridnine.elsa.common.serialization.metadata.PropertySerializationMetadata;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.serialization.handlers.EntitySerializationHandler;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsonUnmarshaller {

    private final JsonFactory jsonFactory = new JsonFactory();

    public <T> T unmarshal(Class<T> cls, InputStream is, Map<String, Object> params) {
        return unmarshal(cls.getName(), is, params);

    }
    public <T> T unmarshal(String className, InputStream is, Map<String, Object> params) {
        return ExceptionUtils.wrapException(() -> {
            try (var parser = jsonFactory.createParser(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return unmarshal(parser, className, params);
            }
        });
    }

    private <T> T unmarshal(JsonParser parser, String className, Map<String, Object> params) throws Exception {
        var pd = new PropertyDescription();
        pd.getAttributes().put("class-name", className);
        var tg = new TagDescription();
        tg.setType("ENTITY");
        tg.setObjectIdAttributeName("class-name");
        var md = new PropertySerializationMetadata(pd, "ENTITY", null, tg, SerializationHandlersRegistry.get().getHandler("ENTITY"));
        return (T) EntitySerializationHandler.deserialize0(parser, md, params, new HashMap<>(), null);
    }

    public static JsonUnmarshaller get(){
        return Environment.getPublished(JsonUnmarshaller.class);
    }
}
