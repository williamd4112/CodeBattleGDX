package com.codebattle.model.scriptprocessor;

import com.codebattle.model.levelobject.ScriptableObject;

import javax.script.ScriptException;

public class ObjectScriptProcessor extends BaseScriptProcessor {

    private final ScriptableObject obj;

    public ObjectScriptProcessor(final ScriptableObject obj) {
        super();
        this.obj = obj;
        this.engine.put("self", obj);
    }

    @Override
    public boolean run() {
        try {
            this.engine.eval(this.script);
        } catch (final ScriptException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
