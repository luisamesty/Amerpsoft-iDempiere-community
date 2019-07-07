/**
 * 
 */
package org.amerp.amxeditor.factory;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.amerp.amxeditor.model.*;
import org.compiere.model.MCountry;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;


/**
 * @author luisamesty
 *
 */
public class ModelFactory implements IModelFactory{
	
	CLogger log = CLogger.getCLogger(ModelFactory.class);
	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getClass(java.lang.String)
	 */
    @Override
    public Class<?> getClass(String p_tableName) {
	    // TODO Auto-generated method stub
	    if(p_tableName.equalsIgnoreCase(MLocationExt.Table_Name)) {
	    	return MLocationExt.class;
	    }
	    if(p_tableName.equalsIgnoreCase(MBPartnerLocationExt.Table_Name)) {
	    	return MBPartnerLocationExt.class;
	    }
//	    if(p_tableName.equalsIgnoreCase(MCountryExt.Table_Name)) {
//	    	return MCountryExt.class;
//	    }
		if(p_tableName.equals(MMunicipality.Table_Name)) {
			return MMunicipality.class;
		}
		if(p_tableName.equals(MParish.Table_Name)) {
			return MParish.class;
		}
//	    log.warning("....................");
//	    log.warning("p_tableName:"+p_tableName);
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getPO(java.lang.String, int, java.lang.String)
	 */
    @Override
    public PO getPO(String p_tableName, int Record_ID, String p_trxName) {
	    // TODO Auto-generated method stub
    	if(p_tableName.equalsIgnoreCase(MLocationExt.Table_Name)) {

    		return new MLocationExt(Env.getCtx(),Record_ID,p_trxName);
    	}
    	if(p_tableName.equalsIgnoreCase(MBPartnerLocationExt.Table_Name)) {

    		return new MBPartnerLocationExt(Env.getCtx(),Record_ID,p_trxName);
    	}
//	    if(p_tableName.equalsIgnoreCase(MCountryExt.Table_Name) ) {
//			return new MCountryExt(Env.getCtx(),Record_ID,p_trxName);
//	    }
		if(p_tableName.equals(MMunicipality.Table_Name)) {
			return new MMunicipality(Env.getCtx(),Record_ID,p_trxName);
		}
		if(p_tableName.equals(MParish.Table_Name)) {
			return new MParish(Env.getCtx(),Record_ID,p_trxName);
		}
//	    log.warning("....................");
//	    log.warning("p_tableName:"+p_tableName);
    	return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getPO(java.lang.String, java.sql.ResultSet, java.lang.String)
	 */
    @Override
    public PO getPO(String p_tableName, ResultSet p_rs, String p_trxName) {
	    // TODO Auto-generated method stub
    	if(p_tableName.equalsIgnoreCase(MLocationExt.Table_Name)) {
    		return new MLocationExt(Env.getCtx(),p_rs,p_trxName);
    	}
    	if(p_tableName.equalsIgnoreCase(MBPartnerLocationExt.Table_Name)){
    		return new MBPartnerLocationExt(Env.getCtx(),p_rs,p_trxName);
	    }
//	    if(p_tableName.equalsIgnoreCase(MCountryExt.Table_Name)) {
//			return new MCountryExt(Env.getCtx(),p_rs,p_trxName);
//	    } 
		if(p_tableName.equals(MMunicipality.Table_Name)) {
			return new MMunicipality(Env.getCtx(),p_rs,p_trxName);
		}
		if(p_tableName.equals(MParish.Table_Name)) {
			return new MParish(Env.getCtx(),p_rs,p_trxName);
		}
//	    log.warning("....................");
//	    log.warning("p_tableName:"+p_tableName);
	    return null;
    }

}
