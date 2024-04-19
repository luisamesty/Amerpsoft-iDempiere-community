/**
 * 
 */
package org.amerp.amxeditor.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author luisamesty
 *
 */
public class MCommunity extends X_C_Community{

	/**
	 * @param p_ctx
	 * @param C_Community_ID
	 * @param p_trxName
	 */
    public MCommunity(Properties p_ctx, int C_Community_ID, String p_trxName) {
	    super(p_ctx, C_Community_ID, p_trxName);
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MCommunity(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
    }



}
