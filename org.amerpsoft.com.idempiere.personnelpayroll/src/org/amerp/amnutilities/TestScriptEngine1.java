package org.amerp.amnutilities;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class TestScriptEngine1 {
  public static void main(String[] args) throws ScriptException {

		// TEST No 1
	  	// Script Engine Manager Basic test witout formula
		ScriptEngine engine1 = new ScriptEngineManager().getEngineByName("JavaScript");
		System.out.println(" **** TEST No.1 ****: ");
		engine1.eval("print('Hello from JavaScript!');");
		
		// TEST No 2
		// Sample Formula using Javascript Class
		Javascript js = new Javascript();
		String scriptTest2 ="CN*3+2000";
		js.setScript(scriptTest2);
		js.variable("CN", 10);
		Double result = (Double) js.run();
		System.out.println(" **** TEST No.2 ****: ");
		System.out.println("Script result (attended: 11) = " + result);

		// TEST No 3
		Object Result= null;
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		ScriptableObject.putProperty(scope, "CN", 1);
		// Execute the script
		Result =  cx.evaluateString(scope, scriptTest2, "p_script", 1, null);
		System.out.println(" **** TEST No.3 ****: ");
		System.out.println(" Result  = "+Result);

		//TEST No 4
		// Script Engine Manager formula Test
		ScriptEngine engine2 = new ScriptEngineManager().getEngineByName("JavaScript");
		ScriptEngineManager manager2 = new ScriptEngineManager();
		ScriptEngine interprete2 = manager2.getEngineByName("JavaScript");		
		interprete2.put("CN", 2);		
		System.out.println(" **** TEST No.4 ****: ");
		engine2.eval(scriptTest2);
		
  }
}