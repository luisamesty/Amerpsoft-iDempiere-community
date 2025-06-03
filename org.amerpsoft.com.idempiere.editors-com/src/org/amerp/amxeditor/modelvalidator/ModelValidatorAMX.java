package org.amerp.amxeditor.modelvalidator;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;

public class ModelValidatorAMX implements ModelValidator {

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		
		
	}

	@Override
	public int getAD_Client_ID() {
		
		return 0;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		
		return null;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		
		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		
		return null;
	}

}
