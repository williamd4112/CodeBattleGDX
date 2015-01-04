package com.codebattle.model.scriptprocessor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public abstract class BaseScriptProcessor {
    protected final ScriptEngineManager manager;
    protected final ScriptEngine engine;
    protected String script = "";

    public BaseScriptProcessor() {
        this.manager = new ScriptEngineManager();
        this.engine = this.manager.getEngineByExtension("js");
    }

    public void setScript(String script) {
        this.script = script;
    }

    abstract public void run();
}
