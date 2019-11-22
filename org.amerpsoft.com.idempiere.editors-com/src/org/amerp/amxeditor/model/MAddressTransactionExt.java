/**
 * 
 */


/**
 * @author luisamesty
 *
 */

/******************************************************************************
 * Copyright (C) 2013 Elaine Tan                                              *
 * Copyright (C) 2013 Trek Global
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.amerp.amxeditor.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.*;
import org.adempiere.model.IAddressValidation;
import org.compiere.model.MOnlineTrxHistory;
import org.compiere.util.*;

/**
 * Address transaction model
 * @author Elaine
 *
 */
public class MAddressTransactionExt  extends X_C_AddressTransaction 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8572809249265680649L;
	private final static CLogger s_log = CLogger.getCLogger(MAddressTransactionExt.class);
	public MAddressTransactionExt(Properties ctx, int C_AddressTransaction_ID, String trxName) 
	{
		super(ctx, C_AddressTransaction_ID, trxName);
	}
	
	public MAddressTransactionExt(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	/** Error Message						*/
	private String				m_errorMessage = null;
	
	/**
	 * Set error message
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage)
	{
		m_errorMessage = errorMessage;
	}
	
	/**
	 * Get error message
	 * @return error message
	 */
	public String getErrorMessage()
	{
		return m_errorMessage;
	}
	
	/**
	 * Get address validation
	 * @return address validation
	 */
	public MAddressValidationExt getMAddressValidation()
	{
		return new MAddressValidationExt(getCtx(), getC_AddressValidation_ID(), get_TrxName());
	}
	


	/**
	 * Online process address validation
	 * @return true if valid
	 */
	public boolean processOnline()
	{
		setErrorMessage(null);

		boolean processed = false;
		try
		{
			IAddressValidationExt validation = getAddressValidation(null);
			if (validation == null)
				setErrorMessage(Msg.getMsg(Env.getCtx(), "AddressNoValidation"));
			else
			{
				processed = validation.onlineValidate(getCtx(), this, get_TrxName());				
				if (!processed || !isValid())
					setErrorMessage("From " + getMAddressValidation().getName() + ": " + getResult());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "processOnline", e);
			setErrorMessage(Msg.getMsg(Env.getCtx(), "AddressNotProcessed") + ":\n" + e.getMessage());
		}
		
		MOnlineTrxHistory history = new MOnlineTrxHistory(getCtx(), 0, get_TrxName());
		history.setAD_Table_ID(MAddressTransactionExt.Table_ID);
		history.setRecord_ID(getC_AddressTransaction_ID());
		history.setIsError(!(processed && isValid()));
		history.setProcessed(processed);
		
		StringBuilder msg = new StringBuilder();
		if (processed)
			msg.append(getResult());
		else
			msg.append("ERROR: " + getErrorMessage());
		history.setTextMsg(msg.toString());
		
		history.saveEx();
		
		setProcessed(processed);
		return processed;
	}
	
	/**
	 * Get address validation instance
	 * @param validation
	 * @return address validation instance or null if not found
	 */
	public static IAddressValidationExt getAddressValidation(MAddressValidationExt validation) 
	{
		String className = validation.getAddressValidationClass();
		if (className == null || className.length() == 0) 
		{
			s_log.log(Level.SEVERE, "Address validation class not defined: " + validation);
			return null;
		}
		
		List<IAddressValidationFactory> factoryList = Service.locator().list(IAddressValidationFactory.class).getServices();
		if (factoryList == null) 
			return null;
		for (IAddressValidationFactory factory : factoryList)
		{
			IAddressValidation processor = factory.newAddressValidationInstance(className);
			if (processor != null)
				return (IAddressValidationExt) processor;
		}
		
		return null;
	}
	
}