package org.amerp.tools.amtmodel;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.model.MProductionPlan;
import org.compiere.util.CLogger;

public class AMTToolsMProductionLine extends MProductionLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8969534479741331127L;

	private MProduction amtproductionParent;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMProductionLine.class);
	
	public AMTToolsMProductionLine(MProduction header) {
		super(header);
		// TODO Auto-generated constructor stub
	}
	
	public AMTToolsMProductionLine(MProductionPlan header) {
		super(header);
		// TODO Auto-generated constructor stub
	}

	public AMTToolsMProductionLine(Properties ctx, int M_ProductionLine_ID,
			String trxName) {
		super(ctx, M_ProductionLine_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public AMTToolsMProductionLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Parent Constructor
	 * @param plan
	 */
	public AMTToolsMProductionLine( AMTToolsMProduction header ) {
		super( header.getCtx(), 0, header.get_TrxName() );
		setM_Production_ID( header.get_ID());
		setAD_Client_ID(header.getAD_Client_ID());
		setAD_Org_ID(header.getAD_Org_ID());
		amtproductionParent = header;
	}

}
