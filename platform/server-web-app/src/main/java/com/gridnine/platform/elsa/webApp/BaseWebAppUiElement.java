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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseWebAppUiElement extends BaseUiElement {

    private final Map<String, Object> initParams = new HashMap<>();

    private final Map<String, Object> state = new HashMap<>();

    private boolean initialized;

    public BaseWebAppUiElement(String type, String tag, OperationUiContext ctx) {
        super(type, tag, ctx);
    }

    protected void setInitParam(String key, Object value) {
        initParams.put(key, value);
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if(commandId.equals("pc")) {
            var obj =data.getAsJsonObject();
            var pn = WebPeerUtils.getString(obj, "pn");
            var pv = WebPeerUtils.getDynamic(obj, "pv");
            state.put(pn, WebPeerUtils.getValue(pv));
            return;
        }
        super.processCommand(ctx, commandId, data);
    }

    protected void setProperty(String propertyName, Object value, OperationUiContext context) {
        state.put(propertyName, value);
        if(initialized){
            var data = new JsonObject();
            WebPeerUtils.addProperty(data, "pn", propertyName);
            WebPeerUtils.addProperty(data, "pv", value);
            sendCommand(context, "pc", data);
        }
    }

    protected<T> T getProperty(String propertyName, Class<T> type) {
        //noinspection unchecked
        return (T) state.get(propertyName);
    }

    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        if(state == null){
            return;
        }
        var obj = state.getAsJsonObject();
        new ArrayList<>(this.state.keySet()).forEach(key -> this.state.put(key,WebPeerUtils.getValue(obj.get(key))));
        if(!obj.has("children")){
            return;
        }
        obj.getAsJsonArray("children").forEach(child -> {
            var obj2 = child.getAsJsonObject();
            var ch = getUnmodifiableListOfChildren().stream().filter(it -> it.getTag() != null && it.getTag().equals(WebPeerUtils.getString(obj2, "tag"))).findFirst().orElse(null);
            if(ch != null){
                ch.restoreFromState(child, ctx);
            }
        });
    }


    @Override
    public JsonObject buildState(OperationUiContext context) {
        var result =  super.buildState(context);
        for(Map.Entry<String, Object> entry : initParams.entrySet()){
            WebPeerUtils.addProperty(result, entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Object> entry : state.entrySet()){
            WebPeerUtils.addProperty(result, entry.getKey(), entry.getValue());
        }
        initialized = true;
        return result;
    }

    protected JsonElement findStateOfChild(JsonElement uiData, String tag){
        if(uiData == null || !uiData.isJsonObject() || uiData.getAsJsonObject().getAsJsonObject().isEmpty()){
            return null;
        }
        return uiData.getAsJsonObject().getAsJsonArray("children").asList().stream().filter(it ->
            it.isJsonObject() && it.getAsJsonObject().has("tag") && it.getAsJsonObject().get("tag").getAsString().equals(tag)
        ).findFirst().orElse(null);
    }

    protected BaseWebAppUiElement findChildByTag(String tag){
        return  (BaseWebAppUiElement) getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals(tag)).findFirst().orElse(null);
    }
}

