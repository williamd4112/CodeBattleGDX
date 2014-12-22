package com.codebattle.model;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.codebattle.model.animation.Animation;
import com.codebattle.model.animation.GameActorAttackAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ShaderLoader;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage 
 * */

public class GameStage extends Stage
{	
	private GameState state;
	private int width , height;
	
	final private TiledMap map;
	final private MapProperties mapProperties;
	
	final private OrthogonalTiledMapRenderer mapRenderer;
	final private OrthographicCamera camera;
	
	final private Group gameObjects;
	final private Group guiLayer;
	
	/*Debug shape render*/
	final ShapeRenderer debugRender;
	
	//Animation queue, actors in the stage will add animation object to this
	//Stage will process it sequentially , not parallelly
	private Queue<Animation> animQueue;
	
	//Used to detect blocking, when actor use its move series function
	private VirtualMap virtualMap;
	
	//Camera sliding direction
	private int camDirection = GameConstants.CAMERA_HOLD;
	
	/*
	 * GameStage Constructor
	 * @param - mapName : used to create map and map renderer , camera
	 * */
	public GameStage(String mapName)
	{
		super();
		
		ShaderProgram shader = new ShaderProgram(ShaderLoader.loadVertexShader("invert"), ShaderLoader.loadFragShader("invert"));
		
		this.map = MapFactory.loadMapFromFile(mapName);
		this.mapProperties = this.map.getProperties();
		this.camera = new OrthographicCamera();
		this.mapRenderer = new OrthogonalTiledMapRenderer(map);
		this.mapRenderer.setView(this.camera);
		//this.mapRenderer.getSpriteBatch().setShader(shader);
		this.getViewport().setCamera(camera);
		
		this.gameObjects = new Group();
		this.guiLayer = new Group();
		this.animQueue = new LinkedList<Animation>();
		this.virtualMap = new VirtualMap(this , map);
		
		this.addActor(this.gameObjects);
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
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		this.moveCamera();
		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();	
		this.renderDebug(delta);
		
	    act(delta);
	    draw();
	    
		switch(this.state) {
		case ANIM:
			this.processAnimation();
			break;
		default:
			break;
		}
	}
	
