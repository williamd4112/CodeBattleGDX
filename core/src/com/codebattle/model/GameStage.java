package com.codebattle.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.codebattle.gui.GameDialog;
import com.codebattle.gui.Resizeable;
import com.codebattle.model.animation.AttackAnimation;
import com.codebattle.model.animation.BaseAnimation;
import com.codebattle.model.animation.CursorAnimation;
import com.codebattle.model.animation.GameActorAttackAnimation;
import com.codebattle.model.animation.OnAttackAnimation;
import com.codebattle.model.event.GameStageEventManager;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Skill;
import com.codebattle.scene.GameScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ShaderLoader;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage
 * */

public class GameStage extends Stage {
    private Owner winner = null;
    private boolean isInit = false;
    private boolean[] switches = new boolean[1000];

    final GameScene parent;
    private GameState state;
    private int width, height;

    final private TiledMap map;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;
    private ShaderProgram shader;
    private float zoom = 1.0f;

    // Layers
    final private Group gameObjects;
    final private Group guiLayer;
    final private Group popLayer;

    // Dialog queue
    private GameDialogQueue dialogQueue;

    /* Debug shape render */
    final ShapeRenderer debugRender;

    // Animation queue, actors in the stage will add animation object to this
    // Stage will process it sequentially , not parallelly
    private Queue<BaseAnimation> animQueue;
    private BaseAnimation currentAnimation = null;

    // Used to detect blocking, when actor use its move series function
    private VirtualMap virtualMap;
    private VirtualSystem[] virtualSystems;

    // Camera sliding direction
    private int camDirection = GameConstants.CAMERA_HOLD;

    // Lighting Variables
    private World world;
    private RayHandler rayHandler;
    private float ambientLightIntensity = 0.15f;
    private float generalLightIntensity = 0.5f;

    // Assistant GUI
    private CursorAnimation cursor;
    private VirtualCell selectCell = null;

    // EventManager
    private GameStageEventManager eventManager;

    /*
     * GameStage Constructor
     *
     * @param - mapName : used to create map and map renderer , camera
     */
    public GameStage(GameScene parent, String mapName) throws Exception {
        super();
        Arrays.fill(this.switches, false);
        this.parent = parent;

        // Create shader
        this.shader = ShaderLoader.loadShader("vignette");

        // Create map and camera
        this.map = MapFactory.loadMapFromFile(mapName);
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.getSpriteBatch()
                .setShader(shader);
        this.getViewport()
                .setCamera(camera);

        // Create Lighting
        this.world = new World(new Vector2(0, -10), true);
        this.rayHandler = new RayHandler(world);
        this.rayHandler.setAmbientLight(0f, 0f, 0f, this.ambientLightIntensity);
        this.rayHandler.setBlur(true);

        // Create all data structure
        this.gameObjects = new Group();
        this.gameObjects.setTouchable(Touchable.disabled);
        this.guiLayer = new Group();
        this.guiLayer.setTouchable(Touchable.childrenOnly);
        this.popLayer = new Group();
        this.popLayer.setTouchable(Touchable.disabled);
        this.animQueue = new LinkedList<BaseAnimation>();
        this.dialogQueue = new GameDialogQueue(this);
        this.eventManager = new GameStageEventManager(this);

        // Virtual world
        this.virtualMap = new VirtualMap(this, map);
        this.virtualSystems = new VirtualSystem[] { new VirtualSystem(this, Owner.RED),
                new VirtualSystem(this, Owner.BLUE) };

        // Add GUI layer
        this.addActor(this.guiLayer);
        this.addActor(this.popLayer);

        // Create debug renderer
        this.debugRender = new ShapeRenderer();

        // Create Select Cursor
        this.cursor = new CursorAnimation();

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
        // First renderering
        if (!this.isInit) {
            this.emitStageStartEvent();
            this.isInit = true;
        }

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

        // Draw lighting
        this.rayHandler.setCombinedMatrix(camera.combined);
        this.rayHandler.updateAndRender();
        this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        // Draw and process animation
        this.processAnimation(this.mapRenderer.getSpriteBatch(), Gdx.graphics.getDeltaTime());

        // Draw Cursor
        this.mapRenderer.getSpriteBatch()
                .begin();
        if (this.selectCell != null) {
            if (this.selectCell.getObject() != null) {
                this.cursor.setPosition(this.selectCell.getObject()
                        .getCursorX(), this.selectCell.getObject()
                        .getCursorY());
                this.cursor.setVisiable(true);
                this.cursor.update();
                this.cursor.draw(this.mapRenderer.getSpriteBatch());
            }
        }
        this.mapRenderer.getSpriteBatch()
                .end();
        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.gameObjects.act(delta);
    }

