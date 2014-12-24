package com.codebattle.demo;

import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.codebattle.utility.MapFactory;

public class DemoBox2DLightScene implements Screen {

    final private TiledMap map;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;

    World world;
    RayHandler rayHandler;
    Box2DDebugRenderer renderer;

    SpriteBatch batch;

    public DemoBox2DLightScene() {
        this.batch = new SpriteBatch();
        this.map = MapFactory.loadMapFromFile("demo2");
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, this.batch);
        this.mapRenderer.setView(this.camera);

        this.renderer = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0, -10), true);
        this.rayHandler = new RayHandler(this.world);

        new DirectionalLight(this.rayHandler, 500, new Color(0.3f, 0.3f, 0.7f, 0.4f), 45);
        new PointLight(this.rayHandler, 500, Color.OLIVE, 400, Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();
        this.renderer.render(this.world, this.camera.combined);
        this.rayHandler.setCombinedMatrix(this.camera.combined);
        this.rayHandler.updateAndRender();
        this.world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }

    @Override
    public void resize(final int width, final int height) {
        this.camera.position.set(width / 2, height / 2, 0);
        this.camera.setToOrtho(false, width, height);
        this.camera.update();
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

    }

}
