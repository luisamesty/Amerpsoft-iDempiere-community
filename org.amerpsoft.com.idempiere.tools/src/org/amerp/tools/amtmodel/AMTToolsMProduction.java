package org.amerp.tools.amtmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.acct.Doc;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MFactAcct;
import org.compiere.model.MLocator;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;

public class AMTToolsMProduction extends MProduction implements DocAction, DocOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4781937514934493156L;

	/**	Process Message 			*/
	private String		am_processMsg = null;

	/**	Just Prepared Flag			*/
	private boolean		am_justPrepared = false;

	static CLogger log = CLogger.getCLogger(AMTToolsMProduction.class);

	
	public AMTToolsMProduction(Properties ctx, int M_Production_ID, String trxName) {
		super(ctx, M_Production_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public AMTToolsMProduction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * deleteM_CostDetail
	 * Delete M_CostDetail Lines
	 * @param p_M_Production_ID
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteM_CostDetail(int p_M_Production_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// GetM_CostDetail_ID
		sql = "DELETE FROM M_CostDetail " +
				" WHERE M_Costdetail_ID IN (  " +
				" SELECT mcd.M_CostDetail_ID " +
				" FROM M_CostDetail mcd WHERE mcd.M_Productionline_ID IN ( " +
				" 	SELECT mpl.M_Productionline_ID " +
				" 	FROM M_Production mpr " +
				" 	LEFT JOIN M_Productionline mpl ON (mpl.M_Production_ID = mpr.M_Production_ID) " +
				" 	WHERE mpr.M_Production_ID  = " +p_M_Production_ID + 
				" 	) " +
				" )" ;
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----M_CostDetail for p_M_Production_ID:"+p_M_Production_ID+"  no:"+no+"  DELETED ");
		return no;
	}
	
	/**
	 * deleteM_CostHistory
	 * Delete M_CostHistory Lines
	 * @param p_M_Production_ID
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteM_CostHistory(int p_M_Production_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// M_CostHistory
		sql = "DELETE FROM M_CostHistory " +
				" WHERE M_CostHistory_ID IN (  " +
				"	SELECT mch.M_CostHistory_ID "+
				"	FROM M_CostHistory  mch "+
				"	LEFT JOIN M_CostDetail mcd ON (mch.M_CostDetail_ID = mcd.M_CostDetail_ID) "+
				"	LEFT JOIN M_Productionline mpl ON (mpl.M_Productionline_ID = mcd.M_Productionline_ID) "+
				"	LEFT JOIN M_Production mpr ON (mpr.M_Production_ID = mpl.M_Production_ID) "+
				"	WHERE mpr.M_Production_ID  = " +p_M_Production_ID +
				" )" ;
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----M_CostHistory for p_M_Production_ID:"+p_M_Production_ID+"  no:"+no+"  DELETED ");
		return no;
	}
	
	
	/**
	 * deleteM_Transaction
	 * Delete M_Transaction Lines
	 * @param p_M_Production_ID
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteM_Transaction(int p_M_Production_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// GetM_CostDetail_ID
		sql = "DELETE FROM M_Transaction " +
				" WHERE M_Transaction_ID IN (  " +
				" 	SELECT M_Transaction_ID  " +
				" 	FROM M_Transaction mt " +
				" 	WHERE mt.M_Productionline_ID IN ( " +
				" 		SELECT DISTINCT M_ProductionLine_ID " +
				" 		FROM M_ProductionLine mpl2 " +
				" 		LEFT JOIN M_Production mpr2 ON (mpr2.M_Production_ID = mpl2.M_Production_ID) " +
				" 		WHERE mpr2.M_Production_ID = " +p_M_Production_ID +
				" 	) " +
				" )" ;
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----M_Transaction for p_M_Production_ID:"+p_M_Production_ID+"  no:"+no+"  DELETED ");
		return no;
	
	}
	
	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt(int p_M_Production_ID, String trxName)
	{
		//MProduction mproduction = new MProduction(Env.getCtx(), p_M_Production_ID, trxName);

		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
//log.warning("reactivateIt   am_processMsg:"+am_processMsg);
		
		if (am_processMsg != null)
			return false;
		// Reactivate M_Production
		MPeriod.testPeriodOpen(Env.getCtx(), getMovementDate(), Doc.DOCTYPE_MatProduction, getAD_Org_ID());
		MFactAcct.deleteEx(AMTToolsMProduction.Table_ID, get_ID(), trxName);
		setPosted(false);
		setProcessed(false);
		setDocAction("CL");
		setDocStatus("DR");
		saveEx(trxName);
		// Reactivate M_ProductionLines
		MProductionLine[] lines =  getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MProductionLine line = lines[i];
//log.warning("Product Lines:" +line.getM_Product_ID());
			line.setProcessed(false);
			line.save(get_TrxName());
		}
		// After reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
//log.warning("reactivateIt  am_processMsg:"+am_processMsg);
		if (am_processMsg != null)
			return true;
		return false;

	}	//	reActivateIt
	
	/**
	 * reverseM_StorageonHand
	 * @param p_M_Production_ID
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int reverseM_StorageonHand(int p_M_Production_ID,  Timestamp p_MovementDate, String trxName)
	throws DBException
	{
		int no=0;
		BigDecimal MovementQty = BigDecimal.ZERO;
		BigDecimal QtyUsed =  BigDecimal.ZERO;
		
		AMTToolsMProductionLine[] lines = getAMTLines(p_M_Production_ID, trxName);
		for ( int i = 0; i<lines.length; i++) {			
			// Production Line
			AMTToolsMProductionLine amtproductionline = lines[i];
			// Attribute Settings
			MAttributeSetInstance asi = new MAttributeSetInstance(Env.getCtx(), amtproductionline.getM_AttributeSetInstance_ID(), trxName);
			String asiString = asi.getDescription();
			// Product Attributes
			MProduct mproduct = new MProduct(Env.getCtx(), amtproductionline.getM_Product_ID(), trxName);
			// Ware House / Locator
			MWarehouse mwarehouse = null;
			MLocator mlocator = null;
			// Date Policy
			if ( asiString == null )
				asiString = "";
			Timestamp dateMPolicy = p_MovementDate;
			if(amtproductionline.getM_AttributeSetInstance_ID()>0){
				dateMPolicy = asi.getCreated();
			}
			// Adjust dateMPolicy only Date, No Time values
			dateMPolicy = Util.removeTime(dateMPolicy);
			// Reverse storage on Hand for finished goods
			// 
			if ( amtproductionline.isEndProduct()) {
//log.warning("..Production Header..."+amtproductionline.getLine()+" getMovementQty="+amtproductionline.getMovementQty()+
//		" M_Locator_ID="+amtproductionline.getM_Locator_ID()+"  dateMPolicy="+dateMPolicy+
//		" Product="+amtproductionline.getM_Product_ID()+ "  AttributeSetInstance="+amtproductionline.getM_AttributeSetInstance_ID());
				// Header Movement Qty
				MovementQty = amtproductionline.getPlannedQty();
		 		if (MovementQty.signum() >= 0) 
		 			MovementQty= MovementQty.negate(); 
				// mwarehouse
				mlocator = new MLocator(Env.getCtx(),amtproductionline.getM_Locator_ID(),trxName);
				mwarehouse = new MWarehouse(Env.getCtx(),mlocator.getM_Warehouse_ID(),trxName);
				// FIND OR CREATE MStorageOnHand FOR dateMPolicy
				MStorageOnHand mstorageonhand = MStorageOnHand.getCreate (Env.getCtx(), amtproductionline.getM_Locator_ID(), 
						 amtproductionline.getM_Product_ID(), amtproductionline.getM_AttributeSetInstance_ID(),
						  dateMPolicy, trxName);
				// SUBTRACT MovementQty 
				mstorageonhand.addQtyOnHand(MovementQty);

			} else {
//log.warning("..Production Line...."+amtproductionline.getLine()+"  getMovementQty="+amtproductionline.getMovementQty()+
//		" M_Locator_ID="+amtproductionline.getM_Locator_ID()+"  dateMPolicy="+dateMPolicy+
//		" Product="+amtproductionline.getM_Product_ID()+ "  AttributeSetInstance="+amtproductionline.getM_AttributeSetInstance_ID());
				if (mproduct.isItem()) {
					// Reverse M_StorageonHand Lines
					// mwarehouse
					mlocator = new MLocator(Env.getCtx(),amtproductionline.getM_Locator_ID(),trxName);
					mwarehouse = new MWarehouse(Env.getCtx(),mlocator.getM_Warehouse_ID(),trxName);
					// Get QtyUsed
					QtyUsed = amtproductionline.getQtyUsed();
					// log.warning("2...QtyUsed="+QtyUsed);
						// ADD MStorage On Hand Record
					if (!MStorageOnHand.add(Env.getCtx(), mwarehouse.getM_Warehouse_ID(),
							amtproductionline.getM_Locator_ID(),
							mproduct.getM_Product_ID(), 
							mproduct.getM_AttributeSetInstance_ID(),
							QtyUsed,
							dateMPolicy,
							trxName))
						{
							try {
								raiseError("Cannot correct Inventory", "");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

				} else {
					no = no +1;
				}
			}
		}
		//log.warning("-----updateM_StorageonHand for p_M_Production_ID:"+p_M_Production_ID+"  no:"+no+"  Updated ");
		return no;
	}
	
	/**
	 * updateM_StorageonHand
	 * @param p_M_Production_ID
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int updateM_StorageonHand(int p_M_Production_ID,  Timestamp p_MovementDate, String trxName)
	throws DBException
	{

		int no=0;
		BigDecimal QtyOnHand = BigDecimal.ZERO;
		BigDecimal NewQty = BigDecimal.ZERO;
		BigDecimal MovementQty = BigDecimal.ZERO;
		
		AMTToolsMProductionLine[] lines = getAMTLines(p_M_Production_ID, trxName);
		for ( int i = 0; i<lines.length; i++) {			
			// Production Line
			AMTToolsMProductionLine amtproductionline = lines[i];
			// Attribute Settings
			MAttributeSetInstance asi = new MAttributeSetInstance(Env.getCtx(), amtproductionline.getM_AttributeSetInstance_ID(), trxName);
			String asiString = asi.getDescription();
			// Product Attributes
			MProduct mproduct = new MProduct(Env.getCtx(), amtproductionline.getM_Product_ID(), trxName);
			
			if ( asiString == null )
				asiString = "";
			Timestamp dateMPolicy = p_MovementDate;
			if(amtproductionline.getM_AttributeSetInstance_ID()>0){
				dateMPolicy = asi.getCreated();
			}
			// Adjust dateMPolicy only Date, No Time values
			dateMPolicy = Util.removeTime(dateMPolicy);
			// Reverse storage on Hand for finished goods
			// 
			if ( amtproductionline.isEndProduct()) {
log.warning("..Production Header..."+amtproductionline.getLine()+" getMovementQty="+amtproductionline.getMovementQty()+
		" M_Locator_ID="+amtproductionline.getM_Locator_ID()+"  dateMPolicy="+dateMPolicy+
		" Product="+amtproductionline.getM_Product_ID()+ "  AttributeSetInstance="+amtproductionline.getM_AttributeSetInstance_ID());
				// Reverse M_StorageonHand Header
				// Get QtyOnHand
//				StringBuilder sql2 = new StringBuilder("SELECT DISTINCT QtyOnHand "+
//						" FROM M_StorageOnHand "+
//						" WHERE M_Product_ID = "+ amtproductionline.getM_Product_ID() +
//						" AND M_Locator_ID= "+ amtproductionline.getM_Locator_ID() +
//						" AND M_AttributeSetInstance_ID="+ amtproductionline.getM_AttributeSetInstance_ID() +
//						" AND DateMaterialPolicy='"+dateMPolicy+"'");
//				QtyOnHandDouble = DB.getSQLValue(trxName, sql2.toString());
				MStorageOnHand mstorageonhand = MStorageOnHand.getCreate (Env.getCtx(), amtproductionline.getM_Locator_ID(), 
						 amtproductionline.getM_Product_ID(), amtproductionline.getM_AttributeSetInstance_ID(),
						  dateMPolicy, trxName);
log.warning("mstorageonhand"+mstorageonhand);
				//AMTMStorageOnHand.addAMTQtyOnHand(storage, amtproductionline.getMovementQty().negate(), trxName);
				QtyOnHand = mstorageonhand.getQtyOnHand(); //BigDecimal.valueOf(QtyOnHandDouble);
				NewQty = BigDecimal.ZERO;
				MovementQty = amtproductionline.getMovementQty();
				// log.warning("1...QtyOnHand="+QtyOnHand+"  NewQty="+NewQty+"  MovemntQty="+MovementQty );
		 		if (MovementQty.signum() >= 0) 
		 			NewQty= QtyOnHand.subtract(MovementQty);
		 		else
					NewQty=QtyOnHand.add(MovementQty);
		 		//log.warning("2...QtyOnHand="+QtyOnHand+"  NewQty="+NewQty+"  MovemntQty="+MovementQty );
		 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				 StringBuilder sql = new StringBuilder("UPDATE M_StorageOnHand ");				 		
				 		sql.append(" SET QtyOnHand= "+NewQty);
				 		sql.append(" , Updated = '" + dateFormat.format(cal.getTime()) + "'"+
						" , UpdatedBy= " +Env.getAD_User_ID(Env.getCtx()) +
						" WHERE M_Product_ID = " + amtproductionline.getM_Product_ID() +
						" AND M_Locator_ID= "+ amtproductionline.getM_Locator_ID() +
						" AND M_AttributeSetInstance_ID="+amtproductionline.getM_AttributeSetInstance_ID() +
						" AND DateMaterialPolicy='"+ dateMPolicy +"'");	
				 		//log.warning("sql="+sql.toString());
				no = no + DB.executeUpdateEx(sql.toString(), trxName );
			} else {
log.warning("..Production Line...."+amtproductionline.getLine()+"  getMovementQty="+amtproductionline.getMovementQty()+
		" M_Locator_ID="+amtproductionline.getM_Locator_ID()+"  dateMPolicy="+dateMPolicy+
		" Product="+amtproductionline.getM_Product_ID()+ "  AttributeSetInstance="+amtproductionline.getM_AttributeSetInstance_ID());
				if (mproduct.isItem()) {
					// Reverse M_StorageonHand Lines
					// Get QtyOnHand
//					MStorageOnHand mstorageonhand = MStorageOnHand.get (Env.getCtx(), amtproductionline.getM_Locator_ID(), 
//							 amtproductionline.getM_Product_ID(), amtproductionline.getM_AttributeSetInstance_ID(),
//							  dateMPolicy, trxName);
					MStorageOnHand mstorageonhand = MStorageOnHand.getCreate (Env.getCtx(), amtproductionline.getM_Locator_ID(), 
							 amtproductionline.getM_Product_ID(), amtproductionline.getM_AttributeSetInstance_ID(),
							  dateMPolicy, trxName);
					// UPDATE M_storageonhand
					//AMTMStorageOnHand.addAMTQtyOnHand(storage, amtproductionline.getMovementQty().negate(), trxName);
log.warning("mstorageonhand"+mstorageonhand);
//					if ( mstorageonhand != null) {
//						QtyOnHand = mstorageonhand.getQtyOnHand(); 						
//					} else {
//						StringBuilder sql2 = new StringBuilder("SELECT DISTINCT QtyOnHand "+
//								" FROM M_StorageOnHand "+
//								" WHERE M_Product_ID = "+ amtproductionline.getM_Product_ID() +
//								" AND M_Locator_ID= "+ amtproductionline.getM_Locator_ID() +
//								" AND M_AttributeSetInstance_ID="+ amtproductionline.getM_AttributeSetInstance_ID() +
//								" AND DateMaterialPolicy='"+dateMPolicy+"'");
//						QtyOnHand =  DB.getSQLValueBD(trxName, sql2.toString());
//log.warning("0...sql2="+sql2.toString() );
//					}
log.warning("1...QtyOnHand="+QtyOnHand+"  NewQty="+NewQty+"  MovemntQty="+MovementQty );
					NewQty = BigDecimal.ZERO;
					MovementQty = amtproductionline.getMovementQty();
					// log.warning("1...QtyOnHand="+QtyOnHand+"  NewQty="+NewQty+"  MovemntQty="+MovementQty );
			 		if (MovementQty.signum() >= 0) 
			 			NewQty= QtyOnHand.add(MovementQty);
			 		else
						NewQty=QtyOnHand.subtract(MovementQty);
log.warning("2...QtyOnHand="+QtyOnHand+"  NewQty="+NewQty+"  MovemntQty="+MovementQty );
			 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					 StringBuilder sql = new StringBuilder("UPDATE M_StorageOnHand ");				 		
					 		sql.append(" SET QtyOnHand= "+NewQty);
					 		sql.append(" , Updated = '" + dateFormat.format(cal.getTime()) + "'"+
							" , UpdatedBy= " +Env.getAD_User_ID(Env.getCtx()) +
							" WHERE M_Product_ID = " + amtproductionline.getM_Product_ID() +
							" AND M_Locator_ID= "+ amtproductionline.getM_Locator_ID() +
							" AND M_AttributeSetInstance_ID="+amtproductionline.getM_AttributeSetInstance_ID() +
							" AND DateMaterialPolicy='"+ dateMPolicy +"'");	
					 		//log.warning("sql="+sql.toString());
					no = no + DB.executeUpdateEx(sql.toString(), trxName );
				} else {
					no = no +1;
				}
			}
		}
		//log.warning("-----updateM_StorageonHand for p_M_Production_ID:"+p_M_Production_ID+"  no:"+no+"  Updated ");
		return no;
	}
	
	/**
	 * getAMTLines
	 * @param p_M_Production_ID
	 * @param trxName
	 * @return
	 */
	
	public static AMTToolsMProductionLine[] getAMTLines( int p_M_Production_ID, String trxName) {
		
		ArrayList<AMTToolsMProductionLine> list = new ArrayList<AMTToolsMProductionLine>();
		
		String sql = "SELECT pl.M_ProductionLine_ID "
			+ "FROM M_ProductionLine pl "
			+ "WHERE pl.M_Production_ID = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql,trxName);
			pstmt.setInt(1, p_M_Production_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add( new AMTToolsMProductionLine( Env.getCtx(), rs.getInt(1), trxName ) );	
		}
		catch (SQLException ex)
		{
			throw new AdempiereException("Unable to load production lines", ex);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		AMTToolsMProductionLine[] retValue = new AMTToolsMProductionLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	/**
	 * raiseError
	 * @param string
	 * @param sql
	 * @throws Exception
	 */
	private static void raiseError(String string, String sql) throws Exception {
		StringBuilder msg = new StringBuilder(string);
		ValueNamePair pp = CLogger.retrieveError();
		if (pp != null)
			msg = new StringBuilder(pp.getName()).append(" - ");
		msg.append(sql);
		throw new AdempiereUserError (msg.toString());
	}
}