    /**
     * Called by game scene recursively
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.camera.setToOrtho(false, width * zoom, height * zoom);
        this.camera.update();
        this.getViewport()
                .setScreenBounds(0, 0, (int) (width * zoom), (int) (height * zoom));

        // Resize GUI
        this.resizeLayer(guiLayer);
        this.resizeLayer(popLayer);

        this.dialogQueue.resize(width, height);

        // Setting shader
        this.shader.begin();
        this.shader.setUniformf("resolution", width, height);
        this.shader.end();
    }

    public void resizeLayer(Group layer) {
        for (Actor ui : layer.getChildren()) {
            if (ui instanceof Resizeable) {
                ((Resizeable) ui).resize(width, height);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.mapRenderer.dispose();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
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
        super.touchDown(screenX, screenY, pointer, button);
        Vector2 worldCoord = this.screenToStageCoordinates(new Vector2(screenX, screenY));
        int vx = (int) (worldCoord.x / 32);
        int vy = (int) (worldCoord.y / 32);

        if (!isOutBoundInVirtualMap(vx, vy)) {
            VirtualCell cell = this.virtualMap.getCell(vx, vy);
            if (cell.getObject() != null)
                this.emitSelectEvent(cell);

        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {
        this.dialogQueue.keyDown(keyCode);
        return super.keyDown(keyCode);
    }

    /**
     * When in State Anim , call processAnimation
     * execute the animation's action every frame until reaching its ending condition
     * when completed, call this animation's finished method once to finish the final stage
     * poll out this animation from queue
     */
    public void processAnimation(Batch batch, float delta) {
        // Only work at ANIM state
        if (this.state != GameState.ANIM)
            return;

        // No animation in the queue
        if (this.animQueue.isEmpty()) {
            this.setState(GameState.PAUSE);
            this.guiLayer.addAction(Actions.sequence(Actions.fadeIn(0.3f)));
            this.resetGUILayerPosition();
            this.currentAnimation = null;

            // Reset the virtual map (Update new state to enter the next state
            this.virtualMap.resetVirtualMap();
            this.emitGUIChangeEvent();
            this.emitRoundCompleteEvent();
            return;
        }

        // Start batch
        batch.begin();

        // Hide the GUI
        this.guiLayer.addAction(Actions.sequence(Actions.fadeOut(0.3f)));

        // Process the top animation
        BaseAnimation anim = this.animQueue.peek();

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
            anim.update(0);
            anim.draw(batch, this.camera, delta);
        }

