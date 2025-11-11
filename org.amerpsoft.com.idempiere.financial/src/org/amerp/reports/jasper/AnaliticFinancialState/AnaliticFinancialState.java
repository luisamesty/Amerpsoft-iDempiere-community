/**
 * 
 */
package org.amerp.reports.jasper.AnaliticFinancialState;

/**
 * @author luisamesty
 *
 */

import java.sql.Timestamp;

import org.compiere.process.*;

//import net.sf.jasperreports.
public class AnaliticFinancialState extends SvrProcess {
	// Private Vars
	private int ad_client_id=0;
	private int ad_org_id;
	Timestamp DateIni= null;
	Timestamp DateEnd=null;
	@Override
	protected void prepare() {
		// Reading all parameters
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null )
				;
			else if (name.equalsIgnoreCase("ad_client_id"))
				ad_client_id = para[i].getParameterAsInt();
			else if (name.equalsIgnoreCase("ad_org_id"))
				ad_org_id = para[i].getParameterAsInt();
			else if (name.equalsIgnoreCase("DateIni"))
				DateIni = para[i].getParameterAsTimestamp();
			else if (name.equalsIgnoreCase("DateEnd"))
				DateEnd = para[i].getParameterAsTimestamp();

			else
				log.severe("Unknown Parameter: "+name);
		}
		log.info("process prepared with "+ad_client_id);
		addLog("Process prepared with "+ad_client_id);
	}
	@Override
	protected String doIt() throws Exception {
		getProcessInfo().getAD_Process_ID();

		return null ;
	}
}
