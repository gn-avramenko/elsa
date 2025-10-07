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

package com.gridnine.platform.elsa.gradle.parser.webApp;

import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.meta.common.EntityDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardCollectionDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardPropertyDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.webApp.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WebAppMetadataHelper {
    public static CustomWebElementDescription toCustomEntity(BaseWebElementDescription element) {
        var result = new CustomWebElementDescription(element.getClassName());
        result.getServerManagedState().getProperties().putAll(element.getServerManagedState().getProperties());
        result.getServerManagedState().getCollections().putAll(element.getServerManagedState().getCollections());
        result.getCommandsFromClient().addAll(element.getCommandsFromClient());
        switch (element.getType()) {
            case MODAL -> {
            }
            case CONTAINER -> {
                var prop = new StandardPropertyDescription();
                prop.setType(StandardValueType.ENUM);
                prop.setNonNullable(true);
                prop.setId("flexDirection");
                prop.setClassName("com.gridnine.platform.elsa.webApp.common.FlexDirection");
                result.getServerManagedState().getProperties().put(prop.getId(), prop);
            }
            case BUTTON -> {
                ButtonWebElementDescription bb = (ButtonWebElementDescription) element;
                {
                    var command = new WebElementCommandDescription();
                    command.setId("click");
                    result.getCommandsFromClient().add(command);
                }
                {
                    var prop = new StandardPropertyDescription();
                    prop.setType(StandardValueType.STRING);
                    prop.setId("title");
                    result.getServerManagedState().getProperties().put(prop.getId(), prop);
                }
            }
            case SELECT -> {
                SelectWebElementDescription sd = (SelectWebElementDescription) element;
                {
                    var input = new InputDescription();
                    result.setInput(input);
                    input.setType(InputType.SELECT);
                    var value = new WebAppEntity();
                    input.setValue(value);
                    var prop = new StandardCollectionDescription();
                    prop.setId("values");
                    prop.setElementType(StandardValueType.STRING);
                    value.getCollections().put(prop.getId(), prop);
                }
                {
                    var options = new StandardCollectionDescription();
                    options.setElementType(StandardValueType.ENTITY);
                    options.setId("options");
                    options.setElementClassName("com.gridnine.platform.elsa.webApp.common.Option");
                    result.getServerManagedState().getCollections().put(options.getId(), options);
                }
                {
                    var multiple = new StandardPropertyDescription();
                    multiple.setType(StandardValueType.BOOLEAN);
                    multiple.setId("multiple");
                    result.getServerManagedState().getProperties().put(multiple.getId(), multiple);
                }
                addDeferred(sd, result);
            }
            case ROUTER -> {
            }
            case TEXT_AREA -> {
            }
            case TEXT_FIELD -> {
            }
            case TABLE -> {
            }
            case AUTOCOMPLETE -> {
            }
            case LABEL -> {
            }
            case CUSTOM -> {
            }
        }
        return result;
    }

    private static void addDeferred(BaseWebElementDescription sd, CustomWebElementDescription ce) {
        var prop = new StandardPropertyDescription();
        prop.setId("deferred");
        prop.setType(StandardValueType.BOOLEAN);
        prop.setNonNullable(false);
        ce.getServerManagedState().getProperties().put(prop.getId(), prop);
    }
    public static EntityDescription getInputValueDescription(BaseWebElementDescription element) {
        WebAppEntity value = element.getInput().getValue();
        var cn = "%sInputValue".formatted(element.getClassName());
        var ed = new EntityDescription(cn);
        ed.getProperties().putAll(value.getProperties());
        ed.getCollections().putAll(value.getCollections());
        return ed;
    }
    public static EntityDescription getConfigurationDescription(BaseWebElementDescription element) {
        var ce = toCustomEntity(element);
        var cn = "%sConfiguration".formatted(ce.getClassName());
        var ed = new EntityDescription(cn);
        ed.getParameters().put("no-serialization", "true");
        ed.getProperties().putAll(ce.getServerManagedState().getProperties());
        ed.getCollections().putAll(ce.getServerManagedState().getCollections());
        for(var cmd: ce.getCommandsFromClient()){
            var prop = new StandardPropertyDescription();
            prop.setType(StandardValueType.ENTITY);
            prop.setId("%sListener".formatted(cmd.getId()));
            if(cmd.getProperties().isEmpty() && cmd.getCollections().isEmpty()){
                prop.setClassName("com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAndArgument<com.gridnine.webpeer.core.ui.OperationUiContext>");
            }
            ed.getProperties().put(prop.getId(), prop);
        }
        if(ce.getInput() != null){
            var inputValueClass = "%sInputValue".formatted(ce.getClassName());
            {
                var prop = new StandardPropertyDescription();
                prop.setType(StandardValueType.ENTITY);
                prop.setId("valueChangeListener");
                prop.setClassName("com.gridnine.platform.elsa.webApp.WebAppValueChangeListener<%s>".formatted(inputValueClass));
                ed.getProperties().put(prop.getId(), prop);
            }
            {
                var prop = new StandardPropertyDescription();
                prop.setType(StandardValueType.ENTITY);
                prop.setId("value");
                prop.setClassName(inputValueClass);
                ed.getProperties().put(prop.getId(), prop);
            }
        }
        if(element instanceof WebElementWithChildren hasChildren){
            for(var entry: hasChildren.getChildren().entrySet()){
                var child = entry.getValue();
                boolean managed = isManagedConfiguration(child);
                if(managed){
                    var prop = new StandardPropertyDescription();
                    prop.setType(StandardValueType.ENTITY);
                    prop.setId(entry.getKey());
                    prop.setClassName("%sConfiguration".formatted(child.getClassName()));
                    ed.getProperties().put(prop.getId(), prop);
                }
            }
        }
        return ed;
    }



    public static boolean isManagedConfiguration(BaseWebElementDescription child) {
        return switch (child.getType()) {
            case CONTAINER -> {
                var ctr = (ContainerWebElementDescription)child;
                yield ctr.isManagedConfiguration();
            }
            default -> true;
        };
    }

    public static String getPropertyType(StandardValueType type, String className) {
        return switch (type) {
            case STRING, CLASS -> "String";
            case LOCAL_DATE -> {
                yield LocalDate.class.getName();
            }
            case LOCAL_DATE_TIME -> {
                yield LocalDateTime.class.getName();
            }
            case INSTANT -> {
                yield Instant.class.getName();
            }
            case BOOLEAN -> "Boolean";
            case ENUM, ENTITY -> {
                yield className;
            }
            case LONG -> "Long";
            case INT -> "Integer";
            case BIG_DECIMAL -> {
                yield BigDecimal.class.getName();
            }
            default -> throw new IllegalStateException("Unknown property type: " + type);
        };
    }

}
