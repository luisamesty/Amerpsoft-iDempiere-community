/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amxeditor.factory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.adempiere.base.IDisplayTypeFactory;
import org.compiere.util.*;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Reference;

/**
 * @author luisamesty
 *
 */
public class DisplayTypeFactory implements IDisplayTypeFactory{
	
	CLogger log = CLogger.getCLogger(DisplayTypeFactory.class);
	
	//public static int LocationExtended = ((X_AD_Reference)new Query(Env.getCtx(),X_AD_Reference.Table_Name,"Name='LocationExtended'",null).first()).getAD_Reference_ID();
	//public static int LocationExtended = 1000066;
	public static int LocationExtended = ((X_AD_Reference)new Query(Env.getCtx(),X_AD_Reference.Table_Name,"Name='LocationExtended'",null).first()).getAD_Reference_ID();

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isID(int)
	 */
    @Override
    public boolean isID(int p_displayType) {
	    // TODO Auto-generated method stub
//log.setLevel(Level.WARNING);
    	if(p_displayType == LocationExtended) {
//log.warning("..........DisplayTypeFactory...........isID - LocationExtended "+LocationExtended);
    		return true;
    	} else {
//log.warning("..........DisplayTypeFactory...........isID:"+p_displayType);
    		return false;
    	}
    }
	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isNumeric(int)
	 */
    @Override
    public boolean isNumeric(int p_displayType) {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getDefaultPrecision(int)
	 */
    @Override
    public Integer getDefaultPrecision(int p_displayType) {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isText(int)
	 */
    @Override
    public boolean isText(int p_displayType) {
	    // TODO Auto-generated method stub
    	if(p_displayType == LocationExtended) {
//log.warning("..........DisplayTypeFactory...........");
//log.warning("isText - LocationExtended"+LocationExtended);
    		return false;
    	} else {
    		return false;
    	}
    	//return false;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isDate(int)
	 */
    @Override
    public boolean isDate(int p_displayType) {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isLookup(int)
	 */
    @Override
    public boolean isLookup(int p_displayType) {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#isLOB(int)
	 */
    @Override
    public boolean isLOB(int p_displayType) {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getNumberFormat(int, org.compiere.util.Language, java.lang.String)
	 */
    @Override
    public DecimalFormat getNumberFormat(int p_displayType, Language p_language, String p_pattern) {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getDateFormat(int, org.compiere.util.Language, java.lang.String)
	 */
    @Override
    public SimpleDateFormat getDateFormat(int p_displayType, Language p_language, String p_pattern) {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getClass(int, boolean)
	 */
    @Override
    public Class<?> getClass(int p_displayType, boolean p_yesNoAsBoolean) {
	    // TODO Auto-generated method stub
    	if(p_displayType == LocationExtended) {
//log.warning(".....................");
//log.warning("getClass - LocationExtended"+LocationExtended);
    		return Integer.class;
    	}  else {
    		return null;
    	}
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getSQLDataType(int, java.lang.String, int)
	 */
    @Override
    public String getSQLDataType(int p_displayType, String p_columnName, int p_fieldLength) {
	    // TODO Auto-generated method stub
    	if(p_displayType == LocationExtended) {
//log.warning(".....................");
//log.warning("getSQLDataType - LocationExtended"+LocationExtended);
    		return "NUMBER(10)";
    	} else {   	
    		return null;
    	}
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.IDisplayTypeFactory#getDescription(int)
	 */
    @Override
    public String getDescription(int p_displayType) {
	    // TODO Auto-generated method stub
    	if(p_displayType == LocationExtended) {
    		return "LocationExtended";   		

    	}   else { 	

    		return null;
    	}
    	
    }

}
