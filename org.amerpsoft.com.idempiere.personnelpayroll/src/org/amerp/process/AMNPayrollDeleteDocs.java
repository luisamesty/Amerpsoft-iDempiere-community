package org.amerp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Period;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class AMNPayrollDeleteDocs {

	// PUBLIC VARS
	static String Msg_Value="";
	static CLogger log = CLogger.getCLogger(AMNPayrollDeleteDocs.class);
	
	/**
	 * amnPayrollDeleteReceiptssGeneratePayrollArrays
	 * @param ctx
	 * @param amnperiod
	 * @param p_AMN_Payroll_ID
	 * @param ReceiptsGenList
	 * @param ReceiptConcepts
	 * @param Msg_Error
	 * @param trxName
	 * @return
	 */
	public static boolean amnPayrollDeleteReceiptsGeneratePayrollArrays(Properties ctx, MAMN_Period amnperiod, int p_AMN_Payroll_ID, 
			List<AMNReceipts> ReceiptsGenList, List<AMNReceiptLines> ReceiptConcepts, String Msg_Error, String trxName) {
			
		if (amnperiod != null && amnPayrollCreateReceiptArray(ctx, amnperiod, p_AMN_Payroll_ID, ReceiptsGenList, ReceiptConcepts, Msg_Error, trxName)
				&& amnPayrollCreateReceiptLinesArray(ctx, amnperiod, p_AMN_Payroll_ID, ReceiptsGenList, ReceiptConcepts, Msg_Error, trxName) ) 
			return true;
		else 
			return false;
	}
	
	
	/**
	 * 
	 * @param ctx
	 * @param amnp
	 * @param trxName
	 * @return
	 */
	public static String  amnPayrollDeleteInvoicesAllProcessOneHeader(Properties ctx, MAMN_Payroll amnp, String trxName) {
	
		String retMesg = "";
		if (amnp != null) {
			amnp.delete(true);
		}
		return retMesg;
	
	}
	
	/**
	 * 
	 * @param ctx
	 * @param amnp
	 * @param p_AMN_Payroll_Detail_ID
	 * @param trxName
	 * @return
	 */
	public static String amnPayrollDeleteInvoicesAllProcessOneLine(Properties ctx, MAMN_Payroll amnp, int p_AMN_Payroll_Detail_ID, String trxName) {
	
		String retMesg = "";
	    // 1. Validar el objeto MAMN_Payroll directamente para evitar la validación anidada.
	    if (amnp == null) {
	    	retMesg = Msg.getElement(ctx, MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_Detail_ID) + " "+
	    			Msg.getMsg(ctx, "NullColumns");
	    }

	    // 2. Simplificar la instanciación y la validación
	    MAMN_Payroll_Detail amnpd = new MAMN_Payroll_Detail(ctx, p_AMN_Payroll_Detail_ID, trxName);

	    // 3. Validar si la instancia es válida (es decir, si el ID existe y se cargó correctamente).
	    // iDempiere devuelve un objeto con ID = 0 si no lo encuentra.
	    if (amnpd.get_ID() <= 0) {
	        // Si no se encuentra el detalle, no hay nada que borrar.
	    	retMesg = Msg.getElement(ctx, MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_Detail_ID) + " "+
	    			amnpd.get_ID();
	    }

	    try {
	        // 4. Utilizar el método de eliminación con mejor control
	        // El parámetro 'true' indica que se hará una eliminación 'real' (hard delete).
	        // En muchos casos, un 'false' (soft delete) es preferible para auditoría.
	        amnpd.delete(true);
	    } catch (Exception e) {
	        // 5. Manejo de excepciones adecuado
	        // Registrar el error para su posterior análisis. Esto es crucial en iDempiere.
	        // CLogger.getCLogger(AMNUtil.class).log(Level.SEVERE, "Error deleting MAMN_Payroll_Detail with ID: " + p_AMN_Payroll_Detail_ID, e);

	        // 6. Retornar false en caso de fallo
	        retMesg = e.getMessage();
	    }
        return retMesg;
	}


	/**
	 * 
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static boolean amnPayrollCreateReceiptArray(Properties ctx, MAMN_Period amnperiod, int p_AMN_Payroll_ID, 
				List<AMNReceipts> ReceiptsGenList, List<AMNReceiptLines> ReceiptConcepts,  String Msg_Error, String trxName) {
			
	    int AMN_Employee_ID=0;
		int AMN_Payroll_ID = 0;
		String DocumentNo ="";
		String DocStatus  ="";
		String Employee_Name ="";
		String Employee_Value="";
		AMNReceipts Receipt = null;
		String sql="";
		// RECEIPTS FROM AMN_Period_ID
		sql = "SELECT \n"
				+ " pyr.amn_payroll_id, "
				+ " pyr.documentno, "
				+ " pyr.description, "
				+ " pyr.docstatus , "
				+ " emp.AMN_Employee_ID, "
				+ " emp.value AS employee_value, "
				+ " emp.name AS employee_name "
				+ " FROM amn_period aper  "
				+ " INNER JOIN amn_payroll pyr ON pyr.amn_period_id = aper.amn_period_id "
				+ " INNER JOIN amn_employee emp ON emp.amn_employee_id = pyr.amn_employee_id "
				+ " WHERE aper.AMN_Period_ID=?  " ;
		if (p_AMN_Payroll_ID >0)
			sql = sql + "AND pyr.AMN_Payroll_ID=? ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		// **********************
		// Document Headers
		// **********************
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnperiod.getAMN_Period_ID());
            if (p_AMN_Payroll_ID >0)
            	pstmt.setInt (2, p_AMN_Payroll_ID);	
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				AMN_Payroll_ID = rspc.getInt(1);
				DocumentNo = rspc.getString(2).trim();
				DocStatus  = rspc.getString(4).trim();
				AMN_Employee_ID = rspc.getInt(5);
				Employee_Value= rspc.getString(6).trim();
				Employee_Name = rspc.getString(7).trim();
				// Document Header
			    // Verify if AMN_Payroll_ID is created and POSTED
				if (DocStatus.compareToIgnoreCase("DR")!=0)
					Msg_Error = Msg_Error + "\r\nDocument: "+DocumentNo+"("+DocStatus+")  "+Employee_Value.trim()+"-"+Employee_Name.trim();
				Receipt = new AMNReceipts();
				Receipt.setAMN_Employee_ID(AMN_Employee_ID);
				Receipt.setEmployee_Value(Employee_Value);
				Receipt.setEmployee_Name(Employee_Name);
				Receipt.setAMN_Payroll_ID(AMN_Payroll_ID);
				ReceiptsGenList.add(Receipt);
			}				
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		DB.close(rspc, pstmt);
		rspc = null; pstmt = null;
		// Number of receipts created or updated
		if (ReceiptsGenList.size() > 0 )
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static boolean amnPayrollCreateReceiptLinesArray(Properties ctx, MAMN_Period amnperiod, int p_AMN_Payroll_ID, 
			List<AMNReceipts> ReceiptsGenList, List<AMNReceiptLines> ReceiptConcepts,  String Msg_Error, String trxName )  {
    	// ReceiptConcepts
		AMNReceiptLines ReceiptLines = null;
    	String sql="";
		//
		sql ="SELECT "
				+ " pyr.amn_payroll_id, "
				+"  pyr_d.amn_payroll_detail_id, "
				+ " pyr_d.calcorder , "
				+ " pyr_d.amn_concept_types_proc_id , "
				+ " pyr_d.qtyvalue  "
				+ " FROM amn_period aper "
				+ " INNER JOIN amn_payroll pyr ON pyr.amn_period_id = aper.amn_period_id "
				+ " INNER JOIN amn_employee emp ON emp.amn_employee_id = pyr.amn_employee_id "
				+ " INNER JOIN amn_payroll_detail pyr_d ON pyr_d.amn_payroll_id = pyr.amn_payroll_id "
				+ " WHERE aper.AMN_Period_ID=? " 
			;
		if (p_AMN_Payroll_ID >0)
			sql = sql + "AND pyr.AMN_Payroll_ID=? ";
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, amnperiod.getAMN_Period_ID());
            if (p_AMN_Payroll_ID >0)
            	pstmt1.setInt (2, p_AMN_Payroll_ID);	
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				ReceiptLines = new AMNReceiptLines();
				ReceiptLines.setAMN_Payroll_ID(rsod1.getInt(1));
				ReceiptLines.setAMN_Payroll_Detail_ID(rsod1.getInt(2));
				ReceiptLines.setCalcOrder(rsod1.getInt(3));
				ReceiptLines.setAMN_Concept_Type_Proc_ID(rsod1.getInt(4));
				ReceiptLines.setQtyValue(rsod1.getBigDecimal(5));
				ReceiptLines.setAMN_Process_ID(amnperiod.getAMN_Process_ID());
				ReceiptLines.setAMN_Contract_ID(amnperiod.getAMN_Contract_ID());
				ReceiptConcepts.add(ReceiptLines);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; 
			pstmt1 = null;
		}
		// Number of receipts created or updated
		if (ReceiptConcepts.size() > 0 )
			return true;
		else
			return false;
    }

}
