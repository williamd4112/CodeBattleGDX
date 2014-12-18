package com.codebattle.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.model.GameStage.GameState;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Script processor.
 *
 * 1. Pass a stage so that the processor knows where it should do operation.
 * 2. Create a new thread to run all commands.
 */
public class ScriptProcessor extends Thread {
    private final ScriptEngineManager manager;
    private final ScriptEngine engine;
    private final GameStage stage;
    private final String script;

    public ScriptProcessor(final GameStage stage, final String script) {
        this.setDaemon(true);
        this.script = script;
        this.stage = stage;
        this.manager = new ScriptEngineManager();
        this.engine = this.manager.getEngineByExtension("js");
        
        System.out.println("ScriptProcessor put object : ");
        for (final Actor actor : this.stage.getGameActors()
                .getChildren()) {
            if (actor instanceof GameActor) {
                System.out.println(actor.getName());
                this.engine.put(actor.getName(), actor);
            }
        }
    }

    @Override
    public void run() {
        try {
        	this.stage.initActorsMap();
        	//Processing script (put animation object into queue
            this.engine.eval(this.script);
            
            //Processing animation (start processing animation)
            this.stage.printAllAnimation();
            this.stage.setState(GameState.ANIM);
        } catch (final ScriptException e) {
            e.printStackTrace();
        }
    }
}
