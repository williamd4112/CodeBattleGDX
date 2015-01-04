package com.codebattle.model.scriptprocessor;

import javax.script.ScriptException;

import com.codebattle.model.GameObject;

public class ObjectScriptProcessor extends BaseScriptProcessor {

    private GameObject obj;

    public ObjectScriptProcessor(GameObject obj) {
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
