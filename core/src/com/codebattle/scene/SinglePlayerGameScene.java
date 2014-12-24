package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.gui.GameSceneGUI;
import com.codebattle.model.GameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.scriptprocessor.ScriptProcessor;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.GameUtil;
import com.codebattle.utility.SoundUtil;

public class SinglePlayerGameScene extends GameScene{

	//private ScriptEditor scriptEditor;
	private GameSceneGUI gui;
	
	public SinglePlayerGameScene(String sceneName) throws Exception 
	{
		super(sceneName);
	}
	
	@Override
	public void setupGUI() 
	{
		this.gui = new GameSceneGUI(new Handler());
		this.stage.addGUI(this.gui);
	}
	
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
	
	public GameObject generateGameObject(String clazz, Owner owner, String name, String type, float x, float y) throws Exception
	{
		if(clazz.equals("GameActor")) {
			return GameActorFactory.getInstance().createGameActor(this.stage, owner, name, type, x, y);
		}else {
			return null;
		}
	}

	@Override
	public void setupInput() 
	{
		Gdx.input.setInputProcessor(this.stage);
	}

	@Override
	public void setupBGS(Element context) throws Exception 
	{
		XmlReader.Element bgsElement = context.getChildByName("bgs");
		String bgsName = bgsElement.getText();
		if(bgsName == null) return; //Default no bgs
		SoundUtil.playBGS(bgsName);
	}

	@Override
	public void setupBGM(Element context) throws Exception 
	{
		XmlReader.Element bgmElement = context.getChildByName("bgm");
		String bgmName = bgmElement.getText();
		if(bgmName == null) return; //Default no bgs
		SoundUtil.playBGM(bgmName);
	}

	/*Handling script interpretation*/
	private class Handler extends ClickListener
	{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			super.clicked(event, x, y);
			String script = gui.getEditor().getText();
			new ScriptProcessor(stage, currentPlayer, script).start();
			
		}
	}

	@Override
	public void resizeGUI(int width, int height) 
	{
		this.gui.resize(width, height);
		this.gui.invalidateHierarchy();
	}

}
