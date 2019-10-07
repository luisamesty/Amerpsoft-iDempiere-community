package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCurrency;
import org.compiere.model.MJournal;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Reference;
import org.compiere.model.X_C_ConversionType;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMFCurrencyConversionRateCopy extends SvrProcess  {

	static CLogger log = CLogger.getCLogger(AMFCurrencyConversionRateCopy.class);

	private int p_AD_Client_ID = 0;
	private int p_C_Currency_ID = 0;
	private int p_C_Currency_ID_TO1=0;
	private int p_C_Currency_ID_TO2 = 0;
	private int C_ConversionType_ID = 0;
	String MessagetoShow ="";
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Currency_ID")) 
				p_C_Currency_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Currency_ID_TO1")) 
				p_C_Currency_ID_TO1 =  para.getParameterAsInt();
			else if (paraName.equals("C_Currency_ID_TO2")) 
				p_C_Currency_ID_TO2 =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		int C_Conversion_Rate_ID = 0;
		int C_Conversion_Rate_ID_TO = 0;
		MCurrency curr = new MCurrency(Env.getCtx(),p_C_Currency_ID, null );
		MCurrency currto1 = new MCurrency(Env.getCtx(),p_C_Currency_ID_TO1, null );
		MCurrency currto2 = new MCurrency(Env.getCtx(),p_C_Currency_ID_TO2, null );
		BigDecimal ConvRate1To2 = BigDecimal.ZERO;
		BigDecimal NewMultiply = BigDecimal.ONE;
		BigDecimal NewDivide = BigDecimal.ONE;
		MConversionRate currate = null;
		MConversionRate currateto = null;
		MConversionRate currate1to2 = null;
		// Get C_ConvertionType_ID where value = 'S' Spot
		C_ConversionType_ID = ((X_C_ConversionType)new Query(Env.getCtx(),X_C_ConversionType.Table_Name,"Value='S'",null).first()).getC_ConversionType_ID();
		// Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// Get Currency Conversion From AD_Client_ID and  C_Currency TO C_Currency_TO1_ID
		StringBuilder sql = new StringBuilder("SELECT ")
		.append(" C_Conversion_Rate_ID, AD_Client_ID, AD_Org_id, isActive, ")
		.append(" C_Currency_id, C_Currency_id_to, ")
	    .append("  validfrom, validto, multiplyrate, dividerate, C_ConversionType_ID ")
	    .append(" FROM C_Conversion_Rate ")
	    .append(" WHERE AD_Client_ID = "+p_AD_Client_ID)
	    .append(" AND C_Currency_ID ="+p_C_Currency_ID)
	    .append(" AND C_Currency_ID_TO ="+p_C_Currency_ID_TO1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				C_Conversion_Rate_ID = rs.getInt(1);
				currate = new MConversionRate(Env.getCtx(), C_Conversion_Rate_ID, null);
				currateto = findC_Conversion_Rate_ExactDates(p_AD_Client_ID, p_C_Currency_ID,
						p_C_Currency_ID_TO2,  rs.getTimestamp(7),  rs.getTimestamp(8), rs.getInt(11));
				MessagetoShow= C_Conversion_Rate_toString(currate);
				addLog(MessagetoShow);
				// Conversion rate Between Curency1 and Currency 2
				currate1to2= findC_Conversion_Rate_RangeDates(p_AD_Client_ID, p_C_Currency_ID_TO1, 
						p_C_Currency_ID_TO2, rs.getTimestamp(7),  rs.getTimestamp(8), C_ConversionType_ID);
				if ( currate1to2 == null) {
					ConvRate1To2 = BigDecimal.ONE;
					MessagetoShow= Msg.translate(Env.getCtx(),"ConversionRateCommandError") +" "+
					Msg.translate(Env.getCtx(),"From") +" "+currto1.getISO_Code()+" "+currto1.getCurSymbol()+" "+
					Msg.translate(Env.getCtx(),"To") +" "+currto2.getISO_Code()+" "+currto2.getCurSymbol();
					addLog(MessagetoShow);
				} else {
					MessagetoShow= C_Conversion_Rate_toString(currate1to2);
					addLog(MessagetoShow);
					ConvRate1To2 = currate1to2.getMultiplyRate();
					//if (ConvRate1To2.compareTo(BigDecimal.ZERO) >= 0){
						NewMultiply = (currate.getMultiplyRate());
						NewMultiply = NewMultiply.multiply(ConvRate1To2).setScale(as.getCostingPrecision()+4, BigDecimal.ROUND_HALF_UP);
						NewDivide = currate.getDivideRate();
						NewDivide = NewDivide.multiply(ConvRate1To2).setScale(as.getCostingPrecision()+4, BigDecimal.ROUND_HALF_UP);
//					}  else {
//						NewMultiply = currate.getMultiplyRate();
//						NewDivide = currate.getDivideRate();
//					}
					if ( currateto == null) {
//log.warning("NO hay.....NewMultiply="+NewMultiply+"  NewDivide="+NewDivide);						
						currateto = new MConversionRate(Env.getCtx(), 0, null);
						currateto.setAD_Org_ID(currate.getAD_Org_ID());
						currateto.setC_ConversionType_ID(currate.getC_ConversionType_ID());
						currateto.setC_Currency_ID(currate.getC_Currency_ID());
						currateto.setC_Currency_ID_To(p_C_Currency_ID_TO2);
						currateto.setValidFrom(currate.getValidFrom());
						currateto.setValidTo(currate.getValidTo());
						currateto.setMultiplyRate(NewMultiply);
						currateto.save();
						MessagetoShow= Msg.translate(Env.getCtx(),"Inserted") +" "+
								C_Conversion_Rate_toString(currateto);
						addLog(MessagetoShow);			
						
					} else {
//log.warning("SI hay.....NewMultiply="+NewMultiply+"  NewDivide="+NewDivide);
						currateto.setMultiplyRate(NewMultiply);
						currateto.save();
						MessagetoShow= Msg.translate(Env.getCtx(),"Updated") +" "+
								C_Conversion_Rate_toString(currateto);
						addLog(MessagetoShow);			
					}
				}
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return null;
	}
	
	/**
	 * findC_Conversion_Rate_ExactDates
	 * @param AD_Client_ID
	 * @param C_Currency_ID
	 * @param C_Currency_id_to
	 * @param validfrom
	 * @param validto
	 * @param C_ConversionType_ID
	 * @return
	 */
	public static MConversionRate findC_Conversion_Rate_ExactDates( int AD_Client_ID, int C_Currency_ID, 
			int C_Currency_id_to , Timestamp validfrom, Timestamp validto, int C_ConversionType_ID) {
		MConversionRate retValue = null;
		int C_Conversion_Rate_ID = 0;
		StringBuilder sql = new StringBuilder("SELECT DISTINCT")
		.append(" C_Conversion_Rate_ID ")
	    .append(" FROM C_Conversion_Rate ")
	    .append(" WHERE AD_Client_ID = "+AD_Client_ID)
	    .append(" AND C_Currency_ID ="+C_Currency_ID)
	    .append(" AND C_Currency_ID_TO ="+C_Currency_id_to)
	    .append(" AND C_ConversionType_ID ="+C_ConversionType_ID)
	    .append(" AND Validfrom='"+validfrom+"'")
	    .append(" AND Validto='"+validto+"'");

		C_Conversion_Rate_ID= DB.getSQLValue(null, sql.toString());
		if (C_Conversion_Rate_ID > 0)
			retValue = new MConversionRate(Env.getCtx(), C_Conversion_Rate_ID, null);
		return retValue;
	}
	/**
	 *  findC_Conversion_Rate_RangeDates
	 * @param AD_Client_ID
	 * @param C_Currency_ID
	 * @param C_Currency_id_to
	 * @param validfrom
	 * @param validto
	 * @return
	 */
	public static MConversionRate findC_Conversion_Rate_RangeDates( int AD_Client_ID, int C_Currency_ID_from, 
			int C_Currency_id_to , Timestamp validfrom, Timestamp validto, int C_ConversionType_ID) {
		MConversionRate retValue = null;
		int C_Conversion_Rate_ID = 0;
		String mess ="";
		StringBuilder sql = new StringBuilder("SELECT DISTINCT")
		.append(" C_Conversion_Rate_ID ")
	    .append(" FROM C_Conversion_Rate ")
	    .append(" WHERE AD_Client_ID = "+AD_Client_ID)
	    .append(" AND C_Currency_ID ="+C_Currency_ID_from)
	    .append(" AND C_Currency_ID_TO ="+C_Currency_id_to)
	    .append(" AND C_ConversionType_ID ="+C_ConversionType_ID)
	    .append(" AND Validfrom<='"+validfrom+"'")
	    .append(" AND Validto>='"+validto+"'");
//log.warning("sql="+sql.toString());
		C_Conversion_Rate_ID= DB.getSQLValue(null, sql.toString());
		if (C_Conversion_Rate_ID > 0) {
			retValue = new MConversionRate(Env.getCtx(), C_Conversion_Rate_ID, null);
			mess=C_Conversion_Rate_toString(retValue);
//log.warning(mess);
		}
		return retValue;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public static String C_Conversion_Rate_toString(MConversionRate convrate)
	{
		MCurrency curr = new MCurrency(Env.getCtx(),convrate.getC_Currency_ID(),null);
		MCurrency currto = new MCurrency(Env.getCtx(),convrate.getC_Currency_ID_To(),null);
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/mm/yyyy");

		StringBuilder sb = new StringBuilder(Msg.translate(Env.getCtx(),"C_Conversion_Rate_ID")+"[");
			sb.append(convrate.getC_Conversion_Rate_ID())
			.append(","+ Msg.translate(Env.getCtx(),"C_Currency_ID")+"=")
			.append(curr.getISO_Code()+"-"+curr.getCurSymbol())
			.append(Msg.translate(Env.getCtx(),"To")+" ")
			.append(currto.getISO_Code()+"-"+currto.getCurSymbol())
			.append(", Multiply=").append(convrate.getMultiplyRate())
			.append(",Divide=").append(convrate.getDivideRate())
			.append(","+ Msg.translate(Env.getCtx(),"ValidFrom")+"="+dt1.format(convrate.getValidFrom()))
			.append(","+ Msg.translate(Env.getCtx(),"ValidTo")+"="+dt1.format(convrate.getValidTo()));
			sb.append("]");
		return sb.toString();
	}	//	toString

}

