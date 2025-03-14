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
 ******************************************************************************/
package org.amerp.amncallouts;

import java.util.Properties;
import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class AMN_Concept_Types_callout implements IColumnCallout {

    private static final CLogger log = CLogger.getCLogger(AMN_Concept_Types_callout.class);
    
    @Override
    public String start(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
        Integer conceptTypeID = (Integer) mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID);
        if (conceptTypeID == null) {
            return null;
        }
        
        String columnName = mField.getColumnName();
        
        if (columnName.equalsIgnoreCase(MAMN_Concept_Types.COLUMNNAME_Value)) {
            processValueColumn(ctx, mTab, conceptTypeID);
        } else if (columnName.equalsIgnoreCase(MAMN_Concept_Types.COLUMNNAME_Name)) {
            processNameColumn(ctx, mTab, conceptTypeID);
        } else if (columnName.equalsIgnoreCase(MAMN_Concept_Types.COLUMNNAME_CalcOrder)) {
            processCalcOrderColumn(ctx, mTab, conceptTypeID);
        }
        
        return null;
    }
    
    private void processValueColumn(Properties ctx, GridTab mTab, int conceptTypeID) {
        String valueConcept = (String) mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Value);
        if (valueConcept != null) {
            mTab.setValue("variable", valueConcept.trim());
            updateConceptType(ctx, conceptTypeID, mTab);
        }
    }
    
    private void processNameColumn(Properties ctx, GridTab mTab, int conceptTypeID) {
        String nameConcept = (String) mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Name);
        if (nameConcept != null) {
            mTab.setValue("Name", nameConcept.trim());
            updateConceptType(ctx, conceptTypeID, mTab);
        }
    }
    
    private void processCalcOrderColumn(Properties ctx, GridTab mTab, int conceptTypeID) {
        Integer calcOrder = (Integer) mTab.getValue(MAMN_Concept_Types.COLUMNNAME_CalcOrder);
        if (calcOrder != null && calcOrder != 0) {
            mTab.setValue("CalcOrder", calcOrder);
            updateConceptType(ctx, conceptTypeID, mTab);
        }
    }
    
    private void updateConceptType(Properties ctx, int conceptTypeID, GridTab mTab) {
        try {
            MAMN_Concept_Types conceptType = new MAMN_Concept_Types(ctx, conceptTypeID, null);
            conceptType.updateALL_AMNConcept_Types_Proc(conceptType);
            conceptType.updateALL_AMNConcept_Types_Contract(conceptType);
        } catch (Exception e) {
            log.severe("Error updating concept type: " + e.getMessage());
        }
    }
    
}
