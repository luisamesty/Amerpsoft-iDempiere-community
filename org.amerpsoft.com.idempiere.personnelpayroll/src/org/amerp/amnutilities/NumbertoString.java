package org.amerp.amnutilities;

import java.util.Locale;

import org.compiere.util.*;
public class NumbertoString {

public static void main(String[] args) throws Exception {
	AmtInWords_ES aiw = new AmtInWords_ES();
	AmtInWords_EN aiwen = new AmtInWords_EN();
	// for (int i=0; i<=2147000000; i++)
		// System.out.println(aiw.getAmtInWords(i+",00"));
	System.out.println("................... Using AmtInWords_XX  Class ................");
	System.out.println("..............Español:");
	System.out.println("9223372036854775807.99="+aiw.getAmtInWords("9223372036854775807.99"));
	System.out.println("54775807.99="+aiw.getAmtInWords("54775807.99"));
	System.out.println("..............Inglés:");
	System.out.println("9223372036854775807.99="+aiwen.getAmtInWords("9223372036854775807.99"));
	System.out.println("54775807.99="+aiwen.getAmtInWords("54775807.99"));
	System.out.println(".................... Using Msg Class ...........................");
	System.out.println("..............Inglés:");
	Locale loca = new Locale("en_US");
	Language lang = new Language("en_US", Env.getAD_Language(Env.getCtx()), loca.US);
	System.out.println("9223372036854775807.99="+Msg.getAmtInWords(lang, "9223372036854775807.99" ));
	lang = new Language("en_CA", Env.getAD_Language(Env.getCtx()), loca.US);
	System.out.println("9223372036854775807.99="+Msg.getAmtInWords(lang, "9223372036854775807.99" ));
	System.out.println("............. Español:");
	//Locale locaes = new Locale("ES");
	// es_ES
	//Language langes = new Language("Spanish", "es_ES", new Locale("ES"));
	System.out.println("AD_Language:"+Language.getAD_Language(new Locale("ES")));
	System.out.println("12345678.89="+Msg.getAmtInWords(new Language("Spanish", "es_ES", new Locale("ES")), "9223372036854775807.99" ));
	System.out.println("9223372036854775807.99="+Msg.getAmtInWords( new org.compiere.util.Language("Spanish", "es_ES", new Locale("ES")), "9223372036854775807.99" ));
	// es_VE
	//Language langes2 = new Language("Spanish", "es_VE", new Locale("ES"));
	System.out.println("AD_Language:"+Language.getAD_Language(new Locale("ES")));
	System.out.println("12345678.89="+Msg.getAmtInWords(new Language("Spanish", "es_VE", new Locale("ES")), "9223372036854775807.99" ));
	System.out.println("9223372036854775807.99="+Msg.getAmtInWords(new Language("Spanish", "es_VE", new Locale("ES")), "9223372036854775807.99" ));

	}
}
