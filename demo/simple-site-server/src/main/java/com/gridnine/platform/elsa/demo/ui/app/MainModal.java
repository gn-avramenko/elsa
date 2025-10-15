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

package com.gridnine.platform.elsa.demo.ui.app;

import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.platform.elsa.webApp.WebAppUtils;
import com.gridnine.platform.elsa.webApp.common.*;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import java.util.ArrayList;
import java.util.List;

public class MainModal extends MainModalSkeleton{

	public MainModal(String tag, MainModalConfiguration config, OperationUiContext ctx){
		super(tag, config, ctx);
		setCloseListener(this::removeChildren, ctx);
	}
	public void closeDialog(OperationUiContext context){
		removeChildren(context);
		setVisible(false, context);
	}
	public void showConfirm(String message, RunnableWithExceptionAndArgument<OperationUiContext> okCallback, OperationUiContext context){
		var confirmMessageConfig = new ConfirmMessageConfiguration();
		confirmMessageConfig.setTitle(message);
		var confirmMessage = new ConfirmMessage("message", confirmMessageConfig, context);
		var buttons = new ArrayList<DialogButton>();
		{
			var buttonConfig = new DialogButtonConfiguration();
			buttonConfig.setTitle("OK");
			buttonConfig.setClickListener(ctx ->{
				okCallback.run(ctx);
				closeDialog(ctx);
			}
			);
			var okButton = new DialogButton("ok", buttonConfig, context);
			buttons.add(okButton);
		}
		{
			var buttonConfig = new DialogButtonConfiguration();
			buttonConfig.setTitle("Cancel");
			buttonConfig.setClickListener(ctx ->{
				closeDialog(ctx);
			}
			);
			var cancelButton = new DialogButton("cancel", buttonConfig, context);
			buttons.add(cancelButton);
		}
		showDialog("Confirm", confirmMessage, buttons, context);
	}
	private void removeChildren(OperationUiContext context){
		var content = WebAppUtils.findChildByTag(this, "content");
		if(content != null){
			 removeChild(context, content);
		}
		var buttons = WebAppUtils.findChildByTag(this, "contebuttonsnt");
		if(buttons != null){
			 removeChild(context, buttons);
		}
	}
	public void showDialog(String title, BaseUiElement content, List<DialogButton> buttons, OperationUiContext context){
		removeChildren(context);
		var config = new ContentWrapperConfiguration();
		config.setFlexDirection(FlexDirection.COLUMN);
		var contentWrapper = new ContentWrapper("content", config, context);
		contentWrapper.addChild(context, content, 0);
		var buttonsWrapper = new ContentWrapper("buttons", config, context);
		for(var button : buttons){
			buttonsWrapper.addChild(context, button, 0);
		}
		setTitle(title, context);
		setVisible(true, context);
		addChild(context, contentWrapper,0);
		addChild(context, buttonsWrapper,0);
	}
}