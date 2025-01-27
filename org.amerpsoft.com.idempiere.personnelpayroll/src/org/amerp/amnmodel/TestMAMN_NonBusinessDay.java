package org.amerp.amnmodel;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class TestMAMN_NonBusinessDay {

	/*
	Salida esperad:
		Días festivos entre 2025-01-01 00:00:00 y 2025-01-31 23:59:59: 2
		Días hábiles entre 2025-01-01 00:00:00 y 2025-01-31 23:59:59: 20
		Días de fin de semana entre 2025-01-01 00:00:00 y 2025-01-31 23:59:59: 10
		Próximo día hábil tras 2025-01-01 00:00:00 con 5 días: 2025-01-08 00:00:00
		Día hábil previo a 2025-01-31 23:59:59 retrocediendo 5 días: 2025-01-24 00:00:00
		¿Es 2025-01-01 00:00:00 un día hábil? false
		Días calendario entre 2025-01-01 00:00:00 y 2025-01-31 23:59:59: 30
	 */
	
	
    public static void main(String[] args) {
        // Inicializa variables necesarias
        Properties ctx = new Properties(); // Contexto del sistema
        int clientId = 1000000; // Reemplaza con un AD_Client_ID válido
        int orgId = 1000000;    // Reemplaza con un AD_Org_ID válido
        boolean isSaturdayBusinessDay = false;

        Env.setContext(ctx, "#AD_Client_ID", 1000000); // Configura el cliente
        Env.setContext(ctx, "#AD_Org_ID", 1000000);   // Configura la organización
        
        // Fechas de prueba
        Timestamp startDate = Timestamp.valueOf("2025-01-01 00:00:00");
        Timestamp endDate = Timestamp.valueOf("2025-01-31 23:59:59");

        // 1. Prueba de sqlGetHolliDaysBetween
        BigDecimal holidays = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(
            isSaturdayBusinessDay, startDate, endDate, clientId, orgId
        );
        System.out.println("Días festivos entre " + startDate + " y " + endDate + ": " + holidays);

        // 2. Prueba de sqlGetBusinessDaysBetween
        BigDecimal businessDays = MAMN_NonBusinessDay.sqlGetBusinessDaysBetween(
            isSaturdayBusinessDay, startDate, endDate, clientId, orgId
        );
        System.out.println("Días hábiles entre " + startDate + " y " + endDate + ": " + businessDays);

        // 3. Prueba de sqlGetWeekEndDaysBetween
        BigDecimal weekendDays = MAMN_NonBusinessDay.sqlGetWeekEndDaysBetween(
            isSaturdayBusinessDay, startDate, endDate, clientId, null
        );
        System.out.println("Días de fin de semana entre " + startDate + " y " + endDate + ": " + weekendDays);

        // 4. Prueba de getNextBusinessDay
        BigDecimal daysToAdd = BigDecimal.valueOf(5); // Días a avanzar
        Timestamp nextBusinessDay = MAMN_NonBusinessDay.getNextBusinessDay(
            isSaturdayBusinessDay, startDate, daysToAdd, clientId, orgId
        );
        System.out.println("Próximo día hábil tras " + startDate + " con " + daysToAdd + " días: " + nextBusinessDay);

        // 5. Prueba de getPreviusBusinessDay
        Timestamp previousBusinessDay = MAMN_NonBusinessDay.getPreviusBusinessDay(
            isSaturdayBusinessDay, endDate, daysToAdd, clientId, orgId
        );
        System.out.println("Día hábil previo a " + endDate + " retrocediendo " + daysToAdd + " días: " + previousBusinessDay);

        // 6. Prueba de isBusinessDay
        boolean isBusiness = MAMN_NonBusinessDay.isBusinessDay(
            isSaturdayBusinessDay, startDate, clientId, orgId
        );
        System.out.println("¿Es " + startDate + " un día hábil? " + isBusiness);

        // 7. Prueba de getDaysBetween
        BigDecimal daysBetween = MAMN_NonBusinessDay.getDaysBetween(startDate, endDate);
        System.out.println("Días calendario entre " + startDate + " y " + endDate + ": " + daysBetween);
    }
}
