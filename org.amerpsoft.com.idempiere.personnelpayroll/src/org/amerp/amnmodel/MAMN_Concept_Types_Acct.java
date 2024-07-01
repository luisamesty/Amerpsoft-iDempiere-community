package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MAMN_Concept_Types_Acct extends X_AMN_Concept_Types_Acct{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8845631871563970011L;

	static CLogger log = CLogger.getCLogger(MAMN_Concept_Types_Acct.class);

	public MAMN_Concept_Types_Acct(Properties ctx,
			int AMN_Concept_Types_Acct_ID, String trxName) {
		super(ctx, AMN_Concept_Types_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Concept_Types_Acct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	findAMNConceptTypesAcct
	 * @param ctx
	 * @param AMN_Concept_Types_ID
	 * @param C_AcctSchema_ID
	 * @return
	 */
	 
	 public static MAMN_Concept_Types_Acct findAMNConceptTypesAcct(Properties ctx, 
				int AMN_Concept_Types_ID,  int C_AcctSchema_ID) {
				
		MAMN_Concept_Types_Acct retValue = null;
		String sql = "SELECT AMN_Concept_Types_Acct_ID, "+
				" AMN_Concept_Types_ID, C_AcctSchema_ID " + 
				" FROM AMN_Concept_Types_Acct " + 
				" WHERE AMN_Concept_Types_ID=" + AMN_Concept_Types_ID +
				" AND C_AcctSchema_ID=" + C_AcctSchema_ID ;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//log.warning("sql="+sql);
		try
		{
			pstmt = DB.prepareStatement(sql, null);
  
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				MAMN_Concept_Types_Acct amnconcepttypesacct = new MAMN_Concept_Types_Acct(ctx, rs.getInt(1), null);
				//log.warning("MAMN_Concept_Types_Acct_ID="+rs.getInt(1));
				if (amnconcepttypesacct.isActive())
					retValue = amnconcepttypesacct;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}

	/**
	 * Copy copyAccountsFrom settings from another ConceptTypes
	 * overwrites existing data
	 * @param source 
	 */
	public void copyAccountsFrom (int SourceAcctSchema_ID, MAMN_Concept_Types_Acct source)
	{

		if (log.isLoggable(Level.FINE))log.log(Level.FINE, "Copying from:" + source + ", to: " + this);
		// CRE
		setAMN_Cre_Acct(source.getAMN_Cre_Acct());
		setAMN_Cre_DW_Acct(source.getAMN_Cre_DW_Acct());
		setAMN_Cre_IW_Acct(source.getAMN_Cre_IW_Acct());
		setAMN_Cre_SW_Acct(source.getAMN_Cre_SW_Acct());
		setAMN_Cre_MW_Acct(source.getAMN_Cre_MW_Acct());
		// DEB
		setAMN_Deb_Acct(source.getAMN_Deb_Acct());
		setAMN_Deb_DW_Acct(source.getAMN_Deb_DW_Acct());
		setAMN_Deb_IW_Acct(source.getAMN_Deb_IW_Acct());
		setAMN_Deb_SW_Acct(source.getAMN_Deb_SW_Acct());
		setAMN_Deb_MW_Acct(source.getAMN_Deb_MW_Acct());
		saveEx();
		
	}
}