        batch.end();
    }

    public void resetAnimQueue() {
        this.animQueue.clear();
    }

    public void reset() {
        this.resetAnimQueue();
        this.virtualMap.resetActorsVirtualCoordinate();
        this.virtualMap.resetVirtualMap();
    }

    /**
     * Event handling
     */
    public void emitAttackEvent(GameObject attacker, Attack attack, int x, int y) {
        try {
            GameObject target = this.virtualMap.getCell(x, y)
                    .getObject();
            if (target != null) {
                GameObjectState state = target.onAttacked(attack);
                // Different type of attack animation
                if (attack.animMeta.type.equals("GameActorAttackAnimation"))
                    this.addAnimation(new GameActorAttackAnimation(this, attack,
                            (GameActor) attacker, target));
                else
                    this.addAnimation(new AttackAnimation(this, attack, target));

                this.addAnimation(new OnAttackAnimation(target));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emitInteractEvent(GameObject contacter, int x, int y) {
        GameObject target = this.virtualMap.getCell(x, y)
                .getObject();
        if (target != null)
            target.onInteract(contacter);
    }

    public void emitSkillEvent(GameObject emitter, Skill skill, int x, int y) {
        try {
            skill.execute(this, emitter, x, y);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void emitSelectEvent(VirtualCell cell) {
        this.selectCell = cell;
        if (this.selectCell.getObject() != null) {
            this.selectCell.getObject()
                    .onSelected(this.parent.getCurrentPlayer());
            this.setCameraTarget(this.selectCell);
        }

        this.emitGUIChangeEvent();
    }

    public void emitUnselectEvent() {
        this.selectCell = null;
        this.cursor.setVisiable(false);
        this.emitGUIChangeEvent();
    }

    public void emitGUIChangeEvent() {
        this.parent.onGUIChange();
    }

    public void emitDestroyedEvent(GameObject obj) {
        obj.onDestroyed();
        this.eventManager.onGameObjectDestroyed(obj);
        this.gameObjects.removeActor(obj);
    }

    public void emitVirtualMapUpdateEvent() {
        this.eventManager.onVirtualMapUpdate(this.virtualMap);
    }

    public void emitRoundCompleteEvent() {
        this.eventManager.onRoundComplete(this);
    }

    public void emitStageStartEvent() {
        this.eventManager.onStageStart();
    }

    public void emitStageCompleteEvent(Owner winner) {
        this.winner = winner;
    }

    /**
     * Add elements of stage
     */
    public GameStage addGameObject(GameObject obj) {
        this.gameObjects.addActor(obj);
        return this;
    }

    public GameStage addGUI(Actor actor) {
        this.guiLayer.addActor(actor);
        return this;
    }

    public void addDialog(GameDialog dlg) {
        this.popLayer.addActor(dlg);
    }

    public void addAnimation(BaseAnimation animation) {
        this.animQueue.add(animation);
    }

    public PointLight addPointLight(Color color, float x, float y, int radius) {
        return new PointLight(rayHandler, 1000, new Color(color.r, color.g, color.b,
                this.generalLightIntensity + 0.3f), radius, x, y);
    }

    public PointLight addPointLight(PointLightMeta light) {
        System.out.printf("add light at %d , %d\n", light.x, light.y);
        return this.addPointLight(light.color, light.x, light.y, light.radius);
    }

    public void putDialog(GameDialog dialog) {
        this.dialogQueue.add(dialog);
    }

    /**
     * Getters
     */
    @SuppressWarnings("unchecked")
    public <T> SnapshotArray<T> getGroupByType(Class<T> type) {
        SnapshotArray<T> objs = new SnapshotArray<T>();
        for (Actor actor : this.gameObjects.getChildren()) {
            if (type.isInstance(actor))
                objs.add((T) actor);
        }

        return objs;
    }

    public SnapshotArray<GameObject> getGameObjectsByOwner(Owner owner) {
        SnapshotArray<GameObject> objs = new SnapshotArray<GameObject>();
        for (Actor actor : this.gameObjects.getChildren()) {
            if (actor instanceof GameObject) {
                GameObject obj = (GameObject) actor;
                if (obj.getOwner() == owner)
                    objs.add(obj);
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

    public VirtualSystem[] getVirtualSystems() {
        return this.virtualSystems;
    }

    public GameStageEventManager getEventManager() {
        return this.eventManager;
    }

    public GameObject findGameObjectByNameAndOwner(String name, Owner owner) {
        for (Actor actor : this.gameObjects.getChildren()) {
            if (actor instanceof GameObject) {
                GameObject obj = (GameObject) actor;
                if (obj.getOwner() == owner && obj.getName()
                        .equals(name))
                    return obj;
            }
        }
        return null;
    }

    public boolean isExistGameObject(String name, Owner owner) {
        GameObject obj = this.findGameObjectByNameAndOwner(name, owner);
        if (obj == null) {
            System.out.println("Target is null");
            return false;
        } else {
            System.out.println("Target: " + obj.getName() + "State: " + obj.getState());
            return (obj.isAlive()) ? true : false;
        }
    }

    public GameObject findGameObject(int vx, int vy) {
        if (vx < 0 || vx >= this.getMapWidth() || vy < 0 || vy >= this.getMapHeight())
            return null;
        return this.virtualMap.getCell(vx, vy)
                .getObject();
    }

    public GameObject getSelectedObject() {
        if (this.selectCell == null)
            return null;
        return this.selectCell.getObject();
    }

    public Group getGUILayer() {
        return this.guiLayer;
    }

    public boolean getSwitchState(int index) {
        if (index < this.switches.length)
            return this.switches[index];
        return false;
    }

    /**
     * Setters
     */
    public void setState(GameState state) {
        this.state = state;
    }

    public void setCameraTarget(GameObject target) {
        this.camera.position.set(target.getX(), target.getY(), 0);
        this.camera.update();
    }

    public void setCameraTarget(VirtualCell target) {
        this.camera.position.set(target.getX(), target.getY(), 0);
        this.camera.update();
    }

    public void setCameraTarget(int x, int y) {
        this.camera.position.set(x, y, 0);
        this.camera.update();
    }

    public void setGUIVisiable(boolean flag) {
        this.guiLayer.setVisible(flag);
    }

    public void setGUILayerPosition(float x, float y) {
        this.guiLayer.setBounds(x - (width / 2), y - (height / 2), width, height);
        this.popLayer.setBounds(x - (width / 2), y - (height / 2), width, height);
    }

    public void setSwitch(int index, boolean val) {
        if (index >= this.switches.length)
            return;
        this.switches[index] = val;
    }

    public void resetGUILayerPosition() {
        this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
    }

    public void removeDialog(GameDialog dlg) {
        this.guiLayer.removeActor(dlg);
    }

    public void fadeOutLayer(Group layer) {
        layer.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.hide()));
    }

    public void fadeInLayer(Group layer) {
        layer.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(0.5f)));
    }

    public void moveCamera() {
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

    /**
     * Condition checkingS
     */
    public void isMouseOutBound(float x, float y) {
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

    public boolean isOutBoundInVirtualMap(int x, int y) {
        return (x < 0 || x >= this.getMapWidth() || y < 0 || y >= this.getMapHeight());
    }

    /**
     * Debugging
     */
    public void printAllAnimation() {
        for (BaseAnimation anim : this.animQueue) {
            System.out.println(anim);
        }
    }

    public void printAllVirtualObjects() {
        int mapWidth = this.mapProperties.get("width", Integer.class);
        int mapHeight = this.mapProperties.get("height", Integer.class);
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

    /**
     * Render debugging shape
     * @param delta
     */
    public void renderDebug() {
        this.debugRender.setProjectionMatrix(camera.combined);
        this.debugRender.begin(ShapeType.Line);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.debugRender.setAutoShapeType(true);
        this.debugRender.setColor(1, 0, 0, 0.2f);

        for (int i = 0; i < this.getMapWidth() * GameConstants.CELL_SIZE; i += GameConstants.CELL_SIZE)
            this.debugRender.line(i, 0, i, this.getMapHeight() * GameConstants.CELL_SIZE);
        for (int i = 0; i < this.getMapHeight() * GameConstants.CELL_SIZE; i += GameConstants.CELL_SIZE)
            this.debugRender.line(0, i, this.getMapWidth() * GameConstants.CELL_SIZE, i);

        this.debugRender.set(ShapeType.Filled);

        int mapWidth = this.mapProperties.get("width", Integer.class);
        int mapHeight = this.mapProperties.get("height", Integer.class);
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                if (!this.virtualMap.isPassiable(col, row)) {
                    this.debugRender.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b,
                            0.2f);
                    if (this.virtualMap.getCell(col, row)
                            .getObject() != null) {
                        GameObject obj = this.virtualMap.getCell(col, row)
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
                    float x = col * GameConstants.CELL_SIZE;
                    float y = row * GameConstants.CELL_SIZE;
                    this.debugRender.rect(x, y, GameConstants.CELL_SIZE, GameConstants.CELL_SIZE);
                }
            }
        }

        this.debugRender.end();
    }
}
