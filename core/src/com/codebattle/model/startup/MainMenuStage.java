package com.codebattle.model.startup;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;

public class MainMenuStage extends Stage {
    final StartupScene parent;

    private Table table;

    private List<TextButton> buttons;
    private TextButton btn_exit;
    private TextButton btn_tutorial;
    private TextButton btn_multi;

    private ButtonHandler buttonHandler;

    public MainMenuStage(StartupScene parent) {
        super();
        this.parent = parent;
        this.table = new Table();
        this.buttonHandler = new ButtonHandler();

        this.table = new Table();
        this.table.setFillParent(true);
        this.addActor(table);
        this.buttons = new ArrayList<TextButton>();

        this.btn_tutorial = new TextButton("Tutorial", GameConstants.DEFAULT_SKIN);
        this.btn_tutorial.addListener(this.buttonHandler);
        this.buttons.add(btn_tutorial);

        this.btn_multi = new TextButton("MultiPlayer", GameConstants.DEFAULT_SKIN);
        this.btn_multi.addListener(this.buttonHandler);
        this.buttons.add(btn_multi);

        this.btn_exit = new TextButton("Exit", GameConstants.DEFAULT_SKIN);
        this.btn_exit.addListener(this.buttonHandler);
        this.buttons.add(btn_exit);

        for (TextButton b : buttons) {
            this.table.add(b)
                    .prefWidth(Gdx.graphics.getWidth() * 0.2f)
                    .row();
        }
        Gdx.input.setInputProcessor(this);
    }

    private class ButtonHandler extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == btn_exit) {
                Gdx.app.exit();
            } else if (event.getListenerActor() == btn_tutorial) {
                try {
                    parent.setStage(new TutorialListStage(parent));
                    MainMenuStage.this.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (event.getListenerActor() == btn_multi) {
                parent.setStage(new MultiPlayerLobby(parent));
                MainMenuStage.this.dispose();
            }
        }
    }
}
