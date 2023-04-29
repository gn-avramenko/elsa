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
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.remoting.RemotingSubscriptionDescription;
import com.gridnine.elsa.meta.remoting.RemotingUploadDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

public class ElsaDemoRemotingMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var rmr = Environment.getPublished(RemotingMetaRegistry.class);
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.GetIndexesRequest");
			{
				var propertyDescription = new PropertyDescription("document");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				entityDescription.getProperties().put("document", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.remoting.GetIndexesRequest", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.remoting.GetIndexesRequest");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.GetIndexesResponse");
			{
				var propertyDescription = new PropertyDescription("indexes");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection");
				entityDescription.getProperties().put("indexes", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.remoting.GetIndexesResponse", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.remoting.GetIndexesResponse");
		}
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
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.UploadFileRequest");
			{
				var propertyDescription = new PropertyDescription("fileId");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("fileId", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.remoting.UploadFileRequest", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.remoting.UploadFileRequest");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent");
			{
				var propertyDescription = new PropertyDescription("document");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				entityDescription.getProperties().put("document", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent");
		}
		{
			var remotingDescription = new RemotingDescription("elsa-demo-remoting");
			remotingDescription.getAttributes().put("xmlns", "http://gridnine.com/elsa/meta-remoting");
			rmr.getRemotings().put("elsa-demo-remoting", remotingDescription);
			{
				var groupDescription = new RemotingGroupDescription("test");
				remotingDescription.getGroups().put("test", groupDescription);
				{
					var serverCallDescription = new RemotingServerCallDescription("getIndexes");
					serverCallDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.GetIndexesHandler");
					serverCallDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.GetIndexesRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.demo.model.remoting.GetIndexesResponse");
					groupDescription.getServerCalls().put("getIndexes", serverCallDescription);
				}
				{
					var subscriptionDescription = new RemotingSubscriptionDescription("demo-document-changed-subscription");
					serverCallDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.IndexChangeSubscriptionHandler");
					subscriptionDescription.setEventClassName("com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent");
					groupDescription.getSubscriptions().put("demo-document-changed-subscription", subscriptionDescription);
				}
				{
					var downloadDescription = new RemotingDownloadDescription("download-index");
					downloadDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.DownloadIndexHandler");
					downloadDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest");
					groupDescription.getDownloads().put("download-index", downloadDescription);
				}
				{
					var downloadDescription = new RemotingDownloadDescription("download-video");
					downloadDescription.getAttributes().put("big-file", "true");
					downloadDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.DownloadVideoHandler");
					groupDescription.getDownloads().put("download-video", downloadDescription);
				}
				{
					var uploadDescription = new RemotingUploadDescription("upload-file");
					uploadDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.demo.server.remoting.UploadFileHandler");
					uploadDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.UploadFileRequest");
					groupDescription.getUploads().put("upload-file", uploadDescription);
				}
			}
		}
	}
}