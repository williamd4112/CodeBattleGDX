package com.codebattle.model.scriptprocessor;

import javax.script.ScriptException;

import com.codebattle.model.levelobject.ScriptableObject;

public class ObjectScriptProcessor extends BaseScriptProcessor {

    private ScriptableObject obj;

    public ObjectScriptProcessor(ScriptableObject obj) {
        super();
        this.obj = obj;
        this.engine.put("self", obj);
    }

    @Override
    public void run() {
        try {
            this.engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}
