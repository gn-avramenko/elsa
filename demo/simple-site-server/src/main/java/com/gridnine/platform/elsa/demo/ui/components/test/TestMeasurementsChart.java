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

import com.google.gson.JsonElement;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class TestMeasurementsChart extends BaseWebAppUiElement {

    public TestMeasurementsChart(String tag, TestMeasurementChartConfiguration config, OperationUiContext ctx) {
        super("main.MeasurementsChart", tag, ctx);
        setMonth(config.getMonth(), ctx);
        setMonthInt(config.getMonthInt(), ctx);
        setNextMonthEnabled(config.isNextMonthEnabled(), ctx);
        setPreviousMonthEnabled(config.isPreviousMonthEnabled(), ctx);
        decorateWithListeners();
    }

    public void setNextMonthEnabled(boolean enabled, OperationUiContext ctx) {
        setProperty("nextMonthEnabled", enabled, ctx);
    }

    public void setPreviousMonthEnabled(boolean enabled, OperationUiContext ctx) {
        setProperty("previousMonthEnabled", enabled, ctx);
    }

    public void setMonth(String month, OperationUiContext ctx) {
        setProperty("month", month, ctx);
    }

    public boolean isNextMonthEnabled() {
        return getProperty("nextMonthEnabled", Boolean.class);
    }
    public boolean isPreviousMonthEnabled() {
        return getProperty("previousMonthEnabled", Boolean.class);
    }

    public String getMonth() {
        return getProperty("month", String.class);
    }

    public int getMonthInt() {
        return getProperty("monthInt", Integer.class);
    }

    public  void setMonthInt(int monthInt, OperationUiContext ctx) {
        setProperty("monthInt", monthInt, ctx);
    }


    private void decorateWithListeners() {
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if(commandId.equals("showNextMonth")) {
            showNextMonth(data, ctx);
            return;
        }
        if(commandId.equals("showPreviousMonth")) {
            showPreviousMonth(data, ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }

    private void showNextMonth(JsonElement data, OperationUiContext ctx) {
        var mi = getMonthInt();
        mi+=1;
        setMonthInt(mi, ctx);
        setMonth("Month "+mi, ctx);
    }

    private void showPreviousMonth(JsonElement data, OperationUiContext ctx) {
        var mi = getMonthInt();
        mi-=1;
        setMonthInt(mi, ctx);
        setMonth("Month "+mi, ctx);
    }


}
