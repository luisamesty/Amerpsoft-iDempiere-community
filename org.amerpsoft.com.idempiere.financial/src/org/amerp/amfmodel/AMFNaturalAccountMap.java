package org.amerp.amfmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Util;

public class AMFNaturalAccountMap<K,V> extends CCache<K,V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8923322992270411218L;
	
	/** Context			*/
	private Properties	m_ctx = null;
	/** Transaction		*/
	private String		m_trxName = null;
	/** Map of Values and Element	*/
	private HashMap<String,MAMF_ElementValue> 	m_valueMap = new HashMap<String,MAMF_ElementValue>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AMFNaturalAccountMap.class);

	/**
	 *  Constructor.
	 *  Parse File does the processing
	 *  @param ctx context
	 *	@param trxName transaction
	 */
	public AMFNaturalAccountMap(Properties ctx, String trxName)
	{
		super(null, "NaturalAccountMap", 100, false);
		m_ctx = ctx;
		m_trxName = trxName;
	}   //  NaturalAccountMap
	
	

	/**
	 *  Read and Parse File
	 * 	@param file Accounts file
	 *  @return error message or "" if OK
	 */
	public String parseFile (File file)
	{
		if (log.isLoggable(Level.CONFIG)) log.config(file.getAbsolutePath());
		String line = null;
		try
		{
			//  see FileImport
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), Ini.getCharset()), 10240);
			//	not safe see p108 Network pgm
			String errMsg = "";

			//  read lines
			int lineNo= 1;
			while ((line = in.readLine()) != null && errMsg.length() == 0) {
				errMsg = parseLine(line, lineNo);
//log.warning("parseFile line="+line);
				lineNo++;
			}
			line = null;
			in.close();

			//  Error
			if (errMsg.length() != 0)
				return errMsg;
		}
		catch (Exception ioe)
		{
			String s = ioe.getLocalizedMessage();
			if (s == null || s.length() == 0)
				s = ioe.toString();
			return "Parse Error: Line=" + line + " - " + s;
		}
		return "";
	}   //  parse

	/**
	 *  Create Account Entry for Default Accounts only.
	 *  @param line line with info
	 *  Line format (9 fields)
	 *   1	A   [Account Value]
	 *   2	B   [Account Name]
	 *   3	C   [Description]
	 *   4	D   [Account Type]
	 *   5	E   [Account Sign]
	 *   6	F   [Document Controlled]
	 *   7	G   [Summary Account]
	 * 	 8	H   [Default_Account]
	 * 	 9	I   [Parent Value] - ignored
	 *
	 *  @return error message or "" if OK
	 *  @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String parseLine (String line, int lineNo) throws Exception
	{
//log.warning("Start lineNo="+lineNo+"  line="+line);

		if (log.isLoggable(Level.CONFIG)) log.config(lineNo+" : "+line);

		if (line.trim().length()==0) {
			log.log(Level.WARNING, "Line "+lineNo+" is empty, ignored. ");
			return "";
		}
		
		//  Fields with ',' are enclosed in "
		StringBuilder newLine = new StringBuilder();
		StringTokenizer st = new StringTokenizer(line, "\"", false);
		if ((st==null )||(st.countTokens()==0)) {
			log.log(Level.SEVERE, "Parse error: No \\\" found in line: "+lineNo);
			return "";
		}
		newLine.append(st.nextToken());         //  first part
		while (st.hasMoreElements())
		{
			String s = st.nextToken();          //  enclosed part
			newLine.append(s.replace(',',' ')); //  remove ',' with space
			if (st.hasMoreTokens())
				newLine.append(st.nextToken()); //  unenclosed
		}
		//  add space at the end        - tokenizer does not count empty fields
		newLine.append(" ");

		//  Parse Line - replace ",," with ", ,"    - tokenizer does not count empty fields
		String pLine = Util.replace(newLine.toString(), ",,", ", ,");
		pLine = Util.replace(pLine, ",,", ", ,");
		st = new StringTokenizer(pLine, ",", false);
		//  All fields there ?
		if (st.countTokens() == 1)
		{
			log.log(Level.SEVERE, "Ignored: Require ',' as separator - " + pLine);
			return "";
		}
		if (st.countTokens() < 9)
		{
			log.log(Level.SEVERE, "Ignored: FieldNumber wrong: " + st.countTokens() + " - " + pLine);
			return "";
		}

		//  Fill variables
		String Value = null, Name = null, Description = null,
			AccountType = null, AccountSign = null, IsDocControlled = null,
			IsSummary = null, Default_Account = null, Parent_Value=null;
		//
		for (int i = 0; i < 9 && st.hasMoreTokens(); i++)
		{
			String s = st.nextToken().trim();
			//  Ignore, if is it header line
			if (s == null)
				s = "";
			if (s.startsWith("[") && s.endsWith("]"))
				return "";
			//
			if (i == 0)			//	A - Value
				Value = s;
			else if (i == 1)	//	B - Name
				Name = s;
			else if (i == 2)	//	C - Description
				Description = s;
			else if (i == 3)	//	D - Type
				AccountType = s.length()>0 ? String.valueOf(s.charAt(0)) : "E";
			else if (i == 4)	//	E - Sign
				AccountSign = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 5)	//	F - DocControlled
				IsDocControlled = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 6)	//	G - IsSummary
				IsSummary = s.length()>0 ? String.valueOf(s.charAt(0)) : "N";
			else if (i == 7)	//	H - Default_Account
				Default_Account = s;
			else if (i == 8)	//	I - Parent_Value
				Parent_Value = s;
		}

		//	Ignore if Value & Name are empty (no error message)
		if ((Value == null || Value.length() == 0) && (Name == null || Name.length() == 0))
			return "";
		//	Summary Account
		if (IsSummary == null || IsSummary.length() == 0)
			IsSummary = "N";
		//  Validation
		if (AccountType == null || AccountType.length() == 0)
			AccountType = "E";
		if (AccountSign == null || AccountSign.length() == 0)
			AccountSign = "N";
		if (IsDocControlled == null || IsDocControlled.length() == 0)
			IsDocControlled = "N";
		if (Name == null)
			Name="Account "+Value;
		if (Description == null)
			Description=Name;
//log.warning("LineNo="+lineNo+"  Value=" + Value + ", AcctType=" + AccountType
//		+ ", Sign=" + AccountSign + ", Doc=" + IsDocControlled
//		+ ", Summary=" + IsSummary + ", Parent_Value="+Parent_Value+" - " + Name 
//		+ " - " + Description+ "\r\n");

//		AMFAccountingClientSetup.setM_info(Message);
		try
		{
			//	Try to find - allows to use same natural account for multiple default accounts 
			MAMF_ElementValue na = (MAMF_ElementValue)m_valueMap.get(Value);
			if (na == null)
			{
				//  Create Account - save later
				na = new MAMF_ElementValue(m_ctx, Value, Name, Description,
					AccountType, AccountSign,
					IsDocControlled.toUpperCase().startsWith("Y"), 
					IsSummary.toUpperCase().startsWith("Y"), m_trxName, Parent_Value);
				m_valueMap.put(Value, na);
			}
			
			//  Add to Cache
			put((K)Value.toUpperCase(), (V)na);
		}
		catch (Exception e)
		{
			return (e.getMessage());
		}

		return "";
	}   //  parseLine

	/**
	 *  Save all Accounts
	 *
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 * 	@param C_Element_ID element
	 *  @param isActive 
	 * 	@return true if created
	 */
	public boolean saveAccounts (int AD_Client_ID, int AD_Org_ID, int C_Element_ID, boolean isActive)
	{
		log.config("");
		int C_Elementparent_ID =  0;
		Iterator<?> iterator = this.values().iterator();
		while (iterator.hasNext())
		{
			MAMF_ElementValue na = (MAMF_ElementValue)iterator.next();
//log.warning(" Cuenta:"+na.getValue()+"-"+na.getName());
			// Parent Account ID
			if (na.getValue_Parent() != null) {
				MAMF_ElementValue napar = findByMElementValue(Env.getCtx(), AD_Client_ID, C_Element_ID, na.getValue_Parent());
				if (napar != null) 
					C_Elementparent_ID =  napar.getC_ElementValue_ID();
				else
					C_Elementparent_ID =  0;
			} else {
				C_Elementparent_ID =  0;
			}
			MAMF_ElementValue nanew = findByMElementValue(Env.getCtx(), AD_Client_ID, C_Element_ID, na.getValue());
			if (nanew == null) {
log.warning(" Cuenta nueva.....:"+na.getValue()+"-"+na.getName());
				nanew = new MAMF_ElementValue(Env.getCtx(),0,null);
				nanew.setAD_Org_ID(AD_Org_ID);
				nanew.setC_Element_ID(C_Element_ID);
				nanew.setIsActive(isActive);		
				nanew.setName(na.getName());
				nanew.setDescription(na.getDescription());
				nanew.setIsSummary(na.isSummary());
				nanew.setValue(na.getValue());
				nanew.setIsDocControlled(na.isDocControlled());
				nanew.setAccountSign(na.getAccountSign());
				nanew.setAccountType(na.getAccountType());
				nanew.setValue_Parent(na.getValue_Parent());
				nanew.setC_Elementparent_ID(C_Elementparent_ID);
				nanew.save();
			} else {
log.warning(" Cuenta existe.....:"+na.getValue()+"-"+na.getName());				
				nanew.setIsActive(isActive);		
				nanew.setName(na.getName());
				nanew.setIsSummary(na.isSummary());
				nanew.setValue(na.getValue());
				nanew.setDescription(na.getDescription());
				nanew.setIsDocControlled(na.isDocControlled());
				nanew.setAccountSign(na.getAccountSign());
				nanew.setAccountType(na.getAccountType());
				nanew.setValue_Parent(na.getValue_Parent());
				nanew.setC_Elementparent_ID(C_Elementparent_ID);
				nanew.save();
			}
		}
//		AMFAccountingClientSetup.setM_info(acctMessage);
		return true;
	}   //  saveAccounts

	/**
	 *  Get ElementValue
	 * 	@param key key
	 *  @return 0 if error
	 */
	public int getC_ElementValue_ID (String key)
	{
		MAMF_ElementValue na = (MAMF_ElementValue)this.get(key);
		if (na == null)
			return 0;
		return na.getC_ElementValue_ID();
	}   //  getC_ElementValue_ID
	
	public static MAMF_ElementValue findByMElementValue(Properties ctx, int AD_Client_ID, int C_Element_ID, String ElementValue) {
			
		MAMF_ElementValue retValue = null;
		int C_ElementValue_ID=0;
		String sql = "SELECT C_ElementValue_ID "
			+ "FROM C_ElementValue "
			+ "WHERE AD_Client_ID="+ AD_Client_ID
			+ " AND C_Element_ID="+ C_Element_ID
			+ " AND Value='"+ElementValue.trim()+"'"
			;        
//log.warning("sql="+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				C_ElementValue_ID = rs.getInt(1);
//log.warning("ElementValue="+ElementValue+"  C_ElementValue_ID="+C_ElementValue_ID);
				retValue = new MAMF_ElementValue(ctx, C_ElementValue_ID, null);
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
}
