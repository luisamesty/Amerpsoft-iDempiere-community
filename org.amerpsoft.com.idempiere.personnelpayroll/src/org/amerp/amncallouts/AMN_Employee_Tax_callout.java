package org.amerp.amncallouts;

import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee_Tax;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.TimeUtil;

public class AMN_Employee_Tax_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Employee_Tax_callout.class);
	
	String ID_YearS="";
	int ID_Year=0;
	Timestamp ID_PeriodIni=null;
	Timestamp ID_PeriodEnd=null;
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		// ******************************
		// FieldRef: FiscalYR
	    //	(Verify if null)	    		
		// ******************************
	    if (mTab.getValue(MAMN_Employee_Tax.COLUMNNAME_FiscalYear) != null) {
	    	ID_YearS =  (String) mTab.getValue(MAMN_Employee_Tax.COLUMNNAME_FiscalYear);
	    	ID_Year=Integer.parseInt(ID_YearS);
			ID_PeriodIni = TimeUtil.getDay(ID_Year, 1, 1);
			ID_PeriodEnd = TimeUtil.getDay(ID_Year, 12, 31);
//log.warning("Period Ini:"+ID_PeriodIni);
//log.warning("Period End:"+ID_PeriodEnd);
			if (mTab.getValue(MAMN_Employee_Tax.COLUMNNAME_ValidFrom) == null)
				mTab.setValue(MAMN_Employee_Tax.COLUMNNAME_ValidFrom,ID_PeriodIni);
			if (mTab.getValue(MAMN_Employee_Tax.COLUMNNAME_ValidTo) == null)
				mTab.setValue(MAMN_Employee_Tax.COLUMNNAME_ValidTo,ID_PeriodEnd);
	    }
		
		return null;
	}

}
