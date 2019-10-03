/**
 * 
 */
package org.amerp.amxeditor.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.util.*;
import org.amerp.amxeditor.model.MMunicipality;


/**
 * @author luisamesty
 *
 */
public class MMunicipality extends X_C_Municipality implements DocAction {
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	/**	Municipality Cache				*/
	private static CCache<String,MMunicipality> s_Municipalitys = null;
	/** Default Municipality				*/
	private static MMunicipality		s_default = null;
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MMunicipality.class);

	/**
	 * 	Load Municipalitys (cached)
	 *	@param ctx context
	 */
	private static void loadAllMunicipalitys (Properties ctx)
	{
		s_Municipalitys = new CCache<String,MMunicipality>("C_Municipality", 500);
		String sql = "SELECT * FROM C_Municipality WHERE IsActive='Y'";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				MMunicipality r = new MMunicipality (ctx, rs, null);
				s_Municipalitys.put(String.valueOf(r.getC_Municipality_ID()), r);
				if (r.isDefault())
					s_default = r;
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		s_log.fine(s_Municipalitys.size() + " - default=" + s_default);
	}	//	loadAllMunicipalitys

	/**
	 * 	Get Country (cached)
	 * 	@param ctx context
	 *	@param C_Municipality_ID ID
	 *	@return Country
	 */
	public static MMunicipality get (Properties ctx, int C_Municipality_ID)
	{
		if (s_Municipalitys == null || s_Municipalitys.size() == 0)
			loadAllMunicipalitys(ctx);
		String key = String.valueOf(C_Municipality_ID);
		MMunicipality r = (MMunicipality)s_Municipalitys.get(key);
		if (r != null)
			return r;
		r = new MMunicipality (ctx, C_Municipality_ID, null);
		if (r.getC_Municipality_ID() == C_Municipality_ID)
		{
			s_Municipalitys.put(key, r);
			return r;
		}
		return null;
	}	//	get

	/**
	 * 	Get Default Municipality
	 * 	@param ctx context
	 *	@return Municipality or null
	 */
	public static MMunicipality getDefault (Properties ctx)
	{
		if (s_Municipalitys == null || s_Municipalitys.size() == 0)
			loadAllMunicipalitys(ctx);
		return s_default;
	}	//	get
	/**
	 *	Return Municipalitys as Array
	 * 	@param ctx context
	 *  @return MCountry Array
	 */
	@SuppressWarnings("unchecked")
	public static MMunicipality[] getMunicipalitys(Properties ctx)
	{
		if (s_Municipalitys == null || s_Municipalitys.size() == 0)
			loadAllMunicipalitys(ctx);
		MMunicipality[] retValue = new MMunicipality[s_Municipalitys.size()];
		s_Municipalitys.values().toArray(retValue);
		Arrays.sort(retValue, new MMunicipality(ctx, 0, null));
		return retValue;
	}	//	getMunicipalitys

	/**
	 *	Return Array of Municipalitys of Country
	 * 	@param ctx context
	 *  @param C_Country_ID country
	 *  @return MMunicipality Array
	 */
	@SuppressWarnings("unchecked")
	public static MMunicipality[] getMunicipalitys (Properties ctx, int C_Region_ID)
	{
		if (s_Municipalitys == null || s_Municipalitys.size() == 0)
			loadAllMunicipalitys(ctx);
		ArrayList<MMunicipality> list = new ArrayList<MMunicipality>();
		Iterator it = s_Municipalitys.values().iterator();
		while (it.hasNext())
		{
			MMunicipality r = (MMunicipality)it.next();
			if (r.getC_Region_ID() == C_Region_ID)
				list.add(r);
		}
		//  Sort it
		MMunicipality[] retValue = new MMunicipality[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue, new MMunicipality(ctx, 0, null));
		return retValue;
	}	//	getMunicipalitys

	/**
	 * @param p_ctx
	 * @param C_Municipality_ID
	 * @param p_trxName
	 */
    public MMunicipality(Properties p_ctx, int C_Municipality_ID, String p_trxName) {
	    super(p_ctx, C_Municipality_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MMunicipality(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#setDocStatus(java.lang.String)
	 */
    @Override
    public void setDocStatus(String p_newStatus) {
	    // TODO Auto-generated method stub
	    
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDocStatus()
	 */
    @Override
    public String getDocStatus() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#processIt(java.lang.String)
	 */
    @Override
    public boolean processIt(String p_action) throws Exception {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#unlockIt()
	 */
    @Override
    public boolean unlockIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#invalidateIt()
	 */
    @Override
    public boolean invalidateIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#prepareIt()
	 */
    @Override
    public String prepareIt() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#approveIt()
	 */
    @Override
    public boolean approveIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#rejectIt()
	 */
    @Override
    public boolean rejectIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#completeIt()
	 */
    @Override
    public String completeIt() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#voidIt()
	 */
    @Override
    public boolean voidIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#closeIt()
	 */
    @Override
    public boolean closeIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#reverseCorrectIt()
	 */
    @Override
    public boolean reverseCorrectIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#reverseAccrualIt()
	 */
    @Override
    public boolean reverseAccrualIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#reActivateIt()
	 */
    @Override
    public boolean reActivateIt() {
	    // TODO Auto-generated method stub
	    return false;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getSummary()
	 */
    @Override
    public String getSummary() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDocumentNo()
	 */
    @Override
    public String getDocumentNo() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDocumentInfo()
	 */
    @Override
    public String getDocumentInfo() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#createPDF()
	 */
    @Override
    public File createPDF() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getProcessMsg()
	 */
    @Override
    public String getProcessMsg() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDoc_User_ID()
	 */
    @Override
    public int getDoc_User_ID() {
	    // TODO Auto-generated method stub
	    return 0;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getC_Currency_ID()
	 */
    @Override
    public int getC_Currency_ID() {
	    // TODO Auto-generated method stub
	    return 0;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getApprovalAmt()
	 */
    @Override
    public BigDecimal getApprovalAmt() {
	    // TODO Auto-generated method stub
	    return null;
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.DocAction#getDocAction()
	 */
    @Override
    public String getDocAction() {
	    // TODO Auto-generated method stub
	    return null;
    }



}
