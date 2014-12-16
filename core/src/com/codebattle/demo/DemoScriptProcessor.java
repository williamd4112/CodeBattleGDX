package com.codebattle.demo;

import com.badlogic.gdx.scenes.scene2d.Stage;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DemoScriptProcessor {
    private final ScriptEngineManager manager;
    private final ScriptEngine engine;
    private final Stage stage;

    public DemoScriptProcessor(final Stage stage) {
        this.manager = new ScriptEngineManager();
        this.engine = this.manager.getEngineByExtension("js");
        this.stage = stage;
        this.engine.put("game_objects", stage.getActors());
    }

    public void execute(final String script) {
        final Thread updatingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    DemoScriptProcessor.this.engine.eval(script);
                } catch (final ScriptException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                updatingThread.start();
                // TODO : avoid infinite loop

            }
        }).start();
    }
}
