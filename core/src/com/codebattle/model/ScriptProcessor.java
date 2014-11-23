package com.codebattle.model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;


/*
 * ScriptProcessor
 * 1.pass a stage so that the processor knows where it should do operation
 * 2.create a new thread to run all commands 
 * */

public class ScriptProcessor extends Thread{
	final private ScriptEngineManager manager;
	final private ScriptEngine engine;
	final private GameStage stage;
	final String script;
	
	public ScriptProcessor(GameStage stage , String script)
	{
		this.setDaemon(true);
		this.script = script;
        this.stage = stage;
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension("js");
                
        for(Actor actor : this.stage.getGameActors().getChildren()) {
        	if(actor instanceof GameActor) {
        		System.out.println("ScriptProcessor put object : " + actor.getName());
        		this.engine.put(actor.getName(), (GameActor)actor);
        	}
        }
	}
	
	public void run()
	{
		try {
			 engine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
}
