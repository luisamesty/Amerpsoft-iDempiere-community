package org.amerp.amnvalidator;


import java.lang.reflect.Method;

import org.compiere.model.*;
import org.compiere.util.*;
import org.osgi.service.component.annotations.Component;

@Component(service = ModelValidator.class)
public class ValidateEmployeeLocation implements ModelValidator {

	static CLogger log = CLogger.getCLogger(ValidateEmployeeLocation.class);
	
    private int m_AD_Client_ID = -1;

    @Override
    public void initialize(ModelValidationEngine engine, MClient client) {
    	
    	log.warning("initialize ... Validate EmployeeLocation ..");
    	
        if (client != null)
            m_AD_Client_ID = client.getAD_Client_ID();
        engine.addModelChange(MBPartnerLocation.Table_Name, this);
    }

    @Override
    public int getAD_Client_ID() {
        return m_AD_Client_ID;
    }

    @Override
    public String modelChange(PO po, int type) throws Exception {
    	// Tabla C_BPartner_LOcation
    	try {
    	    Method getBPartnerIdMethod = po.getClass().getMethod("getC_BPartner_ID");
    	    Integer bpID = (Integer) getBPartnerIdMethod.invoke(po);

    	    if ((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && bpID != null && isEmployee(bpID)) {

    	        // Usar reflexión para isBillTo()
    	        Method isBillToMethod = po.getClass().getMethod("isBillTo");
    	        boolean isBillTo = (boolean) isBillToMethod.invoke(po);
    	        if (isBillTo) {
    	            Method getLocIdMethod = po.getClass().getMethod("getC_BPartner_Location_ID");
    	            Integer locID = (Integer) getLocIdMethod.invoke(po);

    	            int count = DB.getSQLValue(po.get_TrxName(),
    	                "SELECT COUNT(*) FROM C_BPartner_Location WHERE C_BPartner_ID=? AND IsBillTo='Y' AND C_BPartner_Location_ID<>?",
    	                bpID, locID);
    	            if (count > 0)
    	                return Msg.getMsg(Env.getCtx(), "BPartnerBillToAddressUnique");
    	        }

    	        // Usar reflexión para isPayFrom()
    	        Method isPayFromMethod = po.getClass().getMethod("isPayFrom");
    	        boolean isPayFrom = (boolean) isPayFromMethod.invoke(po);
    	        if (isPayFrom) {
    	            Method getLocIdMethod = po.getClass().getMethod("getC_BPartner_Location_ID");
    	            Integer locID = (Integer) getLocIdMethod.invoke(po);

    	            int count = DB.getSQLValue(po.get_TrxName(),
    	                "SELECT COUNT(*) FROM C_BPartner_Location WHERE C_BPartner_ID=? AND IsPayFrom='Y' AND C_BPartner_Location_ID<>?",
    	                bpID, locID);
    	            if (count > 0)
    	                return Msg.getMsg(Env.getCtx(), "BPartnerPayToAddressUnique");
    	        }
    	        
    	        // isShipTo
    	        Method isShipToMethod = po.getClass().getMethod("isShipTo");
    	        boolean isShipTo = (boolean) isShipToMethod.invoke(po);
    	        if (isShipTo) {
       	            Method getLocIdMethod = po.getClass().getMethod("getC_BPartner_Location_ID");
    	            Integer locID = (Integer) getLocIdMethod.invoke(po);
    	            
    	            int count = DB.getSQLValue(po.get_TrxName(),
    	                "SELECT COUNT(*) FROM C_BPartner_Location WHERE C_BPartner_ID=? AND IsShipTo='Y' AND C_BPartner_Location_ID<>?",
    	                bpID, locID);
    	            if (count > 0)
    	                return Msg.getMsg(Env.getCtx(), "BPartnerShipToAddressUnique");
    	        }

    	        // isRemitTo
    	        Method isRemitToMethod = po.getClass().getMethod("isRemitTo");
    	        boolean isRemitTo = (boolean) isRemitToMethod.invoke(po);
    	        if (isRemitTo) {
       	            Method getLocIdMethod = po.getClass().getMethod("getC_BPartner_Location_ID");
    	            Integer locID = (Integer) getLocIdMethod.invoke(po);
    	            
    	            int count = DB.getSQLValue(po.get_TrxName(),
    	                "SELECT COUNT(*) FROM C_BPartner_Location WHERE C_BPartner_ID=? AND IsRemitTo='Y' AND C_BPartner_Location_ID<>?",
    	                bpID, locID);
    	            if (count > 0)
    	                return Msg.getMsg(Env.getCtx(), "BPartnerRemitToAddressUnique");
    	        }
    	    }
    	} catch (Exception e) {
    	    log.warning("No se pudo obtener datos desde " + po.getClass().getName() + ": " + e.getMessage());
    	}
        return null;
    }

    private boolean isEmployee(int cBPartnerID) {
        String sql = "SELECT 1 FROM AMN_Employee WHERE C_BPartner_ID=? AND IsActive='Y'";
        return DB.getSQLValue(null, sql, cBPartnerID) > 0;
    }

    @Override
    public String docValidate(PO po, int timing) {
        return null;
    }

    public void login(MRole role, MUser user) {}

    @Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		// TODO Auto-generated method stub
		return null;
	}
}
