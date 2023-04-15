/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingDownloadDescription;
import com.gridnine.elsa.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

public class ElsaDemoRemotingMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var rmr = Environment.getPublished(RemotingMetaRegistry.class);
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest");
			{
				var propertyDescription = new PropertyDescription("fileId");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("fileId", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest");
		}
		{
			var remotingDescription = new RemotingDescription("elsa-demo-remoting");
			remotingDescription.getAttributes().put("xmlns", "http://gridnine.com/elsa/meta-remoting");
			rmr.getRemotings().put("elsa-demo-remoting", remotingDescription);
			{
				var groupDescription = new RemotingGroupDescription("test");
				remotingDescription.getGroups().put("test", groupDescription);
				{
					var downloadDescription = new RemotingDownloadDescription("download-index");
					downloadDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.DownloadIndexHandler");
					downloadDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest");
					groupDescription.getDownloads().put("download-index", downloadDescription);
				}
				{
					var downloadDescription = new RemotingDownloadDescription("download-video");
					downloadDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.DownloadVideoHandler");
					groupDescription.getDownloads().put("download-video", downloadDescription);
				}
			}
		}
	}
}