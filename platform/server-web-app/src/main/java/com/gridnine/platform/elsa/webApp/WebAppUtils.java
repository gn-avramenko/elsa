/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.webApp;

import com.google.gson.*;
import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.meta.common.StandardValueType;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.utils.GsonSerializable;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class WebAppUtils {
    public static JsonElement toJsonValue(List<?> value, StandardValueType elementType) {
        if(value == null || value.isEmpty()) {
            return JsonNull.INSTANCE;
        }
        var result= new JsonArray();
        value.forEach(item->{
            result.add(toJsonValue(item, elementType));
        });
        return result;
    }
    public static JsonElement toJsonValue(Map<?, ?> value, StandardValueType valueType) {
        if(value == null || value.isEmpty()) {
            return JsonNull.INSTANCE;
        }
        var result= new JsonObject();
        value.forEach((k,v)->{
            result.add(k.toString(), toJsonValue(v, valueType));
        });
        return result;
    }
    public static JsonElement toJsonValue(Object value, StandardValueType type) {
        if(value == null){
            return JsonNull.INSTANCE;
        }
        switch (type){
            case STRING:
                return new JsonPrimitive((String) value);
            case LOCAL_DATE:
                return new JsonPrimitive(((LocalDate) value).toString());
            case LOCAL_DATE_TIME:
                return new JsonPrimitive(((LocalDateTime) value).toString());
            case ENUM:
                return new JsonPrimitive(((Enum<?>) value).name());
            case CLASS:
                return new JsonPrimitive(((Class<?>) value).getName());
            case BOOLEAN:
                return new JsonPrimitive((Boolean) value);
            case BYTE_ARRAY:
                throw Xeption.forDeveloper("unsupported type %s".formatted(type));
            case ENTITY:
                return WebPeerUtils.wrapException(()-> ((GsonSerializable) value).serialize());
            case LONG:
                return new JsonPrimitive((Long) value);
            case INT:
                return new JsonPrimitive((Integer) value);
            case BIG_DECIMAL:
                return new JsonPrimitive((BigDecimal) value);
            case UUID:
                return new JsonPrimitive(((UUID) value).toString());
            case INSTANT:
                return new JsonPrimitive(((Instant) value).toString());
            case ENTITY_REFERENCE: {
                var obj = new JsonObject();
                var val = (EntityReference<?>) value;
                obj.add("id", new JsonPrimitive(val.getId().toString()));
                obj.add("class", new JsonPrimitive(val.getClass().getName()));
                obj.add("caption", new JsonPrimitive(val.getCaption()));
                return obj;
            }
        }
        throw Xeption.forDeveloper("unsupported type %s".formatted(type));
    }
    public static<T> List<T> fromJsonArrayValue(JsonElement value, StandardValueType type,  Class<T> cls) throws Exception {
        if(value == null || value.isJsonNull()){
            return List.of();
        }
        var arr = value.getAsJsonArray();
        var result = new ArrayList<T>();
        for(var elm: arr){
            var item = fromJsonValue(elm, type, cls);
            result.add(item);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static<T> T fromJsonValue(JsonElement value, StandardValueType type, Class<T> cls) throws Exception {
        if(value == null || value.isJsonNull()){
            return null;
        }
        switch (type){
            case STRING:
                return (T) value.getAsString();
            case LOCAL_DATE:
                return (T) LocalDate.parse(value.getAsString());
            case LOCAL_DATE_TIME:
                return (T) LocalDateTime.parse(value.getAsString());
            case ENUM: {
                var enumName =  value.getAsString();
                return (T) Arrays.stream(((Class<Enum<?>>) cls).getEnumConstants()).filter(it -> it.name().equals(enumName)).findFirst().orElse(null);
            }
            case CLASS:
                return (T)Class.forName(value.getAsString());
            case BOOLEAN:
                return (T) Boolean.valueOf(value.getAsBoolean());
            case BYTE_ARRAY:
                throw Xeption.forDeveloper("unsupported type %s".formatted(type));
            case ENTITY: {
                var  res = (GsonDeserializable) cls.getConstructor().newInstance();
                res.deserialize(value);
                return (T) res;
            }
            case LONG:
                return (T) Long.valueOf(value.getAsLong());
            case INT:
                return (T) Integer.valueOf(value.getAsInt());
            case BIG_DECIMAL:
               return  (T) value.getAsBigDecimal();
            case UUID:
                return (T) UUID.fromString(value.getAsString());
            case INSTANT:
                return (T)  Instant.parse(value.getAsString());
            case ENTITY_REFERENCE: {
                var obj = value.getAsJsonObject();
                return (T) new EntityReference<BaseIdentity>(obj.get("id").getAsString(), (Class) Class.forName(obj.get("class").getAsString()), obj.get("caption").getAsString());
            }
        }
        throw Xeption.forDeveloper("unsupported type %s".formatted(type));
    }

    public static JsonElement findStateOfChild(JsonElement uiData, String tag){
        if(uiData == null || !uiData.isJsonObject() || uiData.getAsJsonObject().isEmpty()){
            return null;
        }
        return uiData.getAsJsonObject().getAsJsonArray("children").asList().stream().filter(it ->
                it.isJsonObject() && it.getAsJsonObject().has("tag") && it.getAsJsonObject().get("tag").getAsString().equals(tag)
        ).findFirst().orElse(null);
    }

    public static BaseUiElement findChildByTag(BaseUiElement element, String tag){
        return  element.getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals(tag)).findFirst().orElse(null);
    }

    public static boolean equals(Object a, Object b) {
        if(a instanceof List<?> lst){
            if(!(b instanceof List<?>)){
                return false;
            }
            var lst1 = (List<?>) a;
            var lst2 = (List<?>) b;
            if(lst1.size() != lst2.size()){
                return false;
            }
            for(int i = 0; i < lst1.size(); i++){
                if(!Objects.equals(lst1.get(i), lst2.get(i))){
                    return false;
                }
            }
            return true;
        }
        return Objects.equals(a, b);
    }
}
