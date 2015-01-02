package com.codebattle.scene;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.utility.GameConstants;

public class StartupScene extends ClickListener implements Screen {

    final private OrthographicCamera camera;
    final private Stage stage;
    final private Table table;

    private List<TextButton> buttons;
    private TextButton btn_exit;
    private TextButton btn_tutorial;

    public StartupScene() {
        super();
        this.camera = new OrthographicCamera();
        this.stage = new Stage();
        this.stage.getViewport()
                .setCamera(camera);
        this.table = new Table();
        this.table.setFillParent(true);
        this.stage.addActor(table);
        this.buttons = new ArrayList<TextButton>();

        this.btn_tutorial = new TextButton("Tutorial", GameConstants.DEFAULT_SKIN);
        this.btn_tutorial.addListener(this);
        this.buttons.add(this.btn_tutorial);

        this.btn_exit = new TextButton("Exit", GameConstants.DEFAULT_SKIN);
        this.btn_exit.addListener(this);
        this.buttons.add(btn_exit);

        for (TextButton b : buttons) {
            this.table.add(b)
                    .prefWidth(Gdx.graphics.getWidth() * 0.2f)
                    .row();
        }

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if (event.getListenerActor() == this.btn_exit) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.camera.setToOrtho(false, width, height);
        this.camera.update();
        this.stage.getViewport()
                .setScreenBounds(0, 0, (width), (height));
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

}
