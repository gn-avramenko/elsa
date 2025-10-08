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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.demo.ui.main;

import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class MainPage extends MainPageSkeleton{

	public MainPage(String tag, OperationUiContext ctx){
		super(tag, ctx);
	}

    @Override
    protected MainPageConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new MainPageConfiguration();
        result.setFlexDirection(FlexDirection.COLUMN);
        {
            var chart = new MeasurementsChartConfiguration();
            chart.setMonth("0");
            chart.setNextMonthEnabled(true);
            chart.setPreviousMonthEnabled(true);
            chart.setShowNextMonthListener((ct) ->{
                getMeasurementsChart().setMonth(String.valueOf(Integer.parseInt(getMeasurementsChart().getMonth())+1), ct);
            });
            chart.setShowPreviousMonthListener((ct) ->{
                getMeasurementsChart().setMonth(String.valueOf(Integer.parseInt(getMeasurementsChart().getMonth())-1), ct);
            });
            result.setMeasurementsChart(chart);
        }
        {
            var feebackContent = new FeedbackContentConfiguration();
            feebackContent.setDeferred(true);
            result.setFeedbackContent(feebackContent);
        }
        {
            var sendFeedback = new SendFeedbackButtonConfiguration();
            sendFeedback.setTitle("Send");
            sendFeedback.setClickListener((ct) ->{
                var value = getFeedbackContent().getValue().getValue();
                System.out.println(value);
            });
            result.setSendFeedback(sendFeedback);
        }
        return result;
    }

}