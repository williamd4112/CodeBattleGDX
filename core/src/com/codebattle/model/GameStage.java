package com.codebattle.model;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
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
	
	public enum GameState
	{
		ANIM,
		PAUSE,
	}
	
	private GameState state;
	private int width , height;
	
	final private TiledMap map;
	final private MapProperties mapProperties;
	
	final private OrthogonalTiledMapRenderer mapRenderer;
	final private OrthographicCamera camera;
	
	final Group gameActors;
	final Group guiLayer;
	
	/*Debug shape render*/
	final ShapeRenderer debugRender;
	
	//Animation queue, actors in the stage will add animation object to this
	//Stage will process it sequentially , not parallelly
	private Queue<Animation> animQueue;
	
	//Used to detect blocking, when actor use its move series function
	private boolean actorsMap[][];
	
	/*
	 * GameStage Constructor
	 * @param - mapName : used to create map and map renderer , camera
	 * */
	public GameStage(String mapName)
	{
		super();
		this.map = MapFactory.loadMapFromFile(mapName);
		this.mapProperties = this.map.getProperties();
		this.camera = new OrthographicCamera();
		this.mapRenderer = new OrthogonalTiledMapRenderer(map , this.getBatch());
		this.mapRenderer.setView(this.camera);
		this.getViewport().setCamera(camera);
		
		this.gameActors = new Group();
		this.guiLayer = new Group();
		this.animQueue = new LinkedList<Animation>();
		this.actorsMap = new boolean[this.mapProperties.get("width", Integer.class)][this.mapProperties.get("height", Integer.class)];
		
		this.addActor(this.gameActors);
		this.addActor(this.guiLayer);
		
		this.debugRender = new ShapeRenderer();
		
		this.state = GameState.PAUSE;
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
		
		switch(this.state) {
		case ANIM:
			this.processAnimation();
			break;
		default:
			break;
		}
		
	    act(delta);
	    draw();
	}
	
	/*Debug shape render*/
	public void renderDebug(float delta)
	{
		this.debugRender.setProjectionMatrix(camera.combined);
		this.debugRender.begin(ShapeType.Line);
		this.debugRender.setAutoShapeType(true);
		this.debugRender.setColor(1 , 0 , 0 , 1);
		for(int i = 0 ; i < width ; i += GameUnits.CELL_SIZE) {
			this.debugRender.line(i, 0, i, height);
		}
		this.debugRender.setColor(1 , 0 , 0 , 1);
		for(int i = 0 ; i < height ; i += GameUnits.CELL_SIZE) {
			this.debugRender.line(0, i , width, i);
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		this.debugRender.set(ShapeType.Filled);
		this.debugRender.setColor(1.0f , 0.0f , 0.0f , 0.3f);
		int mapWidth = this.mapProperties.get("width", Integer.class);
		int mapHeight = this.mapProperties.get("height", Integer.class);
		for(int row = 0 ; row < mapHeight ; row++) {
			for(int col = 0 ; col < mapWidth ; col++) {
				if(this.actorsMap[row][col]) {  
					float x = col * GameUnits.CELL_SIZE;
					float y = row * GameUnits.CELL_SIZE;
					this.debugRender.rect(x, y, GameUnits.CELL_SIZE, GameUnits.CELL_SIZE);
				}
			}
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
	
	/**
	 * Initialize actors map when start to process actor's movement (must be called before process script
	 */
	public void initActorsMap()
	{
		int mapWidth = this.mapProperties.get("width", Integer.class);
		int mapHeight = this.mapProperties.get("height", Integer.class);
		for(int row = 0 ; row < mapHeight ; row++) {
			for(int col = 0 ; col < mapWidth ; col++) {
				this.actorsMap[row][col] = false;
			}
		}
		
		for(Actor actor : this.gameActors.getChildren()) {
			int cellRow = (int) (actor.getY() / GameUnits.CELL_SIZE);
			int cellCol = (int) (actor.getX() / GameUnits.CELL_SIZE);
			this.actorsMap[cellRow][cellCol] = true;
		}
	}
	
	/**
	 * Update actors map when each 1-cell movement
	 * @param lastX
	 * @param lastY
	 * @param newX
	 * @param newY
	 */
	public void updateActorsMap(int lastX , int lastY , int newX , int newY)
	{
		this.actorsMap[lastY][lastX] = false;
		this.actorsMap[newY][newX] = true;
	}
	
	/**
	 * When in State Anim , call processAnimation
	 */
	public void processAnimation()
	{
		if(this.animQueue.isEmpty()) {
			this.setState(GameState.PAUSE);
			return;
		}
		
		Animation anim = this.animQueue.peek();
		if(anim.isFinished()) {
			this.animQueue.poll();
		}else {
			anim.update();
		}
	}
	
	
	/**
	 * Add elements of stage
	 */
	public GameStage addGameActor(GameActor actor)
	{
		this.gameActors.addActor(actor);
		return this;
	}
	
	public GameStage addGUI(Actor actor)
	{
		this.guiLayer.addActor(actor);
		return this;
	}
	
	public void addAnimation(Animation animation)
	{
		this.animQueue.add(animation);
	}
	
	/**
	 * Getters
	 */
	public Group getGameActors()
	{
		return this.gameActors;
	}
	
	public boolean[][] getActorsMap()
	{
		return this.actorsMap;
	}
	
	/**
	 * Setters
	 */
	public void setState(GameState state)
	{
		this.state = state;
	}
	
	/**
	 * Debugging
	 */
	public void printAllAnimation()
	{
		for(Animation anim : this.animQueue) {
			System.out.println(anim);
		}
	}
}
