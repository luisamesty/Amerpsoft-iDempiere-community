package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;

import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;

public class MAMN_Employee_Shift extends X_AMN_Employee_Shift{

	private static final long serialVersionUID = -7738539770952501513L;

	
	static CLogger log = CLogger.getCLogger(MAMN_Employee_Shift.class);
			
	public MAMN_Employee_Shift(Properties ctx, int AMN_Employee_Tax_ID, String trxName) {
		super(ctx, AMN_Employee_Tax_ID, trxName);
		//
	}
	public MAMN_Employee_Shift(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		//
	}
	
	public MAMN_Employee_Shift(Properties ctx, String AMN_Employee_Shift_UU, String trxName) {
		super(ctx, AMN_Employee_Shift_UU, trxName);
		// 
	}
	
	public MAMN_Employee_Shift(Properties ctx, String AMN_Employee_Shift_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Employee_Shift_UU, trxName, virtualColumns);
		// 
	}

	/**
     * Devuelve los turnos asociados a un empleado, incluyendo el turno por defecto.
     *
     * @param ctx
     * @param AMN_Employee_ID
     * @param trxName
     * @return arreglo de turnos
     */
	/**
	 * Devuelve los turnos asociados a un empleado, incluyendo el turno por defecto.
	 *
	 * @param ctx             contexto
	 * @param AMN_Employee_ID ID del empleado
	 * @param trxName         nombre de la transacción
	 * @return arreglo de turnos
	 */
	public static MAMN_Shift[] getShiftsForEmployee(Properties ctx, int AMN_Employee_ID, String trxName) {
	    Set<Integer> shiftIds = new HashSet<>();

	    // 1. Buscar registros de la tabla intermedia
	    List<MAMN_Employee_Shift> empShifts = new Query(ctx, MAMN_Employee_Shift.Table_Name, "AMN_Employee_ID=?", trxName)
	            .setParameters(AMN_Employee_ID)
	            .setOnlyActiveRecords(true)
	            .list();

	    for (MAMN_Employee_Shift es : empShifts) {
	        shiftIds.add(es.getAMN_Shift_ID());
	    }

	    // 2. Obtener turno por defecto desde la ficha del trabajador
	    MAMN_Employee employee = new MAMN_Employee(ctx, AMN_Employee_ID, trxName);
	    if (employee.getAMN_Shift_ID() > 0) {
	        shiftIds.add(employee.getAMN_Shift_ID()); // se agrega sin duplicar
	    }

	    // 3. Construir arreglo de turnos
	    return shiftIds.stream()
	            .map(shiftId -> new MAMN_Shift(ctx, shiftId, trxName))
	            .toArray(MAMN_Shift[]::new);
	}
}
