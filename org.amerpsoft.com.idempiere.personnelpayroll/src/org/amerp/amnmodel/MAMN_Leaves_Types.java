package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMN_Leaves_Types extends X_AMN_Leaves_Types {

	private static final long serialVersionUID = -8988970381810939053L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves_Types.class);
	
	public MAMN_Leaves_Types(Properties ctx, int AMN_Leaves_Types_ID, String trxName) {
		super(ctx, AMN_Leaves_Types_ID, trxName);
		// 
	}

	public MAMN_Leaves_Types(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	/**
	 * getLeavesTypesID
	 * @param ctx
	 * @param trxName
	 * @return
	 */
	public static int getLeavesTypesID(Properties ctx, String trxName) {
        String sql = "SELECT AMN_Leaves_Types_ID FROM AMN_Leaves_Types WHERE Value = 'VA'";
        int leaveTypeID = 0;
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                leaveTypeID = rs.getInt("AMN_Leaves_Types_ID");
            }
        } catch (Exception e) {
            log.severe("Error retrieving AMN_Leaves_Types_ID: " + e.getMessage());
        }
        return leaveTypeID;
    }
}
