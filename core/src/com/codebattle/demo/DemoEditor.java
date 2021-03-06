package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.gui.editor.JSCodeEditorCore;
import com.codebattle.utility.GameConstants;

public class DemoEditor implements Screen {

    // private CodeEditor editor;
    private final JSCodeEditorCore editor;
    private final Stage stage;
    private final Table root;
    private ScrollPane pane;

    public DemoEditor() {
        super();
        this.stage = new Stage();
        this.root = new Table();

        this.editor = new JSCodeEditorCore(GameConstants.DEFAULT_SKIN);
        this.root.add(this.pane = new ScrollPane(this.editor, GameConstants.DEFAULT_SKIN))
                .expand()
                .fill();

        this.stage.addActor(this.root);
        this.root.setFillParent(true);

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub

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
