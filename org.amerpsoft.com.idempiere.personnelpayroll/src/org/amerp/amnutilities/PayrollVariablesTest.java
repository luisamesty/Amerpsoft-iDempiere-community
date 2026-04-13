package org.amerp.amnutilities;

public class PayrollVariablesTest {

    public static void main(String[] args) {
        // Crear una instancia con valores iniciales
        PayrollVariables pv = new PayrollVariables(true);

        // Verificar valor inicial CTL_QtyTimes (debe ser 1.0)
        System.out.println("CTL_QtyTimes inicial: " + pv.get("CTL_QtyTimes"));

        // Modificar y leer una variable double
        pv.set("R_SALARIO", 1200.50);
        System.out.println("R_SALARIO después de set: " + pv.get("R_SALARIO"));

        // Sumar valor
        pv.addTo("R_SALARIO", 100.25);
        System.out.println("R_SALARIO después de addTo: " + pv.get("R_SALARIO")); // Debe ser 1300.75

        // Leer una variable no inicializada (debe ser 0.0)
        System.out.println("QTY_HND: " + pv.get("QTY_HND"));

        // Asignar y leer una variable String
        pv.setString("AM_Status", "ACTIVO");
        System.out.println("AM_Status: " + pv.getString("AM_Status"));

        // Verifica los valores por defecto
        System.out.println("IS_VACACION: " + pv.getString("IS_VACACION")); // Debe ser "N"
        System.out.println("AM_Workforce: " + pv.getString("AM_Workforce")); // Debe ser "A"

        // Imprimir todos los valores double (opcional)
        System.out.println("\n---- Todas las variables Double ----");
        pv.getPayrollVars().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });

        // Imprimir todos los valores String (opcional)
        System.out.println("\n---- Todas las variables String ----");
        pv.getPayrollStrings().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
    }
}
