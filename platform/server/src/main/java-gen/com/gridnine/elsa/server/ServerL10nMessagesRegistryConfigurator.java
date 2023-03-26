/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.server;

import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;

public class ServerL10nMessagesRegistryConfigurator{

	public void configure(){
		var registry = Environment.getPublished(L10nMetaRegistry.class);
		{
			var bundleDescription = new L10nMessagesBundleDescription("core");
			registry.getBundles().put(bundleDescription.getId(), bundleDescription);
			{
				var messageDescription = new L10nMessageDescription("Found_several_records");
				{
					var parameterDescription = new PropertyDescription("objectType");
					parameterDescription.setTagName("string-property");
					messageDescription.getParameters().put("objectType", parameterDescription);
				}
				{
					var parameterDescription = new PropertyDescription("propertyName");
					parameterDescription.setTagName("string-property");
					messageDescription.getParameters().put("propertyName", parameterDescription);
				}
				{
					var parameterDescription = new PropertyDescription("propertyValue");
					parameterDescription.setTagName("string-property");
					messageDescription.getParameters().put("propertyValue", parameterDescription);
				}
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Найдено несколько записеей типа {0}, где {1} = {2}");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
		}
	}
}