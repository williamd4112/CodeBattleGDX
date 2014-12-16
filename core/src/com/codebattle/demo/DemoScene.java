package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class DemoScene implements Screen {
    private final Stage stage;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final DemoScriptProcessor scriptProcessor;

    private final DemoObject obj;
    private final DemoUiGroup ui;

    public DemoScene(final String mapName) {
        final TiledMap map = this.loadMapFromFile(mapName);

        this.stage = new Stage();
        this.scriptProcessor = new DemoScriptProcessor(this.stage);
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, this.stage.getBatch());
        this.camera = new OrthographicCamera();
        this.ui = new DemoUiGroup();
        this.obj = new DemoObject("001-Fighter01.png");
        this.stage.getViewport()
                .setCamera(this.camera);
        this.ui.setTouchable(Touchable.disabled);
        this.stage.addActor(this.obj);
        this.stage.addActor(this.ui);
        this.obj.setX(320);
        this.obj.setY(240);
        this.obj.setTouchable(Touchable.enabled);
        final Vector2 coordinate = this.stage.screenToStageCoordinates(new Vector2(
                this.obj.getCenterX(), this.obj.getCenterY()));
        final Actor hit = this.stage.hit(coordinate.x, coordinate.y, true);
        System.out.println(hit);
        Gdx.input.setInputProcessor(this.stage);
    }

    public TiledMap loadMapFromFile(final String mapName) {
        final TiledMap map = new TmxMapLoader().load("level/" + mapName + ".tmx");

        return map == null ? null : map;
    }

    @Override
    public void render(final float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub
        this.camera.position.set(width / 2, height / 2, 0);
        this.camera.setToOrtho(false, width, height);
        this.camera.update();
        this.stage.getViewport()
                .setScreenBounds(0, 0, width, height);
        this.ui.resize(width, height);
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
        this.stage.dispose();
        this.mapRenderer.dispose();
    }
}
