package com.codebattle.model;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.badlogic.gdx.utils.SnapshotArray;
import com.codebattle.model.animation.Animation;
import com.codebattle.model.animation.GameActorAttackAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ShaderLoader;

import java.util.LinkedList;
import java.util.Queue;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage
 * */

public class GameStage extends Stage {
    private GameState state;
    private int width, height;

    final private TiledMap map;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;
    private final ShaderProgram shader;
    private final float zoom = 1.0f;

    final private Group gameObjects;
    final private Group guiLayer;

    /* Debug shape render */
    final ShapeRenderer debugRender;

    // Animation queue, actors in the stage will add animation object to this
    // Stage will process it sequentially , not parallelly
    private final Queue<Animation> animQueue;
    private Animation currentAnimation = null;

    // Used to detect blocking, when actor use its move series function
    private final VirtualMap virtualMap;

    // Camera sliding direction
    private int camDirection = GameConstants.CAMERA_HOLD;

    // Lighting Variables
    private final World world;
    private final RayHandler rayHandler;

    /*
     * GameStage Constructor
     * 
     * @param - mapName : used to create map and map renderer , camera
     */
    public GameStage(final String mapName) {
        super();
        // Create shader
        this.shader = ShaderLoader.loadShader("vignette");

        // Create map and camera
        this.map = MapFactory.loadMapFromFile(mapName);
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.getSpriteBatch()
                .setShader(this.shader);
        this.getViewport()
                .setCamera(this.camera);

        // Create Lighting
        this.world = new World(new Vector2(0, -10), true);
        this.rayHandler = new RayHandler(this.world);
        this.rayHandler.setAmbientLight(0.3f, 0.3f, 0.7f, 0.15f);
        this.rayHandler.setBlur(true);

        // Create all data structure
        this.gameObjects = new Group();
        this.guiLayer = new Group();
        this.animQueue = new LinkedList<Animation>();

        this.virtualMap = new VirtualMap(this, this.map);

        // Add GUI layer
        this.addActor(this.guiLayer);

        // Create debug renderer
        this.debugRender = new ShapeRenderer();

        // Initialize game state
        this.state = GameState.PAUSE;
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Update viewport
        this.moveCamera();
        this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);

        // Draw map
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        // Draw debug shape
        this.renderDebug();

        // Draw game objects
        this.mapRenderer.getSpriteBatch()
                .begin();
        this.gameObjects.draw(this.mapRenderer.getSpriteBatch(), 1.0f);
        this.mapRenderer.getSpriteBatch()
                .end();

        // Draw and process animation
        this.processAnimation(this.mapRenderer.getSpriteBatch(), Gdx.graphics.getDeltaTime());

