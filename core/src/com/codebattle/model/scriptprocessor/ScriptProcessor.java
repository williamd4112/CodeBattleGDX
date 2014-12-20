package com.codebattle.model.scriptprocessor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.codebattle.model.GameActor;
import com.codebattle.model.GameStage;
import com.codebattle.model.GameState;

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
        for (GameActor actor : this.stage.getGroupByType(GameActor.class)) {
             System.out.println(actor.getName());
             this.engine.put(actor.getName(), actor);
            
        }
    }

    @Override
    public void run() {
        try {        	
        	//Reset the virtual map
        	this.stage.getVirtualMap().resetVirtualMap();
        	
        	//Processing script (put animation object into queue
            this.engine.eval(this.script);
          
            //Processing animation (start processing animation)
            this.stage.printAllVirtualObjects();
            this.stage.setState(GameState.ANIM);
            this.stage.getVirtualMap().resetActorsCulmuSteps();
            
        } catch (final ScriptException e) {
        	System.out.println("------Exception occurred------");
        	this.stage.resetAnimQueue();
        	this.stage.getVirtualMap().resetActorsVirtualCoordinate();
        	this.stage.getVirtualMap().resetVirtualMap();
            e.printStackTrace();
        }
    }
}
