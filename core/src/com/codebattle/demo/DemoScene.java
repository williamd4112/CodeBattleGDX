package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class DemoScene implements Screen{
	
	final Stage stage;
	final OrthogonalTiledMapRenderer mapRenderer;
	final OrthographicCamera camera;
	final DemoScriptProcessor scriptProcessor;
	
	DemoObject obj;
	DemoUIGroup ui;
	
	public DemoScene(String mapName)
	{
		TiledMap map = loadMapFromFile(mapName);
		
		stage = new Stage();
		scriptProcessor = new DemoScriptProcessor(stage);
		mapRenderer = new OrthogonalTiledMapRenderer(map , stage.getBatch());
		camera = new OrthographicCamera();
		ui = new DemoUIGroup();
		obj = new DemoObject("001-Fighter01.png");
		stage.getViewport().setCamera(camera);
		ui.setTouchable(Touchable.disabled);
		stage.addActor(obj);	
		stage.addActor(ui);
		obj.setX(320);
		obj.setY(240);
		obj.setTouchable(Touchable.enabled);
		Vector2 coordinate = stage.screenToStageCoordinates(new Vector2(obj.getCenterX(),obj.getCenterY()));
		Actor hit = stage.hit(coordinate.x,coordinate.y,true);
		System.out.println(hit);
		Gdx.input.setInputProcessor(stage);
		
	}
	
	public TiledMap loadMapFromFile(String mapName)
	{
		TiledMap map = new TmxMapLoader().load("level/" + mapName + ".tmx");
		
		return (map == null) ? null : map;
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
	    stage.act(delta);
	    stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		camera.position.set(width / 2 , height / 2 , 0);
		camera.setToOrtho(false, width, height);
		camera.update();
		stage.getViewport().setScreenBounds(0, 0, width, height);
		ui.resize(width, height);
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
		stage.dispose();
		mapRenderer.dispose();

	}

}
