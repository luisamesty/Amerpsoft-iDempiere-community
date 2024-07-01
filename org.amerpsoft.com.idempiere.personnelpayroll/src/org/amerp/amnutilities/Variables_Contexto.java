package org.amerp.amnutilities;

import java.io.IOException;
import javax.print.PrintException;
import org.compiere.util.Env;

public class Variables_Contexto {
	 public static void main(String[] args) throws PrintException, IOException {


		    System.out.println("Variables_Contexto Sample: ");

		    int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		    int AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
		    int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		    int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		    String AD_Language = Env.getAD_Language(Env.getCtx());
		   
		    System.out.println(AD_Client_ID);
		    System.out.println(AD_Org_ID);
		    System.out.println(AD_Role_ID);
		    System.out.println(AD_User_ID);
		    System.out.println(AD_Language);
		  }
	public void Context_Variables()
	{
	    int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
	    int AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
	    int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
	    int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
	    String AD_Language = Env.getAD_Language(Env.getCtx());

	    System.out.println(AD_Client_ID);
	    System.out.println(AD_Org_ID);
	    System.out.println(AD_Role_ID);
	    System.out.println(AD_User_ID);
	    System.out.println(AD_Language);
	}
}