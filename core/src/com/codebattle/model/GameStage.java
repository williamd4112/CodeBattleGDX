package com.codebattle.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codebattle.utility.MapFactory;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage 
 * */

public class GameStage extends Stage{
	
	final OrthogonalTiledMapRenderer mapRenderer;
	final OrthographicCamera camera;
	
	/*
	 * GameStage Constructor
	 * @param - mapName : used to create map and map renderer , camera
	 * */
	public GameStage(String mapName)
	{
		TiledMap map = MapFactory.loadMapFromFile(mapName);
		this.mapRenderer = new OrthogonalTiledMapRenderer(map , this.getBatch());
		this.camera = new OrthographicCamera();
		this.getViewport().setCamera(camera);
	}
	
	/*Called by game scene recursively
	 *@param - delta : interval of rendering*/
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.mapRenderer.setView(camera);
		this.mapRenderer.render();
		
	    act(delta);
	    draw();
	}
	
	/*Called by game scene recursively
	 *@param - width : new screen width
	 *@param - height : new screen height*/
	public void resize(int width, int height)
	{
		this.camera.position.set(width / 2 , height / 2 , 0);
		this.camera.setToOrtho(false, width, height);
		this.camera.update();
		this.getViewport().setScreenBounds(0, 0, width, height);
	}
	
	/*Called by game scene recursively*/
	@Override
	public void dispose() 
	{
		super.dispose();
		this.mapRenderer.dispose();
	}
}
