package com.codebattle.model.scriptprocessor;

import com.codebattle.model.VirtualCell;

import javax.script.ScriptException;

public class CellScriptProcessor extends BaseScriptProcessor {

    final private VirtualCell cell;

    public CellScriptProcessor(final VirtualCell cell) {
        super();
        this.cell = cell;
        this.engine.put("self", cell);
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
