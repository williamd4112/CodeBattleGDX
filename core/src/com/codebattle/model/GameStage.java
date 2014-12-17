package com.codebattle.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codebattle.utility.MapFactory;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage 
 * */

public class GameStage extends Stage{
	
	private int width , height;
	
	final OrthogonalTiledMapRenderer mapRenderer;
	final OrthographicCamera camera;
	
	final Group gameActors;
	final Group guiLayer;
	
	/*Debug shape render*/
	final ShapeRenderer debugRender;
	
	
	/*
	 * GameStage Constructor
	 * @param - mapName : used to create map and map renderer , camera
	 * */
	public GameStage(String mapName)
	{
		TiledMap map = MapFactory.loadMapFromFile(mapName);
		this.camera = new OrthographicCamera();
		this.mapRenderer = new OrthogonalTiledMapRenderer(map , this.getBatch());
		this.mapRenderer.setView(this.camera);
		this.getViewport().setCamera(camera);
		
		this.gameActors = new Group();
		this.guiLayer = new Group();
		
		this.addActor(this.gameActors);
		this.addActor(this.guiLayer);
		
		this.debugRender = new ShapeRenderer();
	}
	
	/*Called when adding a game actor to this stage*/
	public GameStage addGameActor(GameActor actor)
	{
		this.gameActors.addActor(actor);
		return this;
	}
	
	/*Called when adding a game gui to this stage*/
	public GameStage addGUI(Actor actor)
	{
		this.guiLayer.addActor(actor);
		return this;
	}
	
	public Group getGameActors()
	{
		return this.gameActors;
	}
	
	/*Called by game scene recursively
	 *@param - delta : interval of rendering*/
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.mapRenderer.setView(camera);
		this.mapRenderer.render();	
		this.renderDebug(delta);
		
	    act(delta);
	    draw();
	}
	
	/*Debug shape render*/
	public void renderDebug(float delta)
	{
		this.debugRender.setProjectionMatrix(camera.combined);
		this.debugRender.begin(ShapeType.Line);
		this.debugRender.setColor(1 , 0 , 0 , 1);
		for(int i = 0 ; i < width ; i += GameUnits.CELL_SIZE) {
			this.debugRender.line(i, 0, i, height);
		}
		this.debugRender.setColor(1 , 0 , 0 , 1);
		for(int i = 0 ; i < height ; i += GameUnits.CELL_SIZE) {
			this.debugRender.line(0, i , width, i);
		}
		this.debugRender.end();
	}
	
	
	/*Called by game scene recursively
	 *@param - width : new screen width
	 *@param - height : new screen height*/
	public void resize(int width, int height)
	{
		this.camera.position.set(width / 2 , height / 2 , 0);
		this.width = width;
		this.height = height;
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
