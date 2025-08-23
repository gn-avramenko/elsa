//codegen:header:start
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

package com.gridnine.platform.elsa.demo.ui.components.test;
//codegen:header:end
//codegen:import:start
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.common.core.model.common.CallableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd4Arguments;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.List;
//codegen:import:end

//codegen:class:start
public class TestEntityListTable extends BaseWebAppUiElement {

    private RunnableWithExceptionAnd4Arguments<String, String, String,OperationUiContext> actionListener;

    private List<TestEntityListColumnDescription> columns;

    private CallableWithExceptionAnd2Arguments<JsonArray, JsonObject, OperationUiContext> dataSource;

    public TestEntityListTable(String tag, TestEntityListConfiguration config, OperationUiContext ctx) {
        super("common.EntityList", tag, ctx);
        setColumns(config.getColumns(), ctx);
        setData(config.getData(), ctx);
    }

    public void setActionListener(RunnableWithExceptionAnd4Arguments<String, String, String,OperationUiContext> actionListener, OperationUiContext context) {
        this.actionListener = actionListener;
    }

    public void setData(JsonArray data, OperationUiContext context) {
        setProperty("data", data, context);
    }

    public void setSort(JsonObject sort, OperationUiContext context) {
        setProperty("sort", sort, context);
    }

    public JsonArray getData() {
        return getProperty("data", JsonArray.class);
    }

    public List<TestEntityListColumnDescription> getColumns() {
        return columns;
    }

    public void setDataSource(CallableWithExceptionAnd2Arguments<JsonArray, JsonObject, OperationUiContext> dataSource) {
        this.dataSource = dataSource;
    }

    public CallableWithExceptionAnd2Arguments<JsonArray, JsonObject, OperationUiContext> getDataSource() {
        return dataSource;
    }

    public void setColumns(List<TestEntityListColumnDescription> columns, OperationUiContext context) {
        this.columns = columns;
        var array = new JsonArray();
        for (TestEntityListColumnDescription column : columns) {
            ExceptionUtils.wrapException(()->array.add(column.serialize()));
        }
        setProperty("columns", array, context);
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if("get-data".equals(commandId)){
            var res = this.dataSource.call( data.getAsJsonObject(), ctx);
            setSort(data.getAsJsonObject().get("sort").getAsJsonObject(), ctx);
            setData(res, ctx);
            return;
        }
        if("table-action".equals(commandId)){
            var obj = data.getAsJsonObject();
            this.actionListener.run(WebPeerUtils.getString(obj, "rowId"), WebPeerUtils.getString(obj, "columnId"), WebPeerUtils.getString(obj, "actionId"), ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }
//codegen:class:end
}
