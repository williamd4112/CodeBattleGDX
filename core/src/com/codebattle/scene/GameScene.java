package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.gui.ScriptEditor;
import com.codebattle.model.GameActor;
import com.codebattle.model.GameStage;
import com.codebattle.model.ScriptProcessor;

/**
 * Game scene.
 *
 * 1. A game stage to manage all game objects and map in the scene. And a tiled map.
 * 2. A Script Processor process all object operation in blocked mode.
 * 3. A background thread to update map object in sync.
 */
public class GameScene implements Screen {
    private final GameStage stage;
    private final ScriptEditor scriptEditor;

    public GameScene(final String mapName) {
        this.stage = new GameStage(mapName);
        this.scriptEditor = new ScriptEditor(new Handler());
        this.stage.addGui(this.scriptEditor);

        // TODO After test completed, remove this
        GameActor a;
        GameActor b;
        a = new GameActor("Knight", 96, 96);
        b = new GameActor("Lancer", 128, 128);

        this.stage.addGameActor(a)
                .addGameActor(b);

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(final float delta) {
        // TODO Auto-generated method stub
        this.stage.render(delta);
    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub
        this.stage.resize(width, height);
        this.scriptEditor.resize(width, height);
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
    }

    /**
     * Handles script interpretation.
     */
    private class Handler extends ClickListener {
        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            // TODO Auto-generated method stub
            super.clicked(event, x, y);
            final String script = GameScene.this.scriptEditor.getScript();
            new ScriptProcessor(GameScene.this.stage, script).start();
        }
    }
}