        // Draw lighting
        this.rayHandler.setCombinedMatrix(this.camera.combined);
        this.rayHandler.updateAndRender();
        this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        // Draw GUI
        super.draw();
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        this.gameObjects.act(delta);
        this.guiLayer.act(delta);
    }

    /**
     * Render debugging shape
     * @param delta
     */
    public void renderDebug() {
        this.debugRender.setProjectionMatrix(this.camera.combined);
        this.debugRender.begin(ShapeType.Line);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.debugRender.setAutoShapeType(true);
        this.debugRender.setColor(1, 0, 0, 0.2f);

        for (int i = 0; i < this.getMapWidth() * GameConstants.CELL_SIZE; i += GameConstants.CELL_SIZE) {
            this.debugRender.line(i, 0, i, this.getMapHeight() * GameConstants.CELL_SIZE);
        }
        for (int i = 0; i < this.getMapHeight() * GameConstants.CELL_SIZE; i += GameConstants.CELL_SIZE) {
            this.debugRender.line(0, i, this.getMapWidth() * GameConstants.CELL_SIZE, i);
        }

        this.debugRender.set(ShapeType.Filled);

        final int mapWidth = this.mapProperties.get("width", Integer.class);
        final int mapHeight = this.mapProperties.get("height", Integer.class);
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                if (!this.virtualMap.isPassiable(col, row)) {
                    this.debugRender.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 0.2f);
                    if (this.virtualMap.getCell(col, row)
                            .getObject() != null) {
                        final GameObject obj = this.virtualMap.getCell(col, row)
                                .getObject();
                        switch (obj.getOwner()) {
                        case RED:
                            this.debugRender.setColor(1.0f, 0.0f, 0.0f, 0.2f);
                            break;
                        case BLUE:
                            this.debugRender.setColor(0.0f, 0.0f, 1.0f, 0.2f);
                            break;
                        case GREEN:
                            this.debugRender.setColor(0.0f, 1.0f, 0.0f, 0.2f);
                            break;
                        default:
                            break;
                        }
                    }
                    final float x = col * GameConstants.CELL_SIZE;
                    final float y = row * GameConstants.CELL_SIZE;
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
    public void resize(final int width, final int height) {
        this.camera.position.set(width / 2, height / 2, 0);
        this.width = width;
        this.height = height;
        this.camera.setToOrtho(false, width * this.zoom, height * this.zoom);
        this.camera.update();
        this.getViewport()
                .setScreenBounds(0, 0, (int) (width * this.zoom), (int) (height * this.zoom));

        // Setting shader
        this.shader.begin();
        this.shader.setUniformf("resolution", width, height);
        this.shader.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.mapRenderer.dispose();
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        final Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        this.camera.unproject(worldCoordinates);

        this.isMouseOutBound(worldCoordinates.x, worldCoordinates.y);

        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(final int amount) {
        return super.scrolled(amount);
    }

    /**
     * When in State Anim , call processAnimation
     * execute the animation's action every frame until reaching its ending condition
     * when completed, call this animation's finished method once to finish the final stage
     * poll out this animation from queue 
     */
    public void processAnimation(final Batch batch, final float delta) {
        // Only work at ANIM state
        if (this.state != GameState.ANIM) {
            return;
        }

        // No animation in the queue
        if (this.animQueue.isEmpty()) {
            this.setState(GameState.PAUSE);
            this.setGUIVisiable(true);
            this.resetGUILayerPosition();
            this.currentAnimation = null;
            return;
        }

        // Start batch
        batch.begin();

        // Hide the GUI
        this.setGUIVisiable(false);

        // Process the top animation
        final Animation anim = this.animQueue.peek();

        // Setup
        if (anim != this.currentAnimation) {
            anim.setup();
            this.currentAnimation = anim;
        }

        // Update
        if (anim.isFinished()) {
            this.animQueue.poll();
            anim.finished();
        } else {
            anim.update();
            anim.draw(batch, this.camera, delta);
        }

        batch.end();
    }

    public void resetAnimQueue() {
        this.animQueue.clear();
    }

    /**
     * Event handling
     */
    public void emitAttackEvent(final GameObject attacker, final int x, final int y) {
        final GameObject target = this.virtualMap.getCell(x, y)
                .getObject();
        if (target != null) {
            target.onAttacked(attacker);
            if (target instanceof GameActor) {
                try {
                    this.addAnimation(new GameActorAttackAnimation(this, (GameActor) attacker,
                            target));
                } catch (final Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void emitInteractEvent(final GameObject contacter, final int x, final int y) {
        final GameObject target = this.virtualMap.getCell(x, y)
                .getObject();
        if (target != null) {
            target.onInteract(contacter);
        }
    }

    /**
     * Add elements of stage
     */
    public GameStage addGameObject(final GameObject obj) {
        this.gameObjects.addActor(obj);
        this.addPointLight(obj.getX() + 16, obj.getY() + 16);
        return this;
    }

    public GameStage addGUI(final Actor actor) {
        this.guiLayer.addActor(actor);
        return this;
    }

    public void addAnimation(final Animation animation) {
        this.animQueue.add(animation);
    }

    public void addPointLight(final float x, final float y) {
        final Color color = new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 0.85f);
        this.addPointLight(color, x, y);
    }

    public void addPointLight(final Color color, final float x, final float y) {
        new PointLight(this.rayHandler, 100, color, 160, x, y);
    }

    /**
     * Getters
     */
    @SuppressWarnings("unchecked")
    public <T> SnapshotArray<T> getGroupByType(final Class<T> type) {
        final SnapshotArray<T> objs = new SnapshotArray<T>();
        for (final Actor actor : this.gameObjects.getChildren()) {
            if (type.isInstance(actor)) {
                objs.add((T) actor);
            }
        }

        return objs;
    }

    public SnapshotArray<GameObject> getGameObjectsByOwner(final Owner owner) {
        final SnapshotArray<GameObject> objs = new SnapshotArray<GameObject>();
        for (final Actor actor : this.gameObjects.getChildren()) {
            if (actor instanceof GameObject) {
                final GameObject obj = (GameObject) actor;
                if (obj.getOwner() == owner) {
                    objs.add(obj);
                }
            }
        }
        return objs;
    }

    public int getMapWidth() {
        return this.mapProperties.get("width", Integer.class);
    }

    public int getMapHeight() {
        return this.mapProperties.get("height", Integer.class);
    }

    public VirtualMap getVirtualMap() {
        return this.virtualMap;
    }

    /**
     * Setters
     */
    public void setState(final GameState state) {
        this.state = state;
    }

    public void setCameraTarget(final GameObject target) {
        this.camera.position.set(target.getX(), target.getY(), 0);
        this.camera.update();
    }

    public void setGUIVisiable(final boolean flag) {
        this.guiLayer.setVisible(flag);
    }

    public void setGUILayerPosition(final float x, final float y) {
        this.guiLayer.setBounds(x - this.width / 2, y - this.height / 2, this.width, this.height);
    }

    public void resetGUILayerPosition() {
        this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
    }

    public void moveCamera() {
        switch (this.camDirection) {
        case GameConstants.CAMERA_UP:// up
            if (this.camera.position.y < this.getMapHeight() * GameConstants.CELL_SIZE) {
                this.camera.position.y += GameConstants.CAMERA_SPEED;
            }
            break;
        case GameConstants.CAMERA_DOWN:// down
            if (this.camera.position.y > 0) {
                this.camera.position.y -= GameConstants.CAMERA_SPEED;
            }
            break;
        case GameConstants.CAMERA_LEFT:// left
            if (this.camera.position.x > 0) {
                this.camera.position.x -= GameConstants.CAMERA_SPEED;
            }
            break;
        case GameConstants.CAMERA_RIGHT:// right
            if (this.camera.position.x < this.getMapWidth() * GameConstants.CELL_SIZE) {
                this.camera.position.x += GameConstants.CAMERA_SPEED;
            }
            break;
        default:
            break;
        }
    }

    public void removeGameObject(final GameObject obj) {
        this.gameObjects.removeActor(obj);
    }

    /**
     * Condition checkingS
     */
    public void isMouseOutBound(final float x, final float y) {
        final int cx = (int) this.camera.position.x;
        final int cy = (int) this.camera.position.y;

        // Up
        if (y >= cy + this.height / 2 - GameConstants.CAMERA_SLIDING_MARGIN) {
            this.camDirection = GameConstants.CAMERA_UP;
        } else if (y <= cy - (this.height / 2 - GameConstants.CAMERA_SLIDING_MARGIN)) {
            this.camDirection = GameConstants.CAMERA_DOWN;
        } else if (x <= cx - (this.width / 2 - GameConstants.CAMERA_SLIDING_MARGIN)) {
            this.camDirection = GameConstants.CAMERA_LEFT;
        } else if (x >= cx + this.width / 2 - GameConstants.CAMERA_SLIDING_MARGIN) {
            this.camDirection = GameConstants.CAMERA_RIGHT;
        } else {
            this.camDirection = GameConstants.CAMERA_HOLD;
        }

    }

    /**
     * Debugging
     */
    public void printAllAnimation() {
        for (final Animation anim : this.animQueue) {
            System.out.println(anim);
        }
    }

    public void printAllVirtualObjects() {
        final int mapWidth = this.mapProperties.get("width", Integer.class);
        final int mapHeight = this.mapProperties.get("height", Integer.class);
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                if (this.virtualMap.getCell(col, row)
                        .getObject() != null) {
                    System.out.printf("(%d , %d) is %s\n", col, row,
                            this.virtualMap.getCell(col, row)
                                    .getObject()
                                    .getName());
                }
            }
        }
    }

}
