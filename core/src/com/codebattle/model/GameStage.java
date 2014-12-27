package com.codebattle.model;

import java.util.LinkedList;
import java.util.Queue;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codebattle.model.animation.Animation;
import com.codebattle.model.animation.CursorAnimation;
import com.codebattle.model.animation.GameActorAttackAnimation;
import com.codebattle.model.animation.GameObjectOnAttackAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.scene.GameScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.ShaderLoader;
import com.codebattle.utility.TextureFactory;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage 
 * */

public class GameStage extends Stage
{	
	final GameScene parent;
	private GameState state;
	private int width , height;
	
	final private TiledMap map;
	final private MapProperties mapProperties;
	
	final private OrthogonalTiledMapRenderer mapRenderer;
	final private OrthographicCamera camera;
	private ShaderProgram shader;
	private float zoom = 1.0f;
	
	//Layers
	final private Group gameObjects;
	final private Group guiLayer;
	
	/*Debug shape render*/
	final ShapeRenderer debugRender;
	
	//Animation queue, actors in the stage will add animation object to this
	//Stage will process it sequentially , not parallelly
	private Queue<Animation> animQueue;
	private Animation currentAnimation = null;

	//Used to detect blocking, when actor use its move series function
	private VirtualMap virtualMap;
	
	//Camera sliding direction
	private int camDirection = GameConstants.CAMERA_HOLD;
	
	//Lighting Variables
	private World world;
	private RayHandler rayHandler;
	
	//Assistant GUI
	private CursorAnimation cursor;
	private VirtualCell selectCell = null;
	
	/*
	 * GameStage Constructor
	 * @param - mapName : used to create map and map renderer , camera
	 * */
	public GameStage(GameScene parent, String mapName) throws Exception
	{
		super();
		this.parent = parent;
		
		//Create shader
		this.shader = ShaderLoader.loadShader("vignette");
		
		//Create map and camera
		this.map = MapFactory.loadMapFromFile(mapName);
		this.mapProperties = this.map.getProperties();
		this.camera = new OrthographicCamera();
		this.mapRenderer = new OrthogonalTiledMapRenderer(map);
		this.mapRenderer.setView(this.camera);
		this.mapRenderer.getSpriteBatch().setShader(shader);
		this.getViewport().setCamera(camera);
		
		//Create Lighting
		this.world = new World(new Vector2(0 , -10), true);
		this.rayHandler = new RayHandler(world);
		this.rayHandler.setAmbientLight(0.3f, 0.3f, 0.7f, 0.15f);
		this.rayHandler.setBlur(true);
		
		//Create all data structure 
		this.gameObjects = new Group();
		this.guiLayer = new Group();
		this.animQueue = new LinkedList<Animation>();
		
		this.virtualMap = new VirtualMap(this , map);
		
		//Add GUI layer
		this.addActor(this.guiLayer);
		
		//Create debug renderer
		this.debugRender = new ShapeRenderer();
		
		//Create Select Cursor
		this.cursor = new CursorAnimation();
		
		//Initialize game state
		this.state = GameState.PAUSE;
	}
		
	@Override
	public void draw()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	
		//Update viewport
		this.moveCamera();
		this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
		
		//Draw map
		this.mapRenderer.setView(this.camera);
		this.mapRenderer.render();
		
		//Draw debug shape
		this.renderDebug();
		
		//Draw game objects
		this.mapRenderer.getSpriteBatch().begin();
			this.gameObjects.draw(this.mapRenderer.getSpriteBatch(), 1.0f);
		this.mapRenderer.getSpriteBatch().end();
		
		//Draw and process animation
		this.processAnimation(this.mapRenderer.getSpriteBatch(), Gdx.graphics.getDeltaTime());

