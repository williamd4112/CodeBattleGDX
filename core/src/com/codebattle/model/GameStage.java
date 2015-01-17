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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.codebattle.model.animation.SummonAnimation;
import com.codebattle.model.event.GameStageEventManager;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Direction;
import com.codebattle.scene.GameScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ShaderLoader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/*
 * GameStage
 * 1. @maprenderer: used to render the game map
 * 2. @camera: to control the viewport of the stage
 * */

public class GameStage extends Stage {
    private Owner winner = null;
    private boolean isInit = false;
    private final boolean[] switches = new boolean[1000];

    final public GameScene parent;
    private GameState state;
    private int width, height;

    final private TiledMap map;
    final private TiledMapTileLayer topLayer;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;
    private final ShaderProgram shader;
    private final float zoom = 1.0f;

    // Layers
    final private Group gameObjects;
    final private Group guiLayer;
    final private Group popLayer;

    // Dialog queue
    private final GameDialogQueue dialogQueue;

    /* Debug shape render */
    final ShapeRenderer debugRender;

    // Animation queue, actors in the stage will add animation object to this
    // Stage will process it sequentially , not parallelly
    private final Queue<BaseAnimation> animQueue;
    private BaseAnimation currentAnimation = null;

    // Used to detect blocking, when actor use its move series function
    private final VirtualMap virtualMap;
    private final VirtualSystem[] virtualSystems;

    // Camera sliding direction
    private int camDirection = GameConstants.CAMERA_HOLD;

    // Lighting Variables
    private final World world;
    private final RayHandler rayHandler;
    private final float ambientLightIntensity = 0.15f;
    private final float generalLightIntensity = 0.5f;

    // Assistant GUI
    private final CursorAnimation cursor;
    private VirtualCell selectCell = null;

    // EventManager
    private final GameStageEventManager eventManager;

