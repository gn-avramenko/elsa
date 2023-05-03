/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;

public class ElsaDemoL10nMetaRegistryConfigurator{

	public void configure(){
		var registry = Environment.getPublished(L10nMetaRegistry.class);
		{
			var bundleDescription = new L10nMessagesBundleDescription("demo");
			registry.getBundles().put(bundleDescription.getId(), bundleDescription);
			{
				var messageDescription = new L10nMessageDescription("Message_with_string_param");
				{
					var parameterDescription = new PropertyDescription("param");
					parameterDescription.setTagName("string-property");
					messageDescription.getParameters().put("param", parameterDescription);
				}
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Тестовое сообщение с параметром \"{0}\"");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
		}
	}
}