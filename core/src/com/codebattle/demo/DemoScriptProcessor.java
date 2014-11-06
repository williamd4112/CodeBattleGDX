package com.codebattle.demo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class DemoScriptProcessor {
	
	final private ScriptEngineManager manager;
	final private ScriptEngine engine;
	final private Stage stage;
	
	public DemoScriptProcessor(Stage stage)
	{
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension("js");
        this.stage = stage;
        this.engine.put("game_objects", stage.getActors());
	}
	
	public void execute(final String script)
	{
		final Thread updatingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 try {
					 engine.eval(script);
				} catch (ScriptException e) {
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
				//TODO : avoid infinite loop

			}
			
		}).start();
	}
	
	
}
