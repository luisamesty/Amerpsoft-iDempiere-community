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
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import org.compiere.model.MBPartner;
import org.compiere.model.MRefList;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Reference;
import org.compiere.model.X_I_BPartner;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MAMN_Employee extends X_AMN_Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7445345593708015502L;
	static CLogger log = CLogger.getCLogger(MAMN_Employee.class);

	/**	Cache		MAMN_Employee					*/
	private static CCache<Integer,MAMN_Employee> s_cache = new CCache<Integer,MAMN_Employee >(Table_Name, 10);

	public MAMN_Employee(Properties ctx, int AMN_Employee_ID, String trxName) {
		super(ctx, AMN_Employee_ID, trxName);
		// 
	}

	public MAMN_Employee(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// T
	}
	
	// Import Constructor
	public MAMN_Employee(X_AMN_I_Employee impEmployee) {
		
		this (impEmployee.getCtx(), 0, impEmployee.get_TrxName());
		setClientOrg(impEmployee);
		setUpdatedBy(impEmployee.getUpdatedBy());
		//
		String value = impEmployee.getValue();
		if (value == null || value.length() == 0)
			value = impEmployee.getEMail();

		setValue(value);
		String name = impEmployee.getName();
		if (name == null || name.length() == 0)
			name = impEmployee.getEMail();
		setName(name);
		setDescription(impEmployee.getDescription());
		setAD_OrgTo_ID(impEmployee.getAD_OrgTo_ID());
		setAlergic(impEmployee.getAlergic());
		setAMN_CommissionGroup_ID(impEmployee.getAMN_CommissionGroup_ID());
		setAMN_Contract_ID(impEmployee.getAMN_Contract_ID());
		setAMN_Department_ID(impEmployee.getAMN_Department_ID());
		setAMN_Employee_ID(impEmployee.getAMN_Employee_ID());
		setAMN_Jobstation_ID(impEmployee.getAMN_Jobstation_ID());
		setAMN_Jobtitle_ID(impEmployee.getAMN_Jobtitle_ID());
		setAMN_Location_ID(impEmployee.getAMN_Location_ID());
		setAMN_Position_ID(impEmployee.getAMN_Position_ID());
		setAMN_Sector_ID(impEmployee.getAMN_Sector_ID());
		setAMN_Shift_ID(impEmployee.getAMN_Shift_ID());
		setBill_BPartner_ID(impEmployee.getBill_BPartner_ID());
		setBioCode(impEmployee.getBioCode());
		setBirthday(impEmployee.getBirthday());
		setbirthplace(impEmployee.getbirthplace());
		setbloodrh(impEmployee.getbloodrh());
		setbloodtype(impEmployee.getbloodtype());
		setC_Activity_ID(impEmployee.getC_Activity_ID());
		setC_BPartner_ID(impEmployee.getC_BPartner_ID());
		setC_Campaign_ID(impEmployee.getC_Campaign_ID());
		setC_Country_ID(impEmployee.getC_Country_ID());
		setC_Currency_ID(impEmployee.getC_Currency_ID());
		setcivilstatus(impEmployee.getcivilstatus());
		setCountryofNacionality_ID(impEmployee.getCountryofNacionality_ID());
		setC_Project_ID(impEmployee.getC_Project_ID());
		setC_SalesRegion_ID(impEmployee.getC_SalesRegion_ID());
		setDeseases(impEmployee.getDeseases());
		setdownwardloads(impEmployee.getdownwardloads());
		seteducationgrade(impEmployee.geteducationgrade());
		seteducationlevel(impEmployee.geteducationlevel());
		setegresscondition(impEmployee.getegresscondition());
		setegressdate(impEmployee.getegressdate());
		setEMail(impEmployee.getEMail());
		setEMail2(impEmployee.getEMail2());
		setempimg1_ID(impEmployee.getempimg1_ID());
		setempimg2_ID(impEmployee.getempimg2_ID());
		setFirstName1(impEmployee.getFirstName1());
		setFirstName2(impEmployee.getFirstName2());
		setHandUse(impEmployee.getHandUse());
		setHeight(impEmployee.getHeight());
		setHobbyes(impEmployee.getHobbyes());
		setIDCardAux(impEmployee.getIDCardAux());
		setIDCardNo(impEmployee.getIDCardNo());
		setIDNumber(impEmployee.getIDNumber());
		setIDType(impEmployee.getIDType());
		setincomedate(impEmployee.getincomedate());
		setincreasingloads(impEmployee.getincreasingloads());
		setIsDetailedNames(impEmployee.isDetailedNames());
		setisMedicated(impEmployee.isMedicated());
		setisPensioned(impEmployee.isPensioned());
		setisStudying(impEmployee.isStudying());
		setjobcondition(impEmployee.getjobcondition());
		setLastName1(impEmployee.getLastName1());
		setLastName2(impEmployee.getLastName2());
		setOrgSector(impEmployee.getOrgSector());
		setpaymenttype(impEmployee.getpaymenttype());
		setpayrollmode(impEmployee.getpayrollmode());
		setPIN(impEmployee.getPIN());
		setprivateassist(impEmployee.getprivateassist());
		setprofession(impEmployee.getprofession());
		setSalary(impEmployee.getSalary());
		setsex(impEmployee.getsex());
		setSocialSecurityNO(impEmployee.getSocialSecurityNO());
		setSports(impEmployee.getSports());
		setspouse(impEmployee.getspouse());
		setStatus(impEmployee.getStatus());
		setURL(impEmployee.getURL());
		setUseLenses(impEmployee.isUseLenses());
		setWeight(impEmployee.getWeight());
		setzodiacsign(impEmployee.getzodiacsign());
		
	}
	
	/**
	 * Get Employee from Cache
	 * @param ctx context
	 * @param p_MAMN_Employee_ID
	 * @return Memployee
	 */
	public static MAMN_Employee get (Properties ctx, int p_MAMN_Employee_ID)
	{
		if (p_MAMN_Employee_ID <= 0)
			return null;
		//
		Integer key = new Integer(p_MAMN_Employee_ID);
		MAMN_Employee retValue = (MAMN_Employee) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Employee (ctx, p_MAMN_Employee_ID, null);
		if (retValue.getAMN_Employee_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get
	
	/**
	 * sqlGetAMNEmployeeValue (int p_AMN_Employee_ID)
	 * @param p_AMN_Employee_ID	Payroll Employee
	 */
	public String sqlGetAMNEmployeeValue (int p_AMN_Employee_ID)
	
	{
		String sql;
		String Employee_Value="";
		// AMN_Process
    	sql = "select value from amn_process WHERE amn_employee_id=?" ;
    	Employee_Value = DB.getSQLValueString(null, sql, p_AMN_Employee_ID).trim();	
		return Employee_Value;	
	}
	/**
	 * sqlGetAMNEmployeeName (int p_AMN_Employee_ID)
	 * @param p_AMN_Employee_ID	Payroll Employee
	 */
	public String sqlGetAMNEmployeeName (int p_AMN_Employee_ID)
	
	{
		String sql;
		String Employee_Name="";
		// AMN_Process
    	sql = "select name from amn_process WHERE amn_employee_id=?" ;
    	Employee_Name = DB.getSQLValueString(null, sql, p_AMN_Employee_ID).trim();	
		return Employee_Name;	
	}
	/**
	 * sqlGetAMNEmployeeContractID (int p_AMN_Employee_ID)
	 * @param p_AMN_Employee_ID	Payroll Employee
	 */
	public Integer sqlGetAMNEmployeeContractID (int p_AMN_Employee_ID)
	
	{
		String sql;
		Integer Contract_ID = 0;
		// AMN_Contract
    	sql = "select amn_contract_id from amn_process WHERE amn_employee_id=?" ;
    	Contract_ID = DB.getSQLValue(null, sql, p_AMN_Employee_ID);	
		return Contract_ID;	
	}
	/**
	 * sqlGetAMNEmployeeLocationID (int p_AMN_Employee_ID)
	 * @param p_AMN_Employee_ID	Payroll Employee
	 */
	public Integer sqlGetAMNEmployeeLocationID (int p_AMN_Employee_ID)
	
	{
		String sql;
		Integer Location_ID = 0;
		// AMN_Location
    	sql = "select amn_location_id from amn_process WHERE amn_employee_id=?" ;
    	Location_ID = DB.getSQLValue(null, sql, p_AMN_Employee_ID);	
		return Location_ID;	
	}
	/**
	 * sqlGetAMNEmployeeSalary (BigDecimal salary)
	 * @param p_AMN_Employee_ID	Payroll Employee
	 */
	public static BigDecimal sqlGetAMNSalary (int p_AMN_Employee_ID)
	
	{
		String sql;
		BigDecimal Salary = BigDecimal.valueOf(0);
		// AMN_Location
    	sql = "select salary from amn_employee WHERE amn_employee_id=?" ;
    	Salary = DB.getSQLValueBD(null, sql, p_AMN_Employee_ID);	
		return Salary;	
	}
	
	/**
	 * findByBioCode
	 * Description: Find AMN_Employee_ID from BioCode
	 * @param ctx
	 * @param p_BioCode
	 * @return AMN_Employee_ID or 0 if not found()
	 */
	public static int findAMN_Employee_IDbyBioCode( String p_BioCode) {
		int AMN_Employee_ID=0;
		String sql = "SELECT AMN_Employee_ID "
			+ "FROM amn_employee "
			+ "WHERE biocode=? "
			;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setString (1, p_BioCode);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Employee_ID = rs.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    	AMN_Employee_ID = 0;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return AMN_Employee_ID;
	}
	
	/**
	 * findAMN_EmployeebyPin 
	 * Description: Find AMN_Employee_ID from Pin
	 * @param ctx
	 * @param Pin
	 * @return AMN_Employee or null not found()
	 */
	public static MAMN_Employee findAMN_EmployeebyPin(String p_Pin) {
		
		int AMN_Employee_ID=0;
		MAMN_Employee retvalue= null;
		String sql = "SELECT AMN_Employee_ID "
			+ "FROM amn_employee "
			+ "WHERE pin=? "
			;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setString (1, p_Pin);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Employee_ID = rs.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    	AMN_Employee_ID = 0;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (AMN_Employee_ID != 0)
			retvalue = new MAMN_Employee(Env.getCtx(),AMN_Employee_ID, null);
		return retvalue; 
	}
	
	/**
	 * findAMN_Employee_Status
	 * Description: Find Status fro AMN_Employee_ID 
	 * @param p_AMN_Employee_ID
	 * @return Status String 
	 */
	public static String findAMN_Employee_Status( int p_AMN_Employee_ID) {
    	
		MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
		String Employee_Status = amnemployee.getStatus();
		String retValue = "";
		int AD_Reference_ID = ((X_AD_Reference)new Query(Env.getCtx(),X_AD_Reference.Table_Name,"Name='AMN_Status'",null).first()).getAD_Reference_ID();		
		retValue = MRefList.getListName(Env.getCtx(), AD_Reference_ID, Employee_Status);
			return retValue;
	}
	
	/**
	 * getEmployeeCount
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param Status
	 * @return
	 */
	public static Integer getEmployeeCount( 
			int p_AD_Client_ID, int p_AD_Org_ID, 
			String Status) {
				
		Integer Employee_Count = 0;
		Integer retValue = 0;
		String sql = "";
		sql = "SELECT "+
				" CAST(count(*) AS numeric ) " +
				" FROM AMN_employee " + 
				" WHERE AD_Client_ID = ? " +
				" AND AD_Org_ID = ? " +
				" AND Status IN ('"+Status+"')" 
				;        

		//log.warning("sql Count M_INOUT="+sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AD_Client_ID);
            pstmt.setInt (2, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				Employee_Count= rs.getInt(1);
				retValue = Employee_Count;
				//log.warning("sql:"+sql+"  Employee_Count:"+Employee_Count);
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
		//log.warning(" FINAL Employee_Count:"+Employee_Count);
		return retValue;
	}
	
	/**
	 * getLastWorkAnniversary
	 * Returns Last Work Anniversary of an employee since receiptDate
	 * @param receiptDate
	 * @return
	 */
	public  Timestamp getLastWorkAnniversary( Timestamp receiptDate) {
		
		Timestamp incomeDate = getincomedate();
		// Validación de entrada
        if (incomeDate == null || receiptDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        // Convertir las fechas Timestamp a LocalDate
        LocalDate incomeLocalDate = incomeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate receiptLocalDate = receiptDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Si la fecha de ingreso es posterior a la fecha del recibo, no hay aniversario
        if (incomeLocalDate.isAfter(receiptLocalDate)) {
            return null;
        }

        // Calcular los años de diferencia
        long yearsBetween = ChronoUnit.YEARS.between(incomeLocalDate, receiptLocalDate);

        // Determinar la última fecha de aniversario
        LocalDate lastAnniversary = incomeLocalDate.plusYears(yearsBetween);

        // Verificar si el último aniversario es posterior a la fecha del recibo
        if (lastAnniversary.isAfter(receiptLocalDate)) {
            lastAnniversary = lastAnniversary.minusYears(1);
        }

        // Convertir LocalDate de vuelta a Timestamp
        return Timestamp.valueOf(lastAnniversary.atStartOfDay());
    }

	/**
	 * getFirstUserOfBPartner
	 * @return
	 */
	public MUser getFirstUserOfBPartner() {
	    if (getC_BPartner_ID() <= 0)
	        return null;

	    MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());

	    // Obtener todos los usuarios activos del tercero
	    MUser[] users = bp.getContacts(true); // true = solo activos

	    if (users != null && users.length > 0)
	        return users[0];

	    return null;
	}
	
	/**
	 * getFirstUserOfBillBPartner
	 * @return
	 */
	public MUser getFirstUserOfBillBPartner() {
	    if (getC_BPartner_ID() <= 0)
	        return null;

	    MBPartner bp = new MBPartner(getCtx(), getBill_BPartner_ID(), get_TrxName());

	    // Obtener todos los usuarios activos del tercero a facturar
	    MUser[] users = bp.getContacts(true); // true = solo activos

	    if (users != null && users.length > 0)
	        return users[0];

	    return null;
	}
}
