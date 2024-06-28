package org.amerp.amncallouts;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Lot;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPeriod;
import org.compiere.model.MYear;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMN_Payroll_Lot_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Payroll_callout.class);
	Integer C_Year_ID = 0;
	Integer C_Period_ID = 0;	
	Integer AMN_Process_ID =0;
	Integer AMN_Period_ID=0;
	String LotValue ="";
	String LotName = "";
	String LotDescription = "";
	MAMN_Process apr = null;
	MPeriod mpe = null;
	MYear mye = null;
	Date date = new Date();
	Timestamp DateAcct = null;
	Boolean isError=false;
	String errorMessage="";
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		//
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dt2 = new SimpleDateFormat("dd/MM");
		SimpleDateFormat dt3 = new SimpleDateFormat("hh:mm:ss");
		// 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// AMN_Process_ID
		if (mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_AMN_Process_ID) != null) {
			// AMN_Payroll_ID
			AMN_Process_ID = (Integer) mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_AMN_Process_ID);
			apr = new MAMN_Process(Env.getCtx(),AMN_Process_ID,null);
			LotValue = apr.getAMN_Process_Value().trim()+"-"+month+"-"+day+"-"+dt3.format(date);
			LotName = apr.getAMN_Process_Value().trim();
			LotDescription = apr.getName().trim();
		}
		// C_Year_ID
		if (mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Year_ID) != null) {
			// AMN_Payroll_ID
			C_Year_ID = (Integer) mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Year_ID);
			mye = new MYear(Env.getCtx(),C_Year_ID,null);
			LotValue = apr.getAMN_Process_Value().trim()+"-"+mye.getFiscalYear()+"-"+month+"-"+day+"-"+dt3.format(date);
			LotDescription = LotDescription +" "+ mye.getFiscalYear()+"-"+mye.getDescription().trim();
		}
		// C_Period_ID
		if (mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_AMN_Process_ID) != null
				&& mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Period_ID) != null
				&& mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Year_ID) != null ) {
			// C_Period_ID
			C_Period_ID = (Integer) mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Period_ID);
			mpe= new MPeriod(Env.getCtx(),C_Period_ID,null);
			if (DateAcct == null) {
				DateAcct = mpe.getEndDate();
			}
			LotValue = apr.getAMN_Process_Value().trim()+"-"+mye.getFiscalYear()+"-"+month+"-"+day+"-"+dt3.format(DateAcct);
			LotName = LotName+" - "+ mye.getFiscalYear()+" "+dt2.format(mpe.getStartDate()) +"-"+ dt2.format(mpe.getEndDate())+" Lot "+day+"-"+dt3.format(DateAcct);
			LotDescription = apr.getName().trim() +" "+ mye.getFiscalYear()+"-"+mye.getDescription().trim() 
					+"  ("+dt.format(mpe.getStartDate())+" - "+dt.format(mpe.getEndDate())+")"+" Lot "+day+"-"+dt3.format(DateAcct);			
		}
		// DateActt
		if (mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_DateAcct) != null
			&& mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_C_Period_ID) != null ) {
			DateAcct= (Timestamp) mTab.getValue(MAMN_Payroll_Lot.COLUMNNAME_DateAcct);
			if (DateAcct.before(mpe.getStartDate()) || DateAcct.after( mpe.getEndDate()) ) {
				errorMessage= Msg.translate(Env.getCtx(),"Error")+
						" "+Msg.translate(Env.getCtx(),"DateAcct")+" "+DateAcct+
						" ***"+Msg.translate(Env.getCtx(),"ValidationError")+" ***";
				isError=true;
			} else {
				errorMessage= Msg.translate(Env.getCtx(),"DateAcct")+":"+DateAcct+
						"  ***"+Msg.translate(Env.getCtx(),"OK")+" ***";

				isError=false;			
			}
		}
		// 
		mTab.setValue("Value", LotValue);
		mTab.setValue("Name", LotName);
		mTab.setValue("description", LotDescription);
		if (isError) {
			// Error Message
			mTab.fireDataStatusEEvent(errorMessage,Msg.translate(Env.getCtx(),"DateAcct")+":\r\n",true);
			DateAcct = mpe.getEndDate();
		} else {
			if (DateAcct != null) {
				// Info Message
				mTab.fireDataStatusEEvent(errorMessage,Msg.translate(Env.getCtx(),"DateAcct")+":\r\n",false);
			}
		}
		mTab.setValue("DateAcct", DateAcct);
		return null;
	}

}
