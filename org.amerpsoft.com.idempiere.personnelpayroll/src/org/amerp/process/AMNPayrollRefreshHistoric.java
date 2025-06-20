package org.amerp.process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;

public class AMNPayrollRefreshHistoric extends SvrProcess {

	int p_AMN_Contract_ID=0;
	int p_AMN_Employee_ID=0;
	Timestamp p_DateIni;
	Timestamp p_DateEnd;
	String Msg_Value="";

	static CLogger log = CLogger.getCLogger(AMNPayrollRefreshHistoric.class);
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameters = getParameter();
		for (ProcessInfoParameter param : parameters) {
			String name = param.getParameterName();
			if (param.getParameter() == null)
				continue;

			switch (name) {
				case "AMN_Contract_ID":
					p_AMN_Contract_ID = param.getParameterAsInt();
					break;
				case "AMN_Employee_ID":
					p_AMN_Employee_ID = param.getParameterAsInt();
					break;
				case "DateIni":
					p_DateIni = param.getParameterAsTimestamp();
					break;
				case "DateEnd":
					p_DateEnd = param.getParameterAsTimestamp();
					break;
				default:
					log.warning("Parámetro no utilizado: " + name);
					break;
			}
		}
		log.warning(">>> Parámetros: AMN_Contract_ID=" + p_AMN_Contract_ID 
				+ " | AMN_Employee_ID=" + p_AMN_Employee_ID 
				+ " | DateIni=" + p_DateIni 
				+ " | DateEnd=" + p_DateEnd);
	}

	@Override
	protected String doIt() throws Exception {
		log.info("Inicio proceso AMNPayrollRefreshHistoric - "
			+ "Contract_ID=" + p_AMN_Contract_ID
			+ ", Employee_ID=" + p_AMN_Employee_ID
			+ ", DateIni=" + p_DateIni
			+ ", DateEnd=" + p_DateEnd);

		// Validación de fechas
		if (p_DateIni == null || p_DateEnd == null)
			throw new IllegalArgumentException("Debe proporcionar el rango de fechas (DateIni y DateEnd)");

		List<MAMN_Employee> empleados = new ArrayList<>();

		if (p_AMN_Employee_ID > 0) {
			// Procesar solo un empleado específico
			MAMN_Employee emp = new MAMN_Employee(getCtx(), p_AMN_Employee_ID, get_TrxName());
			if (emp.get_ID() <= 0)
				throw new AdempiereException("Empleado no válido");

			empleados.add(emp);
		} else {
			// Construir cláusula WHERE dinámica
			String whereClause = "IsActive='Y'";
			List<Object> params = new ArrayList<>();

			if (p_AMN_Contract_ID > 0) {
				whereClause += " AND AMN_Contract_ID=?";
				params.add(p_AMN_Contract_ID);
			}

			empleados = new Query(getCtx(), MAMN_Employee.Table_Name, whereClause, get_TrxName())
				.setParameters(params)
				.list();
		}

		if (empleados.isEmpty()) {
			return "No se encontraron empleados para procesar";
		}

		for (MAMN_Employee emp : empleados) {
			log.info("Procesando empleado: " + emp.getName() + " (ID=" + emp.get_ID() + ")");

			// SALARY HISTORIC
			MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, null);
			if (amnpayrollhistoric.createAmnPayrollHistoricV2(getCtx(), null, emp.getAMN_Employee_ID(), p_DateIni, p_DateEnd, get_TrxName())) {
				Msg_Value += "Empleado " + emp.getName() + ": OK\n";
			}
			// SALARY HISTORIC END

			addLog(Msg_Value);
		}

		Msg_Value = "Proceso completado. Empleados procesados: " + empleados.size() + "\n" + Msg_Value;

		return "Proceso completado. Empleados procesados: " + empleados.size();
	}

}
