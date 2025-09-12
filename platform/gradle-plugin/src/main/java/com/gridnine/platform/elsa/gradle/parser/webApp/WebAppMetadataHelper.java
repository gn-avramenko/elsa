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

import com.gridnine.platform.elsa.gradle.meta.common.StandardPropertyDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.webApp.BaseWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.CustomWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebAppEntity;

import java.util.ArrayList;

public class WebAppMetadataHelper {
    public static CustomWebElementDescription extendWithStandardProperties(BaseWebElementDescription element){
        var commandsFromServer = new ArrayList<>(element.getCommandsFromServer());
        var commandsFromClient = new ArrayList<>(element.getCommandsFromClient());
        var state = new WebAppEntity();
        state.getProperties().putAll(element.getServerManagedState().getProperties());
        state.getCollections().putAll(element.getServerManagedState().getCollections());
        switch (element.getType()){
            case MODAL -> {
            }
            case CONTAINER -> {
                var prop = new StandardPropertyDescription();
                prop.setType(StandardValueType.ENUM);
                prop.setNonNullable(true);
                prop.setId("flexDirection");
                prop.setClassName("com.gridnine.platform.elsa.webApp.FlexDirection");
                state.getProperties().put(prop.getId(), prop);
            }
            case BUTTON -> {
            }
            case SELECT -> {
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
        var result = new CustomWebElementDescription(element.getId(), element.getClassName());
        result.getServerManagedState().getProperties().putAll(state.getProperties());
        result.getServerManagedState().getCollections().putAll(state.getCollections());

        return result;

    }
}
