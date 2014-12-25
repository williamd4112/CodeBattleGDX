package com.codebattle.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.utility.GameConstants;
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
	
	//Current player
	protected Owner currentPlayer = Owner.RED;
	
	/**
	 * GameScene all have a GameStage, a ScriptEditor
	 * @param sceneName
	 * @throws Exception 
	 */
	public GameScene(String sceneName) throws Exception
	{
		XmlReader.Element context = XMLUtil.readXMLFromFile(GameConstants.SCENE_DIR + sceneName + ".xml");
		String mapName = context.getChildByName("map").getText();
		
		this.stage = new GameStage(this, mapName);
		this.setupGUI();
		this.setupGameObjects(context);
		this.setupInput();
		this.setupBGS(context);
		this.setupBGM(context);
		
		this.stage.getVirtualMap().resetVirtualMap();
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		this.stage.draw();
		this.stage.act(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		this.stage.resize(width, height);
		this.resizeGUI(width, height);
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
	
	public void setCurrentPlayer(Owner owner)
	{
		this.currentPlayer = owner;
	}
	
	public Owner getCurrentPlayer()
	{
		return this.currentPlayer;
	}
	
	abstract public void setupInput();
	abstract public void setupGUI();
	abstract public void setupGameObjects(XmlReader.Element context) throws Exception;
	abstract public void setupBGS(XmlReader.Element context) throws Exception;
	abstract public void setupBGM(XmlReader.Element context) throws Exception;
	abstract public void resizeGUI(int width, int height);
	abstract public void onGUIChange();

}
