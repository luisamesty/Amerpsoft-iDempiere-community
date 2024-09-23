package org.amerp.amnutilities;

import javax.script.ScriptException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


public class TestScriptEngine2 {
	 public static void main(String[] args) throws ScriptException {
		 
			Javascript js = new Javascript();
			String evaluationScript = "(function() {return 1 + x;})();";
			js.setScript(evaluationScript);
			js.variable("x", 10);
			 
			Double result = (Double) js.run();
			System.out.println("Script result (attended: 11) = " + result);

			
			  Context cx = Context.enter();
			  Scriptable scope = cx.initStandardObjects();
			  
			  ScriptableObject.putProperty(scope, "x", 10);

			  
			  cx.evaluateString(scope, evaluationScript, "EvaluationScript", 1, null);

	 }
}
