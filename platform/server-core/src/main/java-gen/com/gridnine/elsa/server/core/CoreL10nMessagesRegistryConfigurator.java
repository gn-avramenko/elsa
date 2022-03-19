/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.server.core;

import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessageParameterDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistryConfigurator;
import org.springframework.stereotype.Component;

@Component
public class CoreL10nMessagesRegistryConfigurator implements L10nMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(L10nMetaRegistry registry){
		{
			var bundleDescription = new L10nMessagesBundleDescription("core");
			registry.getBundles().put(bundleDescription.getId(), bundleDescription);
			{
				var messageDescription = new L10nMessageDescription("Found_several_records");
				{
					var paramDescription = new L10nMessageParameterDescription("objectType");
					paramDescription.setType(StandardValueType.STRING);
					messageDescription.getParameters().put(paramDescription.getId(), paramDescription);
				}
				{
					var paramDescription = new L10nMessageParameterDescription("propertyName");
					paramDescription.setType(StandardValueType.STRING);
					messageDescription.getParameters().put(paramDescription.getId(), paramDescription);
				}
				{
					var paramDescription = new L10nMessageParameterDescription("propertyValue");
					paramDescription.setType(StandardValueType.STRING);
					messageDescription.getParameters().put(paramDescription.getId(), paramDescription);
				}
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "найдено несколько записей {0}, где {1} = {2}");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
			{
				var messageDescription = new L10nMessageDescription("Yes");
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "Да");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
			{
				var messageDescription = new L10nMessageDescription("No");
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "Нет");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
			{
				var messageDescription = new L10nMessageDescription("Choose_variant");
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "Выберите вариант");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
			{
				var messageDescription = new L10nMessageDescription("Question");
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "Вопрос");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
			{
				var messageDescription = new L10nMessageDescription("Object_not_found");
				{
					var paramDescription = new L10nMessageParameterDescription("objectId");
					paramDescription.setType(StandardValueType.STRING);
					messageDescription.getParameters().put(paramDescription.getId(), paramDescription);
				}
				{
					var paramDescription = new L10nMessageParameterDescription("objectUid");
					paramDescription.setType(StandardValueType.STRING);
					messageDescription.getParameters().put(paramDescription.getId(), paramDescription);
				}
				messageDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "Объект {0} с идентификатором {1} не найден");
				bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);
			}
		}
	}
}