/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.model.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import java.util.ArrayList;

public class TypesMetadata extends BaseIntrospectableObject{

	private final ArrayList<RSerializableType> serializableTypes = new ArrayList<>();

	private final ArrayList<RTagDescription> domainEntityTags = new ArrayList<>();

	private final ArrayList<RTagDescription> domainDatabaseTags = new ArrayList<>();

	private final ArrayList<RTagDescription> customEntityTags = new ArrayList<>();

	private final ArrayList<RTagDescription> l10nParameterTypeTags = new ArrayList<>();

	private final ArrayList<RTagDescription> remotingEntityTags = new ArrayList<>();

	public ArrayList<RSerializableType> getSerializableTypes(){
		return serializableTypes;
	}

	public ArrayList<RTagDescription> getDomainEntityTags(){
		return domainEntityTags;
	}

	public ArrayList<RTagDescription> getDomainDatabaseTags(){
		return domainDatabaseTags;
	}

	public ArrayList<RTagDescription> getCustomEntityTags(){
		return customEntityTags;
	}

	public ArrayList<RTagDescription> getL10nParameterTypeTags(){
		return l10nParameterTypeTags;
	}

	public ArrayList<RTagDescription> getRemotingEntityTags(){
		return remotingEntityTags;
	}

	@Override
	public Object getValue(String propertyName){

		if("serializableTypes".equals(propertyName)){
			return serializableTypes;
		}

		if("domainEntityTags".equals(propertyName)){
			return domainEntityTags;
		}

		if("domainDatabaseTags".equals(propertyName)){
			return domainDatabaseTags;
		}

		if("customEntityTags".equals(propertyName)){
			return customEntityTags;
		}

		if("l10nParameterTypeTags".equals(propertyName)){
			return l10nParameterTypeTags;
		}

		if("remotingEntityTags".equals(propertyName)){
			return remotingEntityTags;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		super.setValue(propertyName, value);
	}
}