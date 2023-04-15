/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public class UploadFileRequest extends BaseIntrospectableObject{

	private String fileId;

	public String getFileId(){
		return fileId;
	}

	public void setFileId(String value){
		this.fileId = value;
	}

	@Override
	public Object getValue(String propertyName){

		if("fileId".equals(propertyName)){
			return fileId;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("fileId".equals(propertyName)){
			this.fileId = (String) value;
			return;
		}

		super.setValue(propertyName, value);
	}
}