package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class DemoUi implements Screen {
    private final Stage stage;
    private Table table;
    private ShapeRenderer shapeRenderer;
    private Skin skin;

    private TextArea txtArea;
    private ScrollPane pane;
    private DemoObject obj;

    public DemoUi() {
        this.skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
        this.txtArea = new TextArea("String", this.skin);

        this.stage = new Stage();
        // TODO This variable is unused.
        this.shapeRenderer = new ShapeRenderer();

        final ScrollPane pane2 = new ScrollPane(new Image(new Texture("badlogic.jpg")), this.skin);
        pane2.setScrollingDisabled(false, true);
        // pane2.setCancelTouchFocus(false);
        pane2.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y,
                    final int pointer, final int button) {
                event.stop();
                return true;
            }
        });

        this.table = new Table();
        this.table.debug();
        // table.add(txtArea).expand().fill();
        // table.add(new Image(new Texture("badlogic.jpg")));
        // table.row();
        // table.add(new Image(new Texture("badlogic.jpg")));
        // table.row();
        // table.add(pane2).size(100);
        // table.row();
        // table.add(new Image(new Texture("badlogic.jpg")));
        // table.row();
        // table.add(new Image(new Texture("badlogic.jpg")));

        final ScrollPane pane = new ScrollPane(this.txtArea, this.skin);
        pane.setScrollingDisabled(true, false);
        // pane.setCancelTouchFocus(false);
        if (false) {
            // This sizes the pane to the size of it's contents.
            pane.pack();
            // Then the height is hardcoded, leaving the pane the width of it's contents.
            pane.setHeight(Gdx.graphics.getHeight());
        } else {
            // This shows a hardcoded size.
            pane.setWidth(300);
            pane.setHeight(Gdx.graphics.getHeight());
        }

        this.stage.addActor(pane);

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(final float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub
        this.stage.getViewport()
                .update(width, height, true);

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
        this.shapeRenderer.dispose();
    }
}
