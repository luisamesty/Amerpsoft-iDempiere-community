package org.amerp.amnutilities;

import java.util.List;

public class PayrollAssistRowImport {

    private String pin;
    private String AMN_Payroll_Assist_Unit;
    private int qty;

    // Constructor
    public PayrollAssistRowImport(String pin, String AMN_Payroll_Assist_Unit, int qty) {
        this.pin = pin;
        this.AMN_Payroll_Assist_Unit = AMN_Payroll_Assist_Unit;
        this.qty = qty;
    }

    
    public PayrollAssistRowImport() {
		super();
		
	}


	// Getters y Setters
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAMN_Payroll_Assist_Unit() {
        return AMN_Payroll_Assist_Unit;
    }

    public void setAMN_Payroll_Assist_Unit(String AMN_Payroll_Assist_Unit) {
        this.AMN_Payroll_Assist_Unit = AMN_Payroll_Assist_Unit;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * searchByPin
     * searchByPin on List
     * @param payrollassistRowList
     * @param pin
     * @return
     */
    public static PayrollAssistRowImport searchByPin(List<PayrollAssistRowImport> payrollassistRowList, String pin) {
        for (PayrollAssistRowImport unit : payrollassistRowList) {
            if (unit.getPin().equals(pin)) {
                return unit;
            }
        }
        return null; // Si no se encuentra el elemento
    }
    
    /**
     * incrementQtyByPin
     * @param payrollassistRowList
     * @param pin
     */
    public static void incrementQtyByPin(List<PayrollAssistRowImport> payrollassistRowList, String pin) {
        // Buscar el objeto por pin
    	PayrollAssistRowImport unit = searchByPin(payrollassistRowList, pin);

        // Si se encuentra el objeto, incrementar su cantidad en 1
        if (unit != null) {
            unit.setQty(unit.getQty() + 1); // Incrementar qty
        }
    }
    // Método toString para imprimir el objeto
    @Override
    public String toString() {
        return "PayrollAssistUnit{" +
                "pin='" + pin + '\'' +
                ", AMN_Payroll_Assist_Unit='" + AMN_Payroll_Assist_Unit + '\'' +
                ", qty=" + qty +
                '}';
    }
}