/**********************************************************************
* This file is part of iDempiere ERP Open Source                      *
* http://www.idempiere.org                                            *
*                                                                     *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Carlos Ruiz - globalqss                                           *
**********************************************************************/

package org.globalqss.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;
import org.compiere.util.Util;

/**
 */
public class CalloutPayment extends org.compiere.model.CalloutPayment
{

	@Override
	public String invoice(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		// call first the core callout
		String msg = super.invoice(ctx, WindowNo, mTab, mField, value);
		if (! Util.isEmpty(msg)) {
			return msg;
		}

		// and now fill write-off with payment withholdings
		log.info("");
		Integer invInt = (Integer) value;
		int inv_id = 0;
		if (value != null)
			inv_id = invInt.intValue();

		String sql =
				"SELECT NVL(SUM(TaxAmt),0) "
				+ "  FROM LCO_InvoiceWithholding "
				+ " WHERE C_Invoice_ID = ? "
				+ "   AND IsCalcOnPayment = 'Y'"
				+ "   AND Processed = 'N'"
				+ "   AND C_AllocationLine_ID IS NULL"
				+ "   AND IsActive = 'Y'";
		BigDecimal sumtaxamt = DB.getSQLValueBD(null, sql, inv_id);
		mTab.setValue("WriteOffAmt", sumtaxamt);

		return super.amounts(ctx, WindowNo, mTab, mField, value, null);
	}
	
} // CalloutPayment
