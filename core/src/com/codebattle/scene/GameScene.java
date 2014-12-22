package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.gui.ScriptEditor;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.scriptprocessor.ScriptProcessor;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameUtil;
import com.codebattle.utility.XMLUtil;

/*
 * GameScene:
 * 	1.A game stage to manage all game objects and map in the scene. And a tiled map
 *	2.A Script Processor process all object operation in blocked mode
 *  3.A background thread to update map object in sync	
 * */

abstract public class GameScene implements Screen
{
	final public GameStage stage;
	final public ScriptEditor scriptEditor;
	
	private Owner currentPlayer;
	
	/**
	 * GameScene all have a GameStage, a ScriptEditor
	 * @param sceneName
	 * @throws Exception 
	 */
	public GameScene(String sceneName) throws Exception
	{
		XmlReader.Element context = XMLUtil.readXMLFromFile(GameConstants.SCENE_DIR + sceneName + ".xml");
		String mapName = context.getChildByName("map").getText();
		
		this.stage = new GameStage(mapName);
		
		this.scriptEditor = new ScriptEditor(new Handler());
		this.stage.addGUI(this.scriptEditor);
		
		this.setupGameObjects(context);
		
		this.stage.getVirtualMap().resetVirtualMap();
		this.currentPlayer = Owner.RED;
		
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
	
	/**
	 * setup game objects by reading the scene file
	 * @param context
	 * @throws Exception
	 */
	public void setupGameObjects(XmlReader.Element context) throws Exception
	{
		for(XmlReader.Element element : context.getChildrenByNameRecursively("gameobject")) {
			String clazz = element.getAttribute("class");
			Owner owner = GameUtil.toOwner(element.getAttribute("owner"));
			String name = element.getAttribute("name");
			String type = element.getAttribute("type");
			float x = Float.parseFloat(element.getAttribute("x"));
			float y = Float.parseFloat(element.getAttribute("y"));
			
			GameObject obj = this.generateGameObject(clazz, owner, name, type, x, y);
			this.stage.addGameObject(obj);
		}
	}
	
	/**
	 * generate a game object according the spec read from scene file
	 * @param clazz
	 * @param owner
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	public GameObject generateGameObject(String clazz, Owner owner, String name, String type, float x, float y) throws Exception
	{
		if(clazz.equals("GameActor")) {
			return GameActorFactory.getInstance().createGameActor(this.stage, owner, name, type, x, y);
		}else {
			return null;
		}
	}
	
	/*Handling script interpretation*/
	private class Handler extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			// TODO Auto-generated method stub
			super.clicked(event, x, y);
			String script = scriptEditor.getScript();
			new ScriptProcessor(stage, currentPlayer, script).start();
			
		}
	}
}
