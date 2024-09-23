/* Test.java
 * Test Class for Setting AD_Rule correctly
 */
package org.amerp.amnutilities;
import org.compiere.model.*;
import org.compiere.util.*;
import java.math.*;
import java.sql.Timestamp;

public class Test {
	
	double A_EmployeeNYears = 0;
	Timestamp InvDateIni;
	double retValue =0.0;
	double A_EmployeeNYear=0.0;
	String sql ;
	String A_Ctx;
	String as;
	double prueba () { 
		retValue=A_EmployeeNYears;
		
		// InvDateIni
		sql = "SELECT InvDateIni FROM adempiere.amn_payroll WHERE amn_payroll_id=?" ;
		Object[] args = new Object[] {
				retValue};
		InvDateIni=DB.getSQLValueTS(A_Ctx, sql,args);

		if (retValue > 15)
		        retValue = 15;
		return (retValue);
	}

}

