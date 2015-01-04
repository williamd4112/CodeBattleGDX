package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codebattle.game.CodeBattle;
import com.codebattle.model.startup.MainMenuStage;
import com.codebattle.utility.SoundUtil;

public class StartupScene implements Screen {

    final private CodeBattle parent;

    private Stage stage;

    public StartupScene(CodeBattle parent) {
        super();
        this.parent = parent;
        this.stage = new MainMenuStage(this);
        SoundUtil.playBGM("main_theme.mp3");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport()
                .update(width, height);
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
        this.stage.dispose();

    }

    public void backtoMain() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CodeBattle getParent() {
        return this.parent;
    }
}
