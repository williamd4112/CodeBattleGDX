package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.gui.ScriptEditor;
import com.codebattle.model.GameActor;
import com.codebattle.model.GameStage;

/*
 * GameScene:
 * 	1.A game stage to manage all game objects and map in the scene. And a tiled map
 *	2.A Script Processor process all object operation in blocked mode
 *  3.A background thread to update map object in sync	
 * */

public class GameScene implements Screen{
	
	final GameStage stage;
	final ScriptEditor scriptEditor;
	
	GameActor a;
	
	public GameScene(String mapName)
	{
		this.stage = new GameStage(mapName);
		this.scriptEditor = new ScriptEditor(new Handler());
		this.stage.addActor(this.scriptEditor);
		
		//Test segment
		a = new GameActor("001-Fighter01.png" , 320 , 240);
		this.stage.addActor(a);
		
		Gdx.input.setInputProcessor(this.stage);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		this.stage.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		this.stage.resize(width, height);
		this.scriptEditor.resize(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.stage.dispose();
	}
	
	private class Handler extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			// TODO Auto-generated method stub
			super.clicked(event, x, y);
			String script = scriptEditor.getScript();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						a.turn(GameActor.Direction.LEFT.value);
						while(a.getX() > 0) {
							a.moveBy(-0.2f, 0);
							Thread.sleep(5);
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			}).start();
			
		}
	}
		
}
	