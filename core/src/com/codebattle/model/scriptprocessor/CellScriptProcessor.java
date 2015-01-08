package com.codebattle.model.scriptprocessor;

import javax.script.ScriptException;

import com.codebattle.model.VirtualCell;

public class CellScriptProcessor extends BaseScriptProcessor {

    final private VirtualCell cell;

    public CellScriptProcessor(VirtualCell cell) {
        super();
        this.cell = cell;
        this.engine.put("self", cell);
    }

    @Override
    public boolean run() {
        try {
            this.engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
