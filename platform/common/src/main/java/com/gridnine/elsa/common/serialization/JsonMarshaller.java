/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.serialization.handlers.EntitySerializationHandler;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonMarshaller {

    final static String INTERNAL_ID_PROPERTY="_id";

    final static String CLASS_NAME_PROPERTY = "_cn";

    private final JsonFactory jsonFactory = new JsonFactory();

    public <T extends BaseIntrospectableObject> String marshalToString(T obj, boolean isAbstract, Map<String, Object> params) {
        var baos = new ByteArrayOutputStream();
        marshal(obj, baos, isAbstract, params);
        return ExceptionUtils.wrapException(() -> baos.toString(StandardCharsets.UTF_8));
    }
    public <T extends BaseIntrospectableObject> void marshal(T obj, OutputStream os, boolean isAbstract, Map<String, Object> params) {
        ExceptionUtils.wrapException(() -> {
            try (var generator = jsonFactory.createGenerator(os, JsonEncoding.UTF8)) {
                if (StandardSerializationParameters.isPrettyPrint(params)) {
                    generator.setPrettyPrinter(new DefaultPrettyPrinter());
                }
                var counter = new AtomicInteger(0);
                Set<Object> duplicatingEntities = new HashSet<>();
                collectDuplicatingEntities(obj, duplicatingEntities, new HashSet<>());
                EntitySerializationHandler.serialize(obj, isAbstract, generator, duplicatingEntities, new HashMap<>(), counter, params);
            }
        });
    }

    private <T extends BaseIntrospectableObject> void collectDuplicatingEntities(T obj, Set<Object> duplicatingEntities,Set<Object> processed ) {
       EntitySerializationHandler.processDuplicatingEntities0(obj, Collections.emptyMap(), duplicatingEntities, processed);
    }

    public static JsonMarshaller get(){
        return Environment.getPublished(JsonMarshaller.class);
    }
}
