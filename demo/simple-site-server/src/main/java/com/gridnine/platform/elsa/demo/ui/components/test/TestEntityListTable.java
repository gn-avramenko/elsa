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
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd4Arguments;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndArgument;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import org.springdoc.core.converters.models.Sort;

import java.util.List;
//codegen:import:end

//codegen:class:start
public class TestEntityListTable extends BaseWebAppUiElement {

    private RunnableWithExceptionAnd4Arguments<String, String, String, OperationUiContext> actionListener;

    private RunnableWithExceptionAndArgument<OperationUiContext> refreshDataListener;

    private RunnableWithExceptionAndArgument<OperationUiContext> loadMoreListener;

    private RunnableWithExceptionAnd2Arguments<OperationUiContext, TestSort> changeSortListener;

    public TestEntityListTable(String tag, TestEntityListConfiguration<TestOrganizationEntry> config, OperationUiContext ctx) {
        super("common.EntityList", tag, ctx);
        setColumns(config.getColumns(), ctx);
        setSort(config.getSort(), ctx);
        setData(config.getData(), ctx);
        setLoading(config.isLoading(), ctx);
    }

    public void setActionListener(RunnableWithExceptionAnd4Arguments<String, String, String, OperationUiContext> actionListener) {
        this.actionListener = actionListener;
    }

    public void setRefreshDataListener(RunnableWithExceptionAndArgument<OperationUiContext> refreshDataListener) {
        this.refreshDataListener = refreshDataListener;
    }

    public void setLoadMoreListener(RunnableWithExceptionAndArgument<OperationUiContext> loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setData(List<TestOrganizationEntry> data, OperationUiContext context) {
        setProperty("data", WebPeerUtils.serialize(data), context);
    }

    public void setSort(TestSort sort, OperationUiContext context) {
        WebPeerUtils.wrapException(() -> {
            setProperty("sort", sort, context);
            if (this.changeSortListener != null) {
                this.changeSortListener.run(context, sort);
            }
        });
    }

    public void setLoading(boolean loading, OperationUiContext context) {
        setProperty("loading", loading, context);
    }

    public void setChangeSortListener(RunnableWithExceptionAnd2Arguments<OperationUiContext, TestSort> changeSortListener) {
        this.changeSortListener = changeSortListener;
    }

    public void setColumns(List<TestEntityListColumnDescription> columns, OperationUiContext context) {
        setProperty("columns", WebPeerUtils.serialize(columns), context);
    }


    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if ("refresh-data".equals(commandId)) {
            this.refreshDataListener.run(ctx);
            return;
        }
        if ("load-more".equals(commandId)) {
            this.loadMoreListener.run(ctx);
            return;
        }
        if ("table-action".equals(commandId)) {
            var obj = data.getAsJsonObject();
            this.actionListener.run(WebPeerUtils.getString(obj, "rowId"), WebPeerUtils.getString(obj, "columnId"), WebPeerUtils.getString(obj, "actionId"), ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }

    public TestSort getSort() {
        var prop = getProperty("sort", Object.class);
        if(prop instanceof TestSort) {
            return (TestSort) prop;
        }
        var result = new TestSort();
        result.setDirection(TestSortDirection.valueOf(WebPeerUtils.getString((JsonObject) prop, "direction")));
        result.setFieldId(WebPeerUtils.getString((JsonObject) prop, "fieldId"));
        return result;
    }

//codegen:class:end

}
