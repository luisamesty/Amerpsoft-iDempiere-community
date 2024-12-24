package org.amerp.amnmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBPartner;
import org.compiere.model.MClientInfo;
import org.compiere.model.MFactAcct;
import org.compiere.model.MJournal;
import org.compiere.model.MNote;
import org.compiere.model.MPeriod;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MAMN_Leaves  extends X_AMN_Leaves implements DocAction, DocOptions {

	private static final long serialVersionUID = -7169854017581281267L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves.class);
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;
	/* Is reactivating isRecativating  **/
	private boolean		isReactivating = false;
	/** Persistent Object			*/
	private PO					m_po = null;
	
	public static final String AMN_Leaves_None = "--";
	public static final String AMN_Leaves_Draft = "DR"; 		//	DR Draft - Borrador	Emitida por el Usuario, o en condición de Devuelta
	public static final String AMN_Leaves_Supervisor = "SU"; 	//	SU  Revisión Supervisor	En revisión por el Supervisor del Trabajador
	public static final String AMN_Leaves_Rejected = "SR"; 		//	SR Solicitud Rechazada	Rechazada por el Supervisor del Trabajador
	public static final String AMN_Leaves_HHRR = "RH"; 			//	RH Revisión Recursos Humanos	En Revisión por Recursos Humanos
	public static final String AMN_Leaves_HHRR_Rejected = "RR";	//	RR Rechazada RRHH	Rechazada por Recursos Humanos
	public static final String AMN_Leaves_Filed = "AR";			//	AR Archivo.	Archivo
	public static final String AMN_Leaves_Complete = "CO";		// 	CO Completada	Completada
	public static final String AMN_Leaves_Close = "CL";			// 	CL Closed 
		
	/** Drafted = DR */
	private static final String DOCSTATUS_Drafted = "DR";

	private static final String DOCACTION_Prepare = "PR";

	private static final String DOCACTION_Close = "CL";
	
	public MAMN_Leaves(Properties ctx, int AMN_Leaves_ID, String trxName) {
		super(ctx, AMN_Leaves_ID, trxName);
		// 
	}
	
	public MAMN_Leaves(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	@Override
	public boolean processIt(String action) throws Exception {
		log.warning("===============processIt================================");
		if (isReactivating) {
			setProcessed(false);
			setDocStatus(DOCSTATUS_Drafted);
			isReactivating = false;
			return true;
		}
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());
	}

	@Override
		public boolean reActivateIt() {
		log.warning("===============reActivateIt================================");
		isReactivating= true;
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		//MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		setProcessed(false);
		setIsApproved(false);
		setDocStatus(DOCSTATUS_Drafted);
		setDocAction(DOCACTION_Prepare);
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		return true;
		}


	@Override
	public String prepareIt() {
		log.warning("===============prepareIt================================");
		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean approveIt() {
		log.warning("===============approveIt================================");
		setIsApproved(true);
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		log.warning("===============completeIt================================");
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	@Override
	public String getDocumentNo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stubo
		return null;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
    	//log.warning("=====================customizeValidActions===========================");
    	if (options == null)
    		throw new IllegalArgumentException("Option array parameter is null");
    	if (docAction == null)
    		throw new IllegalArgumentException("Doc action array parameter is null");

    	// If a document is drafted or invalid, the users are able to complete, prepare or void
    	if (docStatus.equals(DocumentEngine.STATUS_Drafted) || docStatus.equals(DocumentEngine.STATUS_Invalid)) {
    		options[index++] = DocumentEngine.ACTION_Complete;
    		options[index++] = DocumentEngine.ACTION_Prepare;
    		options[index++] = DocumentEngine.ACTION_Void;

    		// If the document is already completed, we also want to be able to reactivate or void it instead of only closing it
    	} else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
    		options[index++] = DocumentEngine.ACTION_Void;
    		options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
    		options[index++] = DocumentEngine.ACTION_Reverse_Correct;
    		options[index++] = DocumentEngine.ACTION_ReActivate;
    	}

    	return index;

	}

	@Override
	public String getDocAction() {
		// 
		return this.getDocAction();
	}
	
	private void setDocAction(String docactionPrepare) {
		// 
		
	}
	
	/**
	 * getAMWWorkFlowNodeFromDocStatusSQL
	 *  Returns Work Fflow Node from a given Work Flow and Node Value DR,CO,CL...
	 * @param AMW_WorkFlow_ID
	 * @param DocStatus
	 * @return
	 */
	public MAMW_WF_Node getAMWWorkFlowNodeFromDocStatusSQL (int AMW_WorkFlow_ID, String DocStatus )
	{
		MAMW_WF_Node retValue = null;
		String sql= "SELECT DISTINCT *  "+
				" FROM AMw_WF_Node "+  
				" WHERE AMW_WorkFlow_ID=? "+ 
				" AND Value = '"+ DocStatus + "' " +
				" ORDER BY SeqNo ASC " ;
		ArrayList<MAMW_WF_Node> list = new ArrayList<MAMW_WF_Node>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, AMW_WorkFlow_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MAMW_WF_Node(getCtx(), rs, get_TrxName());
				//log.warning("MAMW_WF_Node Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		return retValue;
	}
	
}
