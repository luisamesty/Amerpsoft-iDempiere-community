package org.amerp.pp.factories;

import org.adempiere.base.IModelValidatorFactory;
import org.amerp.amnvalidator.ValidateEmployeeLocation;
import org.compiere.model.ModelValidator;
import org.osgi.service.component.annotations.Component;

@Component(
	    service = IModelValidatorFactory.class,
	    property = {
	        "service.ranking:Integer=100"
	    }
)
public class AMNValidatorFactory implements IModelValidatorFactory{

	@Override
	public ModelValidator newModelValidatorInstance(String className) {
		// ValidateEmployeeLocation
		if ("org.amerp.pp.factories.AMNValidatorFactory".equals(className))
			return new ValidateEmployeeLocation();
		return null;
	}

}

