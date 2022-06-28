/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.rest.core;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import java.util.*;

public class RemotingEntityDescription extends BaseIntrospectableObject{

	private final List<RemotingEntityPropertyDescription> properties = new ArrayList<>();

	private final List<RemotingEntityCollectionDescription> collections = new ArrayList<>();

	private final List<RemotingEntityMapDescription> maps = new ArrayList<>();

	public List<RemotingEntityPropertyDescription> getProperties(){
		return properties;
	}

	public List<RemotingEntityCollectionDescription> getCollections(){
		return collections;
	}

	public List<RemotingEntityMapDescription> getMaps(){
		return maps;
	}

	@Override
	public Collection<?> getCollection(String collectionName){

		if("properties".equals(collectionName)){
			return properties;
		}

		if("collections".equals(collectionName)){
			return collections;
		}

		if("maps".equals(collectionName)){
			return maps;
		}

		return super.getCollection(collectionName);
	}
}