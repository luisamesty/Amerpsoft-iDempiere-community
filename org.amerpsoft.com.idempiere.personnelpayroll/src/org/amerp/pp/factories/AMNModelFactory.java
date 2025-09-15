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
package org.amerp.pp.factories;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.amerp.amnmodel.*;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class AMNModelFactory implements IModelFactory {
	
	CLogger log = CLogger.getCLogger(AMNModelFactory.class);
	
	@Override
	public Class<?> getClass(String tableName) {

		// Class
		// MAMN_Concept_Types_Proc
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types_Proc.Table_Name))
			return MAMN_Concept_Types_Proc.class;
		// MAMN_Concept_Types
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types.Table_Name))
			return MAMN_Concept_Types.class;
		// MAMN_Concept_Uom
		if(tableName.equalsIgnoreCase(MAMN_Concept_Uom.Table_Name))
			return MAMN_Concept_Uom.class;
		// MAMN_Concept
		if(tableName.equalsIgnoreCase(MAMN_Concept.Table_Name))
			return MAMN_Concept.class;
		// MAMN_Contract
		if(tableName.equalsIgnoreCase(MAMN_Contract.Table_Name))
			return MAMN_Contract.class;
		// MAMN_Department
		if(tableName.equalsIgnoreCase(MAMN_Department.Table_Name))
			return MAMN_Department.class;
		// MAMN_Employee
		if(tableName.equalsIgnoreCase(MAMN_Employee.Table_Name))
			return MAMN_Employee.class;
		// MAMN_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_Employee_Salary.Table_Name))
			return MAMN_Employee_Salary.class;
		// MAMN_Location
		if(tableName.equalsIgnoreCase(MAMN_Location.Table_Name))
			return MAMN_Location.class;
		// MAMN_Payroll_Deferred
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Deferred.Table_Name))
			return MAMN_Payroll_Deferred.class;
		// MAMN_Payroll_Detail
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Detail.Table_Name))
			return MAMN_Payroll_Detail.class;
		// MAMN_Payroll_Assist
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist.Table_Name))
			return MAMN_Payroll_Assist.class;
		// MAMN_Payroll_Assist_Proc
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.Table_Name))
			return MAMN_Payroll_Assist_Proc.class;
		// MAMN_Payroll_Historic
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Historic.Table_Name))
			return MAMN_Payroll_Historic.class;
		// MAMN_Payroll
		if(tableName.equalsIgnoreCase(MAMN_Payroll.Table_Name)) 
			return MAMN_Payroll.class;
		// MAMN_Period
		if(tableName.equalsIgnoreCase(MAMN_Period.Table_Name))
			return MAMN_Period.class;
		// MAMN_Period_Assist
		if(tableName.equalsIgnoreCase(MAMN_Period_Assist.Table_Name))
			return MAMN_Period_Assist.class;
		// MAMN_Position
		if(tableName.equalsIgnoreCase(MAMN_Position.Table_Name))
			return MAMN_Position.class;
		// MAMN_Process
		if(tableName.equalsIgnoreCase(MAMN_Process.Table_Name))
			return MAMN_Process.class;
		// MAMN_Rates
		if(tableName.equalsIgnoreCase(MAMN_Rates.Table_Name))
			return MAMN_Rates.class;
		// MAMN_Role_Access
		if(tableName.equalsIgnoreCase(MAMN_Role_Access.Table_Name))
			return MAMN_Role_Access.class;
		// MAMN_Shift_Detail
		if(tableName.equalsIgnoreCase(MAMN_Shift_Detail.Table_Name))
			return MAMN_Shift_Detail.class;
		// MAMN_Shift
		if(tableName.equalsIgnoreCase(MAMN_Shift.Table_Name))
			return MAMN_Shift.class;
		// MAMN_Schema
		if(tableName.equalsIgnoreCase(MAMN_Schema.Table_Name))
			return MAMN_Schema.class;
		// MAMN_Payroll_Lot
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Lot.Table_Name))
			return MAMN_Payroll_Lot.class;
		// MAMN_Jobunit
		if(tableName.equalsIgnoreCase(MAMN_Jobunit.Table_Name))
			return MAMN_Jobunit.class;
		// MAMN_Jobtitle
		if(tableName.equalsIgnoreCase(MAMN_Jobtitle.Table_Name))
			return MAMN_Jobtitle.class;
		// MAMN_Jobstation
		if(tableName.equalsIgnoreCase(MAMN_Jobstation.Table_Name))
			return MAMN_Jobstation.class;
		// MAMN_Rules
		if(tableName.equalsIgnoreCase(MAMN_Rules.Table_Name))
			return MAMN_Rules.class;
		// MAMN_Sector
		if(tableName.equalsIgnoreCase(MAMN_Sector.Table_Name))
			return MAMN_Sector.class;
		// MAMN_Leaves
		if(tableName.equalsIgnoreCase(MAMN_Leaves.Table_Name))
			return MAMN_Leaves.class;
		// MAMN_Leaves_Types
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Types.Table_Name))
			return MAMN_Leaves_Types.class;
		// MAMN_Payroll_Schedulle
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Schedulle.Table_Name))
			return MAMN_Payroll_Schedulle.class;	
		// MAMN_Leaves_Flow
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Flow.Table_Name))
			return MAMN_Leaves_Flow.class;
		// MAMN_Leaves_Nodes
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Nodes.Table_Name))
			return MAMN_Leaves_Nodes.class;
		// MAMN_Leaves_Access
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Access.Table_Name))
			return MAMN_Leaves_Access.class;
		// MAMN_Payroll_Assist_Unit
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Unit.Table_Name))
			return MAMN_Payroll_Assist_Unit.class;
		// MAMN_Payroll_Assist_Row
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Row.Table_Name))
			return MAMN_Payroll_Assist_Row.class;
		// IMPORT TABLES
		// MAMN_I_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_I_Employee_Salary.Table_Name))
			return MAMN_I_Employee_Salary.class;
		// BPartner Tables
		// MCustom BPartnerLocation Table
		if(tableName.equalsIgnoreCase(MCustomBPartnerLocation.Table_Name))
			return MCustomBPartnerLocation.class;
		// C_Invoice Table
		if(tableName.equalsIgnoreCase(MAMN_Invoice.Table_Name))
			return MAMN_Invoice.class;
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		// Record_ID
		// MAMN_Concept_Types_Proc
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types_Proc.Table_Name))
			return new MAMN_Concept_Types_Proc(Env.getCtx(),Record_ID, trxName);
		// MAMN_Concept_Types
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types.Table_Name))
			return new MAMN_Concept_Types(Env.getCtx(),Record_ID, trxName);
		// MAMN_Concept_Uom
		if(tableName.equalsIgnoreCase(MAMN_Concept_Uom.Table_Name))
			return new MAMN_Concept_Uom(Env.getCtx(),Record_ID, trxName);
		// MAMN_Concept
		if(tableName.equalsIgnoreCase(MAMN_Concept.Table_Name))
			return new MAMN_Concept(Env.getCtx(),Record_ID, trxName);
		// MAMN_Contract
		if(tableName.equalsIgnoreCase(MAMN_Contract.Table_Name))
			return new MAMN_Contract(Env.getCtx(),Record_ID, trxName);
		// MAMN_Department
		if(tableName.equalsIgnoreCase(MAMN_Department.Table_Name))
			return new MAMN_Department(Env.getCtx(),Record_ID, trxName);
		// MAMN_Employee
		if(tableName.equalsIgnoreCase(MAMN_Employee.Table_Name))
			return new MAMN_Employee(Env.getCtx(),Record_ID, trxName);
		// MAMN_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_Employee_Salary.Table_Name))
			return new MAMN_Employee_Salary(Env.getCtx(),Record_ID, trxName);
		// MAMN_Location
		if(tableName.equalsIgnoreCase(MAMN_Location.Table_Name))
			return new MAMN_Location(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Deferred
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Deferred.Table_Name))
			return new MAMN_Payroll_Deferred(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Detail
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Detail.Table_Name))
			return new MAMN_Payroll_Detail(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Assist
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist.Table_Name))
			return new MAMN_Payroll_Assist(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Assist_Proc
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.Table_Name))
			return new MAMN_Payroll_Assist_Proc(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Historic
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Historic.Table_Name))
			return new MAMN_Payroll_Historic(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll
		if(tableName.equalsIgnoreCase(MAMN_Payroll.Table_Name))
			return new MAMN_Payroll(Env.getCtx(),Record_ID, trxName);
		// MAMN_Period
		if(tableName.equalsIgnoreCase(MAMN_Period.Table_Name))
			return new MAMN_Period(Env.getCtx(),Record_ID, trxName);
		// MAMN_Period_Assist
		if(tableName.equalsIgnoreCase(MAMN_Period_Assist.Table_Name))
			return new MAMN_Period_Assist(Env.getCtx(),Record_ID, trxName);
		// MAMN_Position
		if(tableName.equalsIgnoreCase(MAMN_Position.Table_Name))
			return new MAMN_Position(Env.getCtx(),Record_ID, trxName);
		// MAMN_Process
		if(tableName.equalsIgnoreCase(MAMN_Process.Table_Name))
			return new MAMN_Process(Env.getCtx(),Record_ID, trxName);
		// MAMN_Rates
		if(tableName.equalsIgnoreCase(MAMN_Rates.Table_Name))
			return new MAMN_Rates(Env.getCtx(),Record_ID, trxName);
		// MAMN_Role_Access
		if(tableName.equalsIgnoreCase(MAMN_Role_Access.Table_Name))
			return new MAMN_Role_Access(Env.getCtx(),Record_ID, trxName);
		// MAMN_Shift_Detail
		if(tableName.equalsIgnoreCase(MAMN_Shift_Detail.Table_Name))
			return new MAMN_Shift_Detail(Env.getCtx(),Record_ID, trxName);
		// MAMN_Shift
		if(tableName.equalsIgnoreCase(MAMN_Shift.Table_Name))
			return new MAMN_Shift(Env.getCtx(),Record_ID, trxName);
		// MAMN_Schema
		if(tableName.equalsIgnoreCase(MAMN_Schema.Table_Name))
			return new MAMN_Schema(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Lot
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Lot.Table_Name))
			return new MAMN_Payroll_Lot(Env.getCtx(),Record_ID, trxName);
		// MAMN_Jobunit
		if(tableName.equalsIgnoreCase(MAMN_Jobunit.Table_Name))
			return new MAMN_Jobunit(Env.getCtx(),Record_ID, trxName);
		// MAMN_Jobtitle
		if(tableName.equalsIgnoreCase(MAMN_Jobtitle.Table_Name))
			return new MAMN_Jobtitle(Env.getCtx(),Record_ID, trxName);
		// MAMN_Jobstation
		if(tableName.equalsIgnoreCase(MAMN_Jobstation.Table_Name))
			return new MAMN_Jobstation(Env.getCtx(),Record_ID, trxName);
		// MAMN_Rules
		if(tableName.equalsIgnoreCase(MAMN_Rules.Table_Name))
			return new MAMN_Rules(Env.getCtx(),Record_ID, trxName);
		// MAMN_Sector
		if(tableName.equalsIgnoreCase(MAMN_Sector.Table_Name))
			return new MAMN_Sector(Env.getCtx(),Record_ID, trxName);
		// MAMN_Leaves
		if(tableName.equalsIgnoreCase(MAMN_Leaves.Table_Name))
			return new MAMN_Leaves(Env.getCtx(),Record_ID, trxName);
		// MAMN_Leaves_Types
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Types.Table_Name))
			return new MAMN_Leaves_Types(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Schedulle
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Schedulle.Table_Name))
			return new MAMN_Payroll_Schedulle(Env.getCtx(),Record_ID, trxName);
		// MAMN_Leaves_Flow
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Flow.Table_Name))
			return new MAMN_Leaves_Flow(Env.getCtx(),Record_ID, trxName);
		// MAMN_Leaves_Nodes
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Nodes.Table_Name))
			return new MAMN_Leaves_Nodes(Env.getCtx(),Record_ID, trxName);
		// MAMN_Leaves_Access
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Access.Table_Name))
			return new MAMN_Leaves_Access(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Assist_Unit
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Unit.Table_Name))
			return new MAMN_Payroll_Assist_Unit(Env.getCtx(),Record_ID, trxName);
		// MAMN_Payroll_Assist_Row
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Row.Table_Name))
			return new MAMN_Payroll_Assist_Row(Env.getCtx(),Record_ID, trxName);
		// IMPORT TABLES
		// MAMN_I_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_I_Employee_Salary.Table_Name))
			return new MAMN_I_Employee_Salary(Env.getCtx(),Record_ID, trxName);
		// BPartner Tables
		// MCustom BPartnerLocation Table
		if(tableName.equalsIgnoreCase(MCustomBPartnerLocation.Table_Name))
			return new MCustomBPartnerLocation(Env.getCtx(),Record_ID, trxName);
		// C_Invoice Table
		if(tableName.equalsIgnoreCase(MAMN_Invoice.Table_Name))
			return new MAMN_Invoice(Env.getCtx(),Record_ID, trxName);
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		// rs 
		// MAMN_Concept_Types_Proc
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types_Proc.Table_Name))
			return new MAMN_Concept_Types_Proc(Env.getCtx(),rs, trxName);
		// MAMN_Concept_Types
		if(tableName.equalsIgnoreCase(MAMN_Concept_Types.Table_Name))
			return new MAMN_Concept_Types(Env.getCtx(),rs, trxName);
		// MAMN_Concept_Uom
		if(tableName.equalsIgnoreCase(MAMN_Concept_Uom.Table_Name))
			return new MAMN_Concept_Uom(Env.getCtx(),rs, trxName);
		// MAMN_Concept
		if(tableName.equalsIgnoreCase(MAMN_Concept.Table_Name))
			return new MAMN_Concept(Env.getCtx(),rs, trxName);
		// MAMN_Contract
		if(tableName.equalsIgnoreCase(MAMN_Contract.Table_Name))
			return new MAMN_Contract(Env.getCtx(),rs, trxName);
		// MAMN_Department
		if(tableName.equalsIgnoreCase(MAMN_Department.Table_Name))
			return new MAMN_Department(Env.getCtx(),rs, trxName);
		// MAMN_Employee
		if(tableName.equalsIgnoreCase(MAMN_Employee.Table_Name))
			return new MAMN_Employee(Env.getCtx(),rs, trxName);
		// MAMN_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_Employee_Salary.Table_Name))
			return new MAMN_Employee_Salary(Env.getCtx(),rs, trxName);
		// MAMN_Location
		if(tableName.equalsIgnoreCase(MAMN_Location.Table_Name))
			return new MAMN_Location(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Deferred
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Deferred.Table_Name))
			return new MAMN_Payroll_Deferred(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Detail
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Detail.Table_Name))
			return new MAMN_Payroll_Detail(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Assist
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist.Table_Name))
			return new MAMN_Payroll_Assist(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Assist_Proc
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.Table_Name))
			return new MAMN_Payroll_Assist_Proc(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Historic
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Historic.Table_Name))
			return new MAMN_Payroll_Historic(Env.getCtx(),rs, trxName);
		// MAMN_Payroll
		if(tableName.equalsIgnoreCase(MAMN_Payroll.Table_Name)) 
			return new MAMN_Payroll(Env.getCtx(),rs, trxName);
		// MAMN_Period
		if(tableName.equalsIgnoreCase(MAMN_Period.Table_Name))
			return new MAMN_Period(Env.getCtx(),rs, trxName);
		// MAMN_Period_Assist
		if(tableName.equalsIgnoreCase(MAMN_Period_Assist.Table_Name))
			return new MAMN_Period_Assist(Env.getCtx(),rs, trxName);
		// MAMN_Position
		if(tableName.equalsIgnoreCase(MAMN_Position.Table_Name))
			return new MAMN_Position(Env.getCtx(),rs, trxName);
		// MAMN_Process
		if(tableName.equalsIgnoreCase(MAMN_Process.Table_Name))
			return new MAMN_Process(Env.getCtx(),rs, trxName);
		// MAMN_Rates
		if(tableName.equalsIgnoreCase(MAMN_Rates.Table_Name))
			return new MAMN_Rates(Env.getCtx(),rs, trxName);
		// MAMN_Role_Access
		if(tableName.equalsIgnoreCase(MAMN_Role_Access.Table_Name))
			return new MAMN_Role_Access(Env.getCtx(),rs, trxName);
		// MAMN_Shift_Detail
		if(tableName.equalsIgnoreCase(MAMN_Shift_Detail.Table_Name))
			return new MAMN_Shift_Detail(Env.getCtx(),rs, trxName);
		// MAMN_Shift
		if(tableName.equalsIgnoreCase(MAMN_Shift.Table_Name))
			return new MAMN_Shift(Env.getCtx(),rs, trxName);
		// MAMN_Schema
		if(tableName.equalsIgnoreCase(MAMN_Schema.Table_Name))
			return new MAMN_Schema(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Lot
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Lot.Table_Name))
			return new MAMN_Payroll_Lot(Env.getCtx(),rs, trxName);
		// MAMN_Jobunit
		if(tableName.equalsIgnoreCase(MAMN_Jobunit.Table_Name))
			return new MAMN_Jobunit(Env.getCtx(),rs, trxName);
		// MAMN_Jobtitle
		if(tableName.equalsIgnoreCase(MAMN_Jobtitle.Table_Name))
			return new MAMN_Jobtitle(Env.getCtx(),rs, trxName);
		// MAMN_Jobstation
		if(tableName.equalsIgnoreCase(MAMN_Jobstation.Table_Name))
			return new MAMN_Jobstation(Env.getCtx(),rs, trxName);
		// MAMN_Rules
		if(tableName.equalsIgnoreCase(MAMN_Rules.Table_Name))
			return new MAMN_Rules(Env.getCtx(),rs, trxName);
		// MAMN_Sector
		if(tableName.equalsIgnoreCase(MAMN_Sector.Table_Name))
			return new MAMN_Sector(Env.getCtx(),rs, trxName);
		// MAMN_Leaves
		if(tableName.equalsIgnoreCase(MAMN_Leaves.Table_Name))
			return new MAMN_Leaves(Env.getCtx(),rs, trxName);
		// MAMN_Leaves_Types
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Types.Table_Name))
			return new MAMN_Leaves_Types(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Schedulle
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Schedulle.Table_Name))
			return new MAMN_Payroll_Schedulle(Env.getCtx(),rs, trxName);
		// MAMN_Leaves_Flow
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Flow.Table_Name))
			return new MAMN_Leaves_Flow(Env.getCtx(),rs, trxName);
		// MAMN_Leaves_Nodes
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Nodes.Table_Name))
			return new MAMN_Leaves_Nodes(Env.getCtx(),rs, trxName);
		// MAMN_Leaves_Access
		if(tableName.equalsIgnoreCase(MAMN_Leaves_Access.Table_Name))
			return new MAMN_Leaves_Access(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Assist_Unit
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Unit.Table_Name))
			return new MAMN_Payroll_Assist_Unit(Env.getCtx(),rs, trxName);
		// MAMN_Payroll_Assist_Row
		if(tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Row.Table_Name))
			return new MAMN_Payroll_Assist_Row(Env.getCtx(),rs, trxName);
		// IMPORT TABLES
		// MAMN_I_Employee_Salary
		if(tableName.equalsIgnoreCase(MAMN_I_Employee_Salary.Table_Name))
			return new MAMN_I_Employee_Salary(Env.getCtx(),rs, trxName);
		// MCustom BPartnerLocation Table
		if(tableName.equalsIgnoreCase(MCustomBPartnerLocation.Table_Name))
			return new MCustomBPartnerLocation(Env.getCtx(),rs, trxName);
		// C_Invoice Table
		if(tableName.equalsIgnoreCase(MAMN_Invoice.Table_Name))
			return new MAMN_Invoice(Env.getCtx(),rs, trxName);
		return null;
	}

}
