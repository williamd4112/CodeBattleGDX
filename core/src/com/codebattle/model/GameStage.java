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
import com.codebattle.utility.GameUnits;
import com.codebattle.utility.MapFactory;

public class GameStage extends Stage {
    private int width;
    private int height;

    // Used to render the game map.
    private final OrthogonalTiledMapRenderer mapRenderer;
    // To control the viewport of the stage.
    private final OrthographicCamera camera;

    private final Group gameActors;
    private final Group guiLayer;

    // Debug shape render.
    private final ShapeRenderer debugRender;

    /**
     * Create a game stage.
     *
     * @param mapName   Used to create map and map renderer , camera
     */
    public GameStage(final String mapName) {
        final TiledMap map = MapFactory.loadMapFromFile(mapName);
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, this.getBatch());
        this.mapRenderer.setView(this.camera);
        this.getViewport()
                .setCamera(this.camera);

        this.gameActors = new Group();
        this.guiLayer = new Group();

        this.addActor(this.gameActors);
        this.addActor(this.guiLayer);

        this.debugRender = new ShapeRenderer();
    }

    /**
     * Add an actor to game actors.
     *
     * @param actor     New actor
     * @return this
     */
    public GameStage addGameActor(final GameActor actor) {
        this.gameActors.addActor(actor);
        return this;
    }

    /**
     * Add an actor to Gui layer.
     *
     * @param actor     New actor
     * @return this
     */
    public GameStage addGui(final Actor actor) {
        this.guiLayer.addActor(actor);
        return this;
    }

    public Group getGameActors() {
        return this.gameActors;
    }

    /**
     * Render game scene.
     *
     * @param delta     Render interval
     */
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();
        this.renderDebug(delta);

        this.act(delta);
        this.draw();
    }

    /**
     * Render game scene in debug mode.
     *
     * @param delta     Render interval
     */
    public void renderDebug(final float delta) {
        this.debugRender.setProjectionMatrix(this.camera.combined);
        this.debugRender.begin(ShapeType.Line);
        this.debugRender.setColor(1, 0, 0, 1);
        for (int i = 0; i < this.width; i += GameUnits.CELL_SIZE) {
            this.debugRender.line(i, 0, i, this.height);
        }
        this.debugRender.setColor(1, 0, 0, 1);
        for (int i = 0; i < this.height; i += GameUnits.CELL_SIZE) {
            this.debugRender.line(0, i, this.width, i);
        }
        this.debugRender.end();
    }

    /**
     * Resize the game scene.
     *
     * @param width     New screen width
     * @param height    New screen height
     */
    public void resize(final int width, final int height) {
        this.camera.position.set(width / 2, height / 2, 0);
        this.width = width;
        this.height = height;
        this.camera.setToOrtho(false, width, height);
        this.camera.update();
        this.getViewport()
                .setScreenBounds(0, 0, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.mapRenderer.dispose();
    }
}
