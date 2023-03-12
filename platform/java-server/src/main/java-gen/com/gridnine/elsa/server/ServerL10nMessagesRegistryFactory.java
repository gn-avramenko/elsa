/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.server;

import com.gridnine.elsa.core.l10n.Localizer;
import com.gridnine.elsa.core.model.common.L10nMessage;

public class ServerL10nMessagesRegistryFactory{

	public String Found_several_records(String objectType, String propertyName, String propertyValue){
		return Localizer.get().toString("core", "Found_several_records", null, objectType, propertyName, propertyValue);
	}

	public static L10nMessage Found_several_recordsMessage(String objectType, String propertyName, String propertyValue){
		return new L10nMessage("core", "Found_several_records", objectType, propertyName, propertyValue);
	}
}