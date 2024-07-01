package org.amerp.amnutilities;

import java.io.IOException;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrintServices {

	  public static void main(String[] args) throws PrintException, IOException {

		    String defaultPrinter =  PrintServiceLookup.lookupDefaultPrintService().getName();
		    System.out.println("Default Printer: " + defaultPrinter);
		    
		    System.out.println("Available Printers: ");
		    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		    for (int i = 0; i < services.length; i++) {
		       System.out.println(services[i].getName());
		    }
	  }
}

