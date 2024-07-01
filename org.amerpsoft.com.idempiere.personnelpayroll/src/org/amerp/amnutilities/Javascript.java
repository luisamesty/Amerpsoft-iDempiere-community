package org.amerp.amnutilities;

import java.io.Reader;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
 
/**
 * Simple Javascript function caller
 * 
 * @author Deisss (MIT License)
 * @version 0.1
 */
public class Javascript {
    private String script;
    private Reader reader;
    private ScriptEngine engine;
    private Bindings bindings;
 
    /**
     * Constructor
     * 
     * @param script The script to start
     */
    public Javascript(String script) {
        this.startEngine();
        this.setScript(script);
    }
 
    /**
     * Constructor
     * 
     * @param reader An alternative to script using Reader
     */
    public Javascript(Reader reader) {
        this.startEngine();
        this.setReader(reader);
    }
 
    /**
     * Constructor
     */
    public Javascript() {
        this.startEngine();
    }
 
    /**
     * Start engine system
     */
    private void startEngine() {
        this.script = null;
        this.reader = null;
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("javascript");
        this.bindings = this.engine.getBindings(ScriptContext.ENGINE_SCOPE);
    }
 
    /**
     * Register a new variable to use inside the system
     * 
     * @param name The name to use inside script
     * @param content The object value (can be java element)
     */
    public void variable(String name, Object content) {
        this.bindings.put(name, content);
    }
 
    /**
     * Run a single eval, without function, and return content
     * 
     * @return The object created inside your script
     * @throws ScriptException 
     */
    public Object run() throws ScriptException {
        if(this.script == null && this.reader == null) {
            return null;
        }
        if(this.reader != null) {
            return this.engine.eval(this.reader);
        } else {
            return this.engine.eval(this.script);
        }
    }
 
    /**
     * Call a function with scope binding, and get function return
     * 
     * @param fct The function name to call (should appear inside the script
     *                                                            submitted)
     * @param args The arguments to pass to this function
     * @return The javascript function return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Object run(String fct, Object... args)
            throws ScriptException, NoSuchMethodException {
 
        // Stop if not possible to run
        if(this.script == null && this.reader == null) {
            return null;
        }
        // Starting script
        this.run();
 
        // Starting invoke element
        Invocable invoke = (Invocable) this.engine;
        return invoke.invokeFunction(fct, args);
    }
 
    /**
     * Clear the script currently loaded and readers
     */
    public void clear() {
        this.reader = null;
        this.script = null;
    }
 
    /**
     * Set the script to pun inside system
     * 
     * @param script The script to run
     */
    public final void setScript(String script) {
        if(script != null && script.length() > 0) {
            this.script = script;
        }
    }
 
    /**
     * Get the script to run
     * 
     * @return The script actually setted
     */
    public String getScript() {
        return this.script;
    }
 
    /**
     * Set the reader (alternative to script)
     * 
     * @param reader The reader to use when eval js code
     */
    public final void setReader(Reader reader) {
        if(reader != null) {
            this.reader = reader;
        }
    }
 
    /**
     * Get the reader
     * 
     * @return The reader currently in use
     */
    public Reader getReader() {
        return this.reader;
    }
 
    /**
     * Get the internal bindings currently in use
     * 
     * @return The system currently in use
     */
    public Bindings getBindings() {
        return this.bindings;
    }
 
    /**
     * Get the engine currently in use
     * 
     * @return The engine currently in use
     */
    public ScriptEngine getEngine() {
        return this.engine;
    }
}