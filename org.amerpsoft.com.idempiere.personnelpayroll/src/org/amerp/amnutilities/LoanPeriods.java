package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * LoanPeriods
 * 	int LoanCuotaNo
 *	String PeriodValue
 *	Timestamp PeriodDate
 *	BigDecimal CuotaAmount 
 */
public class LoanPeriods {
	
	public int LoanCuotaNo;
	public String PeriodValue;
	public Timestamp PeriodDateIni;
	public Timestamp PeriodDateEnd;
	public BigDecimal CuotaAmount ;
	
	public LoanPeriods() {
		this.LoanCuotaNo = 0;
		this.PeriodValue = "";
		this.PeriodDateIni  = null;
		this.PeriodDateEnd  = null;
		this.CuotaAmount = BigDecimal.valueOf(0);
	}
	public LoanPeriods(int LoanCuotaNo, String PeriodValue, Timestamp PeriodDateIni, Timestamp PeriodDateEnd, BigDecimal CuotaAmount) {
		this.LoanCuotaNo = LoanCuotaNo;
		this.PeriodValue = PeriodValue;
		this.PeriodDateIni  = PeriodDateIni;
		this.PeriodDateEnd  = PeriodDateEnd;
		this.CuotaAmount = CuotaAmount;
		// TODO Auto-generated constructor stub
	}
	public int getLoanCuotaNo() {
		return LoanCuotaNo;
	}
	public void setLoanCuotaNo(int loanCuotaNo) {
		LoanCuotaNo = loanCuotaNo;
	}
	public String getPeriodValue() {
		return PeriodValue;
	}
	public void setPeriodValue(String periodValue) {
		PeriodValue = periodValue;
	}
	
	public BigDecimal getCuotaAmount() {
		return CuotaAmount;
	}
	public void setCuotaAmount(BigDecimal cuotaAmount) {
		CuotaAmount = cuotaAmount;
	}

	public Timestamp getPeriodDateIni() {
		return PeriodDateIni;
	}
	public void setPeriodDateIni(Timestamp periodDateIni) {
		PeriodDateIni = periodDateIni;
	}
	public Timestamp getPeriodDateEnd() {
		return PeriodDateEnd;
	}
	public void setPeriodDateEnd(Timestamp periodDateEnd) {
		PeriodDateEnd = periodDateEnd;
	}

	  /**
     * Genera una lista de objetos LoanPeriods basados en el tipo de período (PayrollDaysInt).
     *
     * @param startDate      Fecha inicial.
     * @param numberOfPeriods Número de períodos a calcular.
     * @param payrollDaysInt  Tipo de período (7, 14, 15, 30).
     * @return Lista de objetos LoanPeriods.
     */
    public List<LoanPeriods> generateLoanPeriods(Timestamp startDate, int numberOfPeriods, int payrollDaysInt) {
        List<LoanPeriods> loanPeriodsList = new ArrayList<>();

        // Convertir Timestamp a LocalDate
        LocalDate currentDate = startDate.toLocalDateTime().toLocalDate();

        for (int i = 0; i < numberOfPeriods; i++) {
            LocalDate periodStartDate = null;
            LocalDate periodEndDate = null;

            switch (payrollDaysInt) {
                case 7: // Semanal
                    periodStartDate = currentDate;
                    periodEndDate = currentDate.plusDays(6);
                    currentDate = currentDate.plusDays(7); // Avanzar una semana
                    break;

                case 14: // Cada dos semanas
                    periodStartDate = currentDate;
                    periodEndDate = currentDate.plusDays(13);
                    currentDate = currentDate.plusDays(14); // Avanzar dos semanas
                    break;

                case 15: // Quincenal (primera y segunda quincena del mes)
                    periodStartDate = currentDate.withDayOfMonth(1); // Primer día del mes
                    periodEndDate = currentDate.withDayOfMonth(15); // Día 15 del mes
                    if (i % 2 == 1) { // Segunda quincena
                        periodStartDate = currentDate.withDayOfMonth(16);
                        periodEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                    }
                    currentDate = currentDate.plusMonths(1); // Avanzar al siguiente mes
                    break;

                case 30: // Mensual (primer día y último día del mes)
                    periodStartDate = currentDate.withDayOfMonth(1); // Primer día del mes
                    periodEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth()); // Último día del mes
                    currentDate = currentDate.plusMonths(1); // Avanzar al siguiente mes
                    break;

                default:
                    throw new IllegalArgumentException("Valor de PayrollDaysInt no válido: " + payrollDaysInt);
            }

            // Convertir LocalDate a Timestamp
            Timestamp periodDateIni = Timestamp.valueOf(periodStartDate.atStartOfDay());
            Timestamp periodDateEnd = Timestamp.valueOf(periodEndDate.atStartOfDay());

            // Crear un objeto LoanPeriods y agregarlo a la lista
            LoanPeriods loanPeriod = new LoanPeriods(
                    i + 1, // Número de cuota
                    "Period " + (i + 1), // Valor del período
                    periodDateIni, // Fecha de inicio del período
                    periodDateEnd, // Fecha de fin del período
                    BigDecimal.ZERO // Monto de la cuota (puedes ajustar este valor)
            );
            loanPeriodsList.add(loanPeriod);
        }

        return loanPeriodsList;
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        Timestamp startDate = Timestamp.valueOf("2023-10-01 00:00:00"); // Fecha inicial
        // numberOfPeriods Número de períodos a calcular
        // payrollDaysInt Tipo de período (7, 14, 15, 30)
        LoanPeriods periodo = new LoanPeriods();;
        List<LoanPeriods> periods;
        // SEMANAL
        periods = periodo.generateLoanPeriods(startDate, 5, 7);
        System.out.println("Semanal ..... ");
        for (LoanPeriods period : periods) {
            System.out.println("Cuota No: " + period.getLoanCuotaNo() +
                    ", Periodo: " + period.getPeriodValue() +
                    ", Inicio de Período: " + period.getPeriodDateIni() +
                    ", Fin de Período: " + period.getPeriodDateEnd() +
                    ", Monto: " + period.getCuotaAmount());
        }
        
        // QUINCENAL
        periods = periodo.generateLoanPeriods(startDate, 5, 15);
        System.out.println("Semanal ..... ");
        for (LoanPeriods period : periods) {
            System.out.println("Cuota No: " + period.getLoanCuotaNo() +
                    ", Periodo: " + period.getPeriodValue() +
                    ", Inicio de Período: " + period.getPeriodDateIni() +
                    ", Fin de Período: " + period.getPeriodDateEnd() +
                    ", Monto: " + period.getCuotaAmount());
        }

        // MENSUAL
        periods = periodo.generateLoanPeriods(startDate, 5, 30);
        System.out.println("Semanal ..... ");
        for (LoanPeriods period : periods) {
            System.out.println("Cuota No: " + period.getLoanCuotaNo() +
                    ", Periodo: " + period.getPeriodValue() +
                    ", Inicio de Período: " + period.getPeriodDateIni() +
                    ", Fin de Período: " + period.getPeriodDateEnd() +
                    ", Monto: " + period.getCuotaAmount());
        }

    }
    
}