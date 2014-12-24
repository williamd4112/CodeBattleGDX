package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codebattle.utility.MapFactory;
import com.codebattle.utility.ShaderLoader;

public class DemoLightStage extends Stage {
    final private TiledMap map;
    final private MapProperties mapProperties;

    final private OrthogonalTiledMapRenderer mapRenderer;
    final private OrthographicCamera camera;
    private final ShaderProgram shader;

    public DemoLightStage() {
        this.shader = new ShaderProgram(ShaderLoader.loadVertexShader("ambient"),
                ShaderLoader.loadFragShader("ambient"));

        this.map = MapFactory.loadMapFromFile("demo2");
        this.mapProperties = this.map.getProperties();
        this.camera = new OrthographicCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);
        this.mapRenderer.setView(this.camera);
        // this.mapRenderer.getSpriteBatch().setShader(shader);
        this.getViewport()
                .setCamera(this.camera);
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        super.draw();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();
    }

    @Override
    public void act(final float delta) {
        // TODO Auto-generated method stub
        super.act(delta);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        super.dispose();
        this.mapRenderer.dispose();
    }

}