	/*Debug shape render*/
	public void renderDebug(float delta)
	{
		this.debugRender.setProjectionMatrix(camera.combined);
		this.debugRender.begin(ShapeType.Line);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		this.debugRender.setAutoShapeType(true);
		this.debugRender.setColor(1 , 0 , 0 , 0.2f);
		
		for(int i = 0 ; i < this.getMapWidth() * GameConstants.CELL_SIZE ; i += GameConstants.CELL_SIZE) 
			this.debugRender.line(i, 0, i, this.getMapHeight() * GameConstants.CELL_SIZE  );
		for(int i = 0 ; i < this.getMapHeight() * GameConstants.CELL_SIZE ; i += GameConstants.CELL_SIZE) 
			this.debugRender.line(0, i ,  this.getMapWidth() * GameConstants.CELL_SIZE , i);
		

		this.debugRender.set(ShapeType.Filled);
		
		int mapWidth = this.mapProperties.get("width", Integer.class);
		int mapHeight = this.mapProperties.get("height", Integer.class);
		for(int row = 0 ; row < mapHeight ; row++) {
			for(int col = 0 ; col < mapWidth ; col++) {
				if(!this.virtualMap.isPassiable(col , row)) {
					this.debugRender.setColor(Color.ORANGE.r , Color.ORANGE.g , Color.ORANGE.b , 0.4f);
					if(this.virtualMap.getCell(col, row).getObject() != null) {
						GameObject obj = this.virtualMap.getCell(col, row).getObject();
						switch(obj.getOwner()) {
						case RED:
							this.debugRender.setColor(1.0f , 0.0f , 0.0f , 0.3f);
							break;
						case BLUE:
							this.debugRender.setColor(0.0f , 0.0f , 1.0f , 0.3f);
							break;
						case GREEN:
							this.debugRender.setColor(0.0f , 1.0f , 0.0f , 0.3f);
							break;
						default:
							break;
						}
					}
					float x = col * GameConstants.CELL_SIZE;
					float y = row * GameConstants.CELL_SIZE;
					this.debugRender.rect(x, y, GameConstants.CELL_SIZE, GameConstants.CELL_SIZE);
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
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoordinates);
		
		this.isMouseOutBound(worldCoordinates.x, worldCoordinates.y);
		
		return super.mouseMoved(screenX, screenY);
	}
	
	/**
	 * When in State Anim , call processAnimation
	 * execute the animation's action every frame until reaching its ending condition
	 * when completed, call this animation's finished method once to finish the final stage
	 * poll out this animation from queue 
	 */
	public void processAnimation()
	{
		//No animation in the queue
		if(this.animQueue.isEmpty()) {
			this.setState(GameState.PAUSE);
			this.setGUIVisiable(true);
			this.resetGUILayerPosition();
			return;
		}
		
		//Hide the GUI
		this.setGUIVisiable(false);
		
		//Process the top animation
		Animation anim = this.animQueue.peek();
		if(anim.isFinished()) {
			this.animQueue.poll();
			anim.finished();
		}else {
			anim.update();
		}
	}
	
	public void resetAnimQueue()
	{
		this.animQueue.clear();
	}
	
	/**
	 * Event handling
	 */	
	public void emitAttackEvent(GameObject attacker , int x , int y)
	{
		GameObject target = this.virtualMap.getCell(x, y).getObject();
		if(target != null) {
			target.onAttacked(attacker);
			if (target instanceof GameActor)
				this.addAnimation(new GameActorAttackAnimation(this, (GameActor) attacker, target));
		}
	}
	
	public void emitInteractEvent(GameObject contacter , int x , int y)
	{
		GameObject target = this.virtualMap.getCell(x, y).getObject();
		if(target != null)
			target.onInteract(contacter);
	}
	
	/**
	 * Add elements of stage
	 */
	public GameStage addGameActor(GameActor actor)
	{
		this.gameObjects.addActor(actor);
		return this;
	}
	
	public GameStage addGameObject(GameObject obj)
	{
		this.gameObjects.addActor(obj);
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
	@SuppressWarnings("unchecked")
	public <T> SnapshotArray<T> getGroupByType(Class<T> type)
	{
		SnapshotArray<T> objs = new SnapshotArray<T>();
		for(Actor actor : this.gameObjects.getChildren()) {
			if(type.isInstance(actor))
				objs.add((T)actor);
		}
		
		return objs;
	}
	
	public SnapshotArray<GameObject> getGameObjectsByOwner(Owner owner)
	{
		SnapshotArray<GameObject> objs = new SnapshotArray<GameObject>();
		for(Actor actor : this.gameObjects.getChildren()) {
			if(actor instanceof GameObject) {
				GameObject obj = (GameObject)actor;
				if(obj.getOwner() == owner) 
					objs.add(obj);
			}
		}
		return objs;
	}
	
	public int getMapWidth()
	{
		return this.mapProperties.get("width", Integer.class);
	}
	
	public int getMapHeight()
	{
		return this.mapProperties.get("height", Integer.class);
	}
	
	public VirtualMap getVirtualMap()
	{
		return this.virtualMap;
	}
	
	/**
	 * Setters
	 */
	public void setState(GameState state)
	{
		this.state = state;
	}
	
	public void setCameraTarget(GameObject target)
	{
		this.camera.position.set(target.getX() , target.getY() , 0);
		this.camera.update();
	}
	
	public void setGUIVisiable(boolean flag)
	{
		this.guiLayer.setVisible(flag);
	}
		
	public void setGUILayerPosition(float x , float y)
	{
		this.guiLayer.setBounds(x - (width / 2), y - (height / 2), width, height);
	}
	
	public void resetGUILayerPosition()
	{
		this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
	}
	
	public void moveCamera()
	{
		switch (this.camDirection) {
		case GameConstants.CAMERA_UP:// up
			this.camera.position.y += GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_DOWN:// down
			this.camera.position.y -= GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_LEFT:// left
			this.camera.position.x -= GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_RIGHT:// right
			this.camera.position.x += GameConstants.CAMERA_SPEED;
			break;
		default:
			break;
		}
		this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
	}
	
	public void removeGameObject(GameObject obj)
	{
		this.gameObjects.removeActor(obj);
	}
	
	/**
	 * Condition checkingS
	 */
	public void isMouseOutBound(float x , float y)
	{
		int cx = (int) this.camera.position.x;
		int cy = (int) this.camera.position.y;

		// Up
		if (y >= cy + (height / 2 - GameConstants.CAMERA_SLIDING_MARGIN))
			this.camDirection = GameConstants.CAMERA_UP;
		// Down
		else if (y <= cy - (height / 2 - GameConstants.CAMERA_SLIDING_MARGIN))
			this.camDirection = GameConstants.CAMERA_DOWN;
		// Left
		else if (x <= cx - (width / 2 - GameConstants.CAMERA_SLIDING_MARGIN))
			this.camDirection = GameConstants.CAMERA_LEFT;
		// Right
		else if (x >= cx + (width / 2 - GameConstants.CAMERA_SLIDING_MARGIN))
			this.camDirection = GameConstants.CAMERA_RIGHT;
		else
			this.camDirection = GameConstants.CAMERA_HOLD;
		
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
	
	public void printAllVirtualObjects()
	{
		int mapWidth = this.mapProperties.get("width", Integer.class);
		int mapHeight = this.mapProperties.get("height", Integer.class);
		for(int row = 0 ; row < mapHeight ; row++) {
			for(int col = 0 ; col < mapWidth ; col++) {
				if(this.virtualMap.getCell(col, row).getObject() != null) {
					System.out.printf("(%d , %d) is %s\n", col,row,this.virtualMap.getCell(col, row).getObject().getName());
				}
			}
		}
	}
	
}
