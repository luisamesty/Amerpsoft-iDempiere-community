package org.amerp.process.imp;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 * AMNImportDeleteRecords
 * Deletes all records from an Import Table
 * @author luisamesty
 *
 */
public class AMNImportDeleteRecords extends SvrProcess {
	/**	Table be deleted		*/
	private int				p_AD_Table_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return clear Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		StringBuilder msglog = new StringBuilder("AD_Table_ID=").append(p_AD_Table_ID);
		if (log.isLoggable(Level.INFO)) log.info(msglog.toString());
		//	get Table Info
		MTable table = new MTable (getCtx(), p_AD_Table_ID, get_TrxName());
		if (table.get_ID() == 0){
			StringBuilder msgexc = new StringBuilder("No AD_Table_ID=").append(p_AD_Table_ID);
			throw new IllegalArgumentException (msgexc.toString());
		}	
		String tableName = table.getTableName();
		if (!tableName.startsWith("AMN_I")){
			StringBuilder msgexc = new StringBuilder("Not an import table = ").append(tableName);
			throw new IllegalArgumentException (msgexc.toString());
		}	
		
		//	Delete
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE AD_Client_ID=").append(getAD_Client_ID());
		if (m_deleteOldImported)
		{
			sql.append(" AND I_IsImported='Y' ");
		}
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		StringBuilder msg = new StringBuilder().append(Msg.translate(getCtx(), tableName + "_ID")).append(" #").append(no);
		return msg.toString();
	}	//	ImportDelete

}
