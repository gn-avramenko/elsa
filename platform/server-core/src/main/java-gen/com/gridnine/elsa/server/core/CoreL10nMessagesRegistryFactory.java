/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.server.core;

import com.gridnine.elsa.common.core.l10n.Localizer;
import com.gridnine.elsa.common.core.model.common.L10nMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoreL10nMessagesRegistryFactory{

	@Autowired
	private Localizer localizer;

	public String Found_several_records(String objectType, String propertyName, String propertyValue){
		return localizer.toString("core", "Found_several_records", objectType, propertyName, propertyValue);
	}

	public L10nMessage Found_several_recordsMessage(String objectType, String propertyName, String propertyValue){
		return new L10nMessage("core", "Found_several_records", objectType, propertyName, propertyValue);
	}

	public String Yes(){
		return localizer.toString("core", "Yes");
	}

	public L10nMessage YesMessage(){
		return new L10nMessage("core", "Yes");
	}

	public String No(){
		return localizer.toString("core", "No");
	}

	public L10nMessage NoMessage(){
		return new L10nMessage("core", "No");
	}

	public String Choose_variant(){
		return localizer.toString("core", "Choose_variant");
	}

	public L10nMessage Choose_variantMessage(){
		return new L10nMessage("core", "Choose_variant");
	}

	public String Question(){
		return localizer.toString("core", "Question");
	}

	public L10nMessage QuestionMessage(){
		return new L10nMessage("core", "Question");
	}

	public String Object_not_found(String objectId, String objectUid){
		return localizer.toString("core", "Object_not_found", objectId, objectUid);
	}

	public L10nMessage Object_not_foundMessage(String objectId, String objectUid){
		return new L10nMessage("core", "Object_not_found", objectId, objectUid);
	}
}