    /**
     * Create a game stage
     *
     * @param parent        Parent scene
     * @param mapName       Map name
     * @throws Exception
     */
    public GameStage(final GameScene parent, final String mapName) throws Exception {
        super();
        Arrays.fill(this.switches, false);
        this.parent = parent;

        // Create shader
        this.shader = ShaderLoader.loadShader("vignette");

        // Create map and camera
        this.map = MapFactory.loadMapFromFile(mapName);
        if (this.map.getLayers().get("inpassiable") != null) {
            this.map.getLayers().get("inpassiable").setVisible(false);
        }

        this.topLayer = (TiledMapTileLayer) this.map.getLayers().get("most up layer");
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.getSpriteBatch().setShader(this.shader);
        this.getViewport().setCamera(this.camera);

        // Create Lighting
        this.world = new World(new Vector2(0, -10), true);
        this.rayHandler = new RayHandler(this.world);
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
        this.virtualMap = new VirtualMap(this, this.map);
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

        Gdx.input.setInputProcessor(this);
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
        this.mapRenderer.getSpriteBatch().begin();
        this.gameObjects.draw(this.mapRenderer.getSpriteBatch(), 1.0f);
        this.mapRenderer.getSpriteBatch().end();

        // Draw top layer
        this.mapRenderer.getSpriteBatch().begin();
        if (this.topLayer != null) {
            this.mapRenderer.renderTileLayer(this.topLayer);
        }
        this.mapRenderer.getSpriteBatch().end();

        // Draw lighting
        this.rayHandler.setCombinedMatrix(this.camera.combined);
        this.rayHandler.updateAndRender();
        this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        // Draw and process animation
        this.processAnimation(this.mapRenderer.getSpriteBatch(), Gdx.graphics.getDeltaTime());

        // Draw Cursor
        this.mapRenderer.getSpriteBatch().begin();
        if (this.selectCell != null) {
            if (this.selectCell.getObject() != null) {
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
    public void act(final float delta) {
        super.act(delta);
        this.gameObjects.act(delta);
    }

    /**
     * Called by game scene recursively
     * @param width
     * @param height
     */
    public void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.camera.setToOrtho(false, width * this.zoom, height * this.zoom);
        this.camera.update();
        this.getViewport().setScreenBounds(0, 0, (int) (width * this.zoom),
                (int) (height * this.zoom));

        // Resize GUI
        this.resizeLayer(this.guiLayer);
        this.resizeLayer(this.popLayer);

        this.dialogQueue.resize(width, height);

        // Setting shader
        this.shader.begin();
        this.shader.setUniformf("resolution", width, height);
        this.shader.end();
    }

    public void resizeLayer(final Group layer) {
        for (final Actor ui : layer.getChildren()) {
            if (ui instanceof Resizeable) {
                ((Resizeable) ui).resize(this.width, this.height);
            }
        }
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

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer,
            final int button) {
        super.touchDown(screenX, screenY, pointer, button);
        final Vector2 worldCoord =
                this.screenToStageCoordinates(new Vector2(screenX, screenY));
        final int vx = (int) (worldCoord.x / 32);
        final int vy = (int) (worldCoord.y / 32);

        if (this.hit(worldCoord.x, worldCoord.y, true) != null) {
            return super.touchDown(screenX, screenY, pointer, button);
        }

        if (!this.isOutBoundInVirtualMap(vx, vy)) {
            final VirtualCell cell = this.virtualMap.getCell(vx, vy);
            this.emitSelectEvent(cell);

        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(final int keyCode) {
        this.dialogQueue.keyDown(keyCode);
        return super.keyDown(keyCode);
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
        final BaseAnimation anim = this.animQueue.peek();

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
            anim.update(delta);
            anim.draw(batch, this.camera, delta);
        }

        batch.end();
    }

    public void resetAnimQueue() {
        for (final BaseAnimation anim : this.animQueue) {
            if (anim instanceof SummonAnimation) {
                final SummonAnimation summ = (SummonAnimation) anim;
                summ.removeLight();
            }
        }
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
    public void emitAttackEvent(final GameObject attacker, final Attack attack, final int x,
            final int y) {
        try {
            final GameObject target = this.virtualMap.getCell(x, y).getObject();
            if (target != null) {
                // Different type of attack animation
                if (attacker instanceof GameActor) {
                    this.addAnimation(new GameActorAttackAnimation(this, attack,
                            (GameActor) attacker, target));
                } else {
                    this.addAnimation(new AttackAnimation(this, attack, target));
                }
                target.onAttacked(attack);

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void emitInteractEvent(final GameObject contacter, final int x, final int y) {
        final GameObject target = this.virtualMap.getCell(x, y).getObject();
        if (target != null) {
            target.onInteract(contacter);
        }
    }

    public void emitSkillEvent(final GameObject emitter, final Skill skill, final int x,
            final int y) {
        try {
            if (!this.isOutBoundInVirtualMap(x, y)) {
                this.getVirtualMap().getCell(x, y).onSkill(skill, emitter);
            }
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void emitSelectEvent(final VirtualCell cell) {
        this.selectCell = cell;
        if (this.selectCell.getObject() != null) {
            this.selectCell.getObject().onSelected(this.parent.getCurrentPlayer());
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

    public void emitDestroyedEvent(final GameObject obj) {
        obj.onDestroyed();
        this.eventManager.onGameObjectDestroyed(obj);
        this.gameObjects.removeActor(obj);
    }

    public void emitVirtualMapUpdateEvent() {
        this.eventManager.onVirtualMapUpdate(this.virtualMap);
    }

    public void emitRoundCompleteEvent() {
        this.eventManager.onRoundComplete(this);
        for (final Actor a : this.gameObjects.getChildren()) {
            if (a instanceof MoveableGameObject) {
                ((MoveableGameObject) a).setDirection(Direction.HOLD_DEF);
            }
        }
        this.parent.onRoundComplete();
    }

    public void emitStageStartEvent() {
        this.eventManager.onStageStart();
    }

    public void emitStageCompleteEvent(final Owner winner) {
        this.winner = winner;
        this.parent.onStageComplete(winner);
    }

    /**
     * Add elements of stage
     */
    public GameStage addGameObject(final GameObject obj) {
        this.gameObjects.addActor(obj);
        return this;
    }

    public GameStage addGUI(final Actor actor) {
        this.guiLayer.addActor(actor);
        return this;
    }

    public void addDialog(final GameDialog dlg) {
        this.popLayer.addActor(dlg);
    }

    public void addAnimation(final BaseAnimation animation) {
        this.animQueue.add(animation);
    }

    public PointLight addPointLight(final Color color, final float x, final float y,
            final int radius) {
        return new PointLight(this.rayHandler, 1000, new Color(color.r, color.g, color.b,
                this.generalLightIntensity + 0.3f), radius, x, y);
    }

    public PointLight addPointLight(final PointLightMeta light) {
        System.out.printf("add light at %d , %d\n", light.x, light.y);
        return this.addPointLight(light.color, light.x, light.y, light.radius);
    }

    public void putDialog(final GameDialog dialog) {
        this.dialogQueue.add(dialog);
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

    public SnapshotArray<GameActor> getGameActorsByOwner(final Owner owner) {
        final SnapshotArray<GameActor> objs = new SnapshotArray<GameActor>();
        for (final Actor actor : this.gameObjects.getChildren()) {
            if (actor instanceof GameActor) {
                final GameActor obj = (GameActor) actor;
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

    public VirtualSystem[] getVirtualSystems() {
        return this.virtualSystems;
    }

    public VirtualSystem getVirtualSystem(final Owner owner) {
        if (owner.index < this.virtualSystems.length) {
            return this.virtualSystems[owner.index];
        } else {
            return null;
        }
    }

    public GameStageEventManager getEventManager() {
        return this.eventManager;
    }

    public GameObject findGameObjectByNameAndOwner(final String name, final Owner owner) {
        for (final Actor actor : this.gameObjects.getChildren()) {
            if (actor instanceof GameObject) {
                final GameObject obj = (GameObject) actor;
                if (obj.getOwner() == owner && obj.getName().equals(name)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public boolean isExistGameObject(final String name, final Owner owner) {
        final GameObject obj = this.findGameObjectByNameAndOwner(name, owner);
        if (obj == null) {
            System.out.println("Target is null");
            return false;
        } else {
            System.out.println("Target: " + obj.getName() + "State: " + obj.getState());
            return obj.isAlive() ? true : false;
        }
    }

    public GameObject findGameObject(final int vx, final int vy) {
        if (vx < 0 || vx >= this.getMapWidth() || vy < 0 || vy >= this.getMapHeight()) {
            return null;
        }
        return this.virtualMap.getCell(vx, vy).getObject();
    }

    public GameObject getSelectedObject() {
        if (this.selectCell == null) {
            return null;
        }
        return this.selectCell.getObject();
    }

    public VirtualCell getSelectedCell() {
        if (this.selectCell == null) {
            return null;
        }
        return this.selectCell;
    }

    public Group getGUILayer() {
        return this.guiLayer;
    }

    public boolean getSwitchState(final int index) {
        if (index < this.switches.length) {
            return this.switches[index];
        }
        return false;
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

    public void setCameraTarget(final VirtualCell target) {
        this.camera.position.set(target.getX(), target.getY(), 0);
        this.camera.update();
    }

    public void setCameraTarget(final int x, final int y) {
        this.camera.position.set(x, y, 0);
        this.camera.update();
    }

    public void setGUIVisiable(final boolean flag) {
        this.guiLayer.setVisible(flag);
    }

    public void setGUILayerPosition(final float x, final float y) {
        this.guiLayer.setBounds(x - this.width / 2, y - this.height / 2, this.width,
                this.height);
        this.popLayer.setBounds(x - this.width / 2, y - this.height / 2, this.width,
                this.height);
    }

    public void setSwitch(final int index, final boolean val) {
        if (index >= this.switches.length) {
            return;
        }
        this.switches[index] = val;
    }

    public void setAmbientLight(final Color color, final float intensity) {
        this.rayHandler.setAmbientLight(color.r * 1.0f / 255, color.g * 1.0f / 255,
                color.b * 1.0f / 255, intensity);
    }

    public void resetGUILayerPosition() {
        this.setGUILayerPosition(this.camera.position.x, this.camera.position.y);
    }

    public void removeDialog(final GameDialog dlg) {
        this.guiLayer.removeActor(dlg);
    }

    public void fadeOutLayer(final Group layer) {
        layer.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.hide()));
    }

    public void fadeInLayer(final Group layer) {
        layer.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(0.5f)));
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

    public boolean isOutBoundInVirtualMap(final int x, final int y) {
        return x < 0 || x >= this.getMapWidth() || y < 0 || y >= this.getMapHeight();
    }

    /**
     * Debugging
     */
    public void printAllAnimation() {
        for (final BaseAnimation anim : this.animQueue) {
            System.out.println(anim);
        }
    }

    public void printAllVirtualObjects() {
        final int mapWidth = this.mapProperties.get("width", Integer.class);
        final int mapHeight = this.mapProperties.get("height", Integer.class);
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                if (this.virtualMap.getCell(col, row).getObject() != null) {
                    System.out.printf("(%d , %d) is %s\n", col, row,
                            this.virtualMap.getCell(col, row).getObject().getName());
                }
            }
        }
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

        for (int i = 0; i < this.getMapWidth() * GameConstants.CELL_SIZE; i +=
                GameConstants.CELL_SIZE) {
            this.debugRender.line(i, 0, i, this.getMapHeight() * GameConstants.CELL_SIZE);
        }
        for (int i = 0; i < this.getMapHeight() * GameConstants.CELL_SIZE; i +=
                GameConstants.CELL_SIZE) {
            this.debugRender.line(0, i, this.getMapWidth() * GameConstants.CELL_SIZE, i);
        }

        this.debugRender.set(ShapeType.Filled);

        final int mapWidth = this.mapProperties.get("width", Integer.class);
        final int mapHeight = this.mapProperties.get("height", Integer.class);
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                final VirtualCell cell = this.virtualMap.getCell(col, row);

                // Draw select object and its range
                if (this.selectCell != null) {
                    final GameObject obj = this.selectCell.getObject();
                    if (obj != null) {
                        if (obj instanceof GameActor) {
                            final GameActor actor = (GameActor) obj;
                            if (actor.isInRange(actor.getProp().maxsteps, cell.getVX(),
                                    cell.getVY())) {
                                this.renderDebugCell(actor.getOwner(), col, row);
                            }
                        }
                    }
                }

                // Draw object
                if (!this.virtualMap.isPassiable(col, row)) {
                    if (this.virtualMap.getCell(col, row).getObject() != null) {
                        this.renderDebugCell(this.virtualMap.getCell(col, row)
                                .getObject()
                                .getOwner(), col, row);
                    }
                }

                // Draw select cell
                if (cell == this.selectCell) {
                    if (cell.getObject() == null) {
                        this.renderDebugCell(this.parent.getCurrentPlayer(), col, row);
                    }
                }
            }
        }

        this.debugRender.end();
    }

    private void renderDebugCell(final Owner owner, final int col, final int row) {
        switch (owner) {
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
        final float x = col * GameConstants.CELL_SIZE;
        final float y = row * GameConstants.CELL_SIZE;
        this.debugRender.rect(x, y, GameConstants.CELL_SIZE, GameConstants.CELL_SIZE);
    }
}
