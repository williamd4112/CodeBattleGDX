package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.ShaderLoader;
import com.codebattle.utility.TextureFactory;

public class DemoLightScene implements Screen {

    final private TiledMap map;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;

    ShaderProgram shader;
    ShaderProgram defaultShader;

    SpriteBatch batch;

    FrameBuffer fbo;
    Texture light;

    public DemoLightScene() {
        this.batch = new SpriteBatch();
        this.defaultShader = ShaderLoader.loadShader("default");
        this.shader = new ShaderProgram(ShaderLoader.loadVertexShader("ambient"),
                ShaderLoader.loadFragShader("ambient"));
        this.shader.setUniformi("u_lightmap", 1);

        try {
            this.light = TextureFactory.getInstance()
                    .loadTextureFromFile("light", ResourceType.IMAGE);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.map = MapFactory.loadMapFromFile("demo2");
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, this.batch);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.getSpriteBatch()
                .setShader(this.shader);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Draw FBO
        this.fbo.begin();
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.setShader(this.defaultShader);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        float lightSize = 128f;
        this.batch.draw(this.light, 320, 240, lightSize, lightSize);
        lightSize = 96;
        this.batch.draw(this.light, 280, 240, lightSize, lightSize);
        this.batch.end();
        this.fbo.end();

        // draw the actual scene
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.setShader(this.shader);
        this.batch.begin();
        this.fbo.getColorBufferTexture()
                .bind(1);
        this.light.bind(0);
        this.batch.end();

        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        // this.mapRenderer.setView(this.camera);
        // this.mapRenderer.render();
    }

    @Override
    public void resize(final int width, final int height) {
        this.camera.position.set(width / 2, height / 2, 0);
        this.camera.setToOrtho(false, width, height);
        this.camera.update();

        // Setting shader
        this.fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
        this.shader.begin();
        this.shader.setUniformf("resolution", width, height);
        this.shader.setUniformi("u_lightmap", 1);
        // this.shader.setUniformf("LightPos", width/2, height/2, 1);
        this.shader.end();

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