		//Draw lighting
		this.rayHandler.setCombinedMatrix(camera.combined);
		//this.rayHandler.updateAndRender();
		this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		
		//Draw GUI
		this.mapRenderer.getSpriteBatch().begin();
		if(this.selectCell != null) {
			if(this.selectCell.getObject() != null) {
				this.cursor.setPosition(this.selectCell.getObject().getCursorX(),
						this.selectCell.getObject().getCursorY());
				this.cursor.setVisiable(true);
				this.cursor.update();
				this.cursor.draw(this.mapRenderer.getSpriteBatch());
			}
		}
		this.mapRenderer.getSpriteBatch().end();
		super.draw();
	}

	@Override
	public void act(float delta) 
	{
		super.act(delta);
		//this.gameObjects.act(delta);
		for(Actor actor : this.gameObjects.getChildren())
			actor.act(delta);
		//this.guiLayer.act(delta);
	}

	/**
	 * Render debugging shape
	 * @param delta
	 */
	public void renderDebug()
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
					this.debugRender.setColor(Color.ORANGE.r , Color.ORANGE.g , Color.ORANGE.b , 0.2f);
					if(this.virtualMap.getCell(col, row).getObject() != null) {
						GameObject obj = this.virtualMap.getCell(col, row).getObject();
						switch(obj.getOwner()) {
						case RED:
							this.debugRender.setColor(1.0f , 0.0f , 0.0f , 0.2f);
							break;
						case BLUE:
							this.debugRender.setColor(0.0f , 0.0f , 1.0f , 0.2f);
							break;
						case GREEN:
							this.debugRender.setColor(0.0f , 1.0f , 0.0f , 0.2f);
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
		
	/**
	 * Called by game scene recursively
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height)
	{
		this.camera.position.set(width / 2 , height / 2 , 0);
		this.width = width;
		this.height = height;
		this.camera.setToOrtho(false, width * zoom, height * zoom);
		this.camera.update();
		this.getViewport().setScreenBounds(0, 0, (int)(width * zoom), (int)(height * zoom));
		
		//Setting shader
		this.shader.begin();
		this.shader.setUniformf("resolution", width, height);
		this.shader.end();
	}
	
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
	
	@Override
	public boolean scrolled(int amount) {
		return super.scrolled(amount);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 worldCoord = this.screenToStageCoordinates(new Vector2(screenX , screenY));
		int vx = (int)(worldCoord.x/32);
		int vy = (int)(worldCoord.y/32);
		
		System.out.printf("touch down(%d , %d)\n",vx ,vy);
		if(!isOutBoundInVirtualMap(vx , vy)) {
			VirtualCell cell = this.virtualMap.getCell(vx, vy);
			this.emitSelectEvent(cell);
		}
		
		return super.touchDown(screenX, screenY, pointer, button);
	}

	/**
	 * When in State Anim , call processAnimation
	 * execute the animation's action every frame until reaching its ending condition
	 * when completed, call this animation's finished method once to finish the final stage
	 * poll out this animation from queue 
	 */
	public void processAnimation(Batch batch, float delta)
	{
		//Only work at ANIM state
		if(this.state != GameState.ANIM) return;
		
		//No animation in the queue
		if(this.animQueue.isEmpty()) {
			this.setState(GameState.PAUSE);
			this.guiLayer.addAction(Actions.sequence(Actions.fadeIn(0.3f)));
			this.resetGUILayerPosition();
			this.currentAnimation = null;
			
        	//Reset the virtual map (Update new state to enter the next state
        	this.virtualMap.resetVirtualMap();
        	this.emitGUIChangeEvent();
        	
        	return;
		}
		
		//Start batch
		batch.begin();
		
		//Hide the GUI
		this.guiLayer.addAction(Actions.sequence(Actions.fadeOut(0.3f)));
		
		//Process the top animation
		Animation anim = this.animQueue.peek();
		
		//Setup
		if(anim != this.currentAnimation) {
			anim.setup();
			this.currentAnimation = anim;
		}
		
		//Update
		if(anim.isFinished()) {
			this.animQueue.poll();	
			anim.finished();
		} else {
			anim.update();
			anim.draw(batch, this.camera, delta);
		}
		
		batch.end();
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
			if (target instanceof GameActor) {
				try {
					this.addAnimation(new GameActorAttackAnimation(this, (GameActor) attacker, target));
					this.addAnimation(new GameObjectOnAttackAnimation(target));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void emitInteractEvent(GameObject contacter , int x , int y)
	{
		GameObject target = this.virtualMap.getCell(x, y).getObject();
		if(target != null)
			target.onInteract(contacter);
	}
	
	public void emitSelectEvent(VirtualCell cell)
	{
		this.selectCell = cell;
		if(this.selectCell.getObject() != null) {
			this.selectCell.getObject().onSelected(this.parent.getCurrentPlayer());
			this.setCameraTarget(this.selectCell);
		}
		this.emitGUIChangeEvent();
	}
	
	public void emitUnselectEvent()
	{
		this.selectCell = null;
		this.cursor.setVisiable(false);
		this.emitGUIChangeEvent();
	}
	
	public void emitGUIChangeEvent()
	{
		this.parent.onGUIChange();
	}
	
	/**
	 * Add elements of stage
	 */	
	public GameStage addGameObject(GameObject obj)
	{
		this.gameObjects.addActor(obj);
		this.addPointLight(obj.getX() + 16, obj.getY() + 16);
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
	
	public void addPointLight(float x , float y)
	{
		Color color = new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b ,0.85f);
		this.addPointLight(color, x, y);
	}
	
	public void addPointLight(Color color, float x , float y)
	{
		new PointLight(rayHandler, 100, color, 160, x, y);
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
	
	public GameObject getSelectedObject()
	{
		return this.selectCell.getObject();
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
	
	public void setCameraTarget(VirtualCell target)
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
		this.guiLayer.setBounds(x - (width / 2), y - (height / 2), width , height);
	}
	
	public void resetGUILayerPosition()
	{
		this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
	}
	
	public void moveCamera()
	{
		switch (this.camDirection) {
		case GameConstants.CAMERA_UP:// up
			if (this.camera.position.y < this.getMapHeight() * GameConstants.CELL_SIZE)
				this.camera.position.y += GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_DOWN:// down
			if (this.camera.position.y > 0)
				this.camera.position.y -= GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_LEFT:// left
			if (this.camera.position.x > 0)
				this.camera.position.x -= GameConstants.CAMERA_SPEED;
			break;
		case GameConstants.CAMERA_RIGHT:// right
			if (this.camera.position.x < this.getMapWidth() * GameConstants.CELL_SIZE)
				this.camera.position.x += GameConstants.CAMERA_SPEED;
			break;
		default:
			break;
		}
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
	
	public boolean isOutBoundInVirtualMap(int x , int y)
	{
		return (x < 0 || x >= this.getMapWidth() || y < 0 || y >= this.getMapHeight());
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
