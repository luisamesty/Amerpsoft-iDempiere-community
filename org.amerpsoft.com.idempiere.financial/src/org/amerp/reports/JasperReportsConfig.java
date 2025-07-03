package org.amerp.reports;

import java.io.InputStream;
import java.util.Properties;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;

public class JasperReportsConfig {
    static {
        try (InputStream is = JasperReportsConfig.class.getResourceAsStream("/main/resources/jasperreport.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                JasperReportsContext ctx = DefaultJasperReportsContext.getInstance();
                props.forEach((k,v) -> ctx.setProperty(k.toString(), v.toString()));
                System.out.println("✅ JasperReportsConfig: propiedades cargadas manualmente desde jasperreport.properties");
            } else {
                JasperReportsContext context = DefaultJasperReportsContext.getInstance();
                context.setProperty(
                    "net.sf.jasperreports.extension.chart.theme.factory.spring.enabled",
                    "false"
                );
          	    System.out.println("⚠ JasperReportsConfig: jasperreports.properties no encontrado en el classpath");
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}