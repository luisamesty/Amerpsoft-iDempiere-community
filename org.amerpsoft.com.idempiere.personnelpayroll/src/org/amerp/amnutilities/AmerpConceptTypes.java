/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
  *****************************************************************************/

package org.amerp.amnutilities;

import java.math.BigDecimal;

/**
 * @author luisamesty
 *
 */
public class AmerpConceptTypes {
	
		public int CalcOrder;
		public String ConceptVariable;
		public BigDecimal QtyValue;
		public BigDecimal ResultValue;
		public String ErrorMessage;
		/**
		 * @param p_i
		 * @param p_string
		 * @param p_valueOf
		 * @param p_valueOf2
		 * @param p_string2
		 */
        public AmerpConceptTypes(int p_i, String p_string, BigDecimal p_valueOf, BigDecimal p_valueOf2, String p_string2) {
	        // TODO Auto-generated constructor stub
        }
		/**
		 * @return the calcOrder
		 */
		public int getCalcOrder() {
			return CalcOrder;
		}
		/**
		 * @param p_calcOrder the calcOrder to set
		 */
		public void setCalcOrder(int p_calcOrder) {
			CalcOrder = p_calcOrder;
		}
		/**
		 * @return the conceptVariable
		 */
		public String getConceptVariable() {
			return ConceptVariable;
		}
		/**
		 * @param p_conceptVariable the conceptVariable to set
		 */
		public void setConceptVariable(String p_conceptVariable) {
			ConceptVariable = p_conceptVariable;
		}
		/**
		 * @return the qtyValue
		 */
		public BigDecimal getQtyValue() {
			return QtyValue;
		}
		/**
		 * @param p_qtyValue the qtyValue to set
		 */
		public void setQtyValue(BigDecimal p_qtyValue) {
			QtyValue = p_qtyValue;
		}
		/**
		 * @return the resultValue
		 */
		public BigDecimal getResultValue() {
			return ResultValue;
		}
		/**
		 * @param p_resultValue the resultValue to set
		 */
		public void setResultValue(BigDecimal p_resultValue) {
			ResultValue = p_resultValue;
		}
		/**
		 * @return the errorMessage
		 */
		public String getErrorMessage() {
			return ErrorMessage;
		}
		/**
		 * @param p_errorMessage the errorMessage to set
		 */
		public void setErrorMessage(String p_errorMessage) {
			ErrorMessage = p_errorMessage;
		}
	

}
