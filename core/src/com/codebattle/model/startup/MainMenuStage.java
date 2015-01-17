package com.codebattle.model.startup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

import java.util.ArrayList;
import java.util.List;

public class MainMenuStage extends Stage {
    final StartupScene parent;

    private Table table;

    private final List<TextButton> buttons;
    private final TextButton btn_exit;
    private final TextButton btn_tutorial;
    private final TextButton btn_multi;

    private final ButtonHandler buttonHandler;

    private Drawable background;

    public MainMenuStage(final StartupScene parent) {
        super();
        this.parent = parent;
        this.table = new Table();
        this.buttonHandler = new ButtonHandler();
        try {
            this.background = TextureFactory.getInstance().loadDrawable(
                    "StartupSceneBackground.png", ResourceType.PICTURE);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.table = new Table();
        this.table.setFillParent(true);
        this.addActor(this.table);
        this.buttons = new ArrayList<TextButton>();

        this.btn_tutorial = new TextButton("Tutorial", GameConstants.DEFAULT_SKIN);
        this.btn_tutorial.addListener(this.buttonHandler);
        this.buttons.add(this.btn_tutorial);

        this.btn_multi = new TextButton("MultiPlayer", GameConstants.DEFAULT_SKIN);
        this.btn_multi.addListener(this.buttonHandler);
        this.buttons.add(this.btn_multi);

        this.btn_exit = new TextButton("Exit", GameConstants.DEFAULT_SKIN);
        this.btn_exit.addListener(this.buttonHandler);
        this.buttons.add(this.btn_exit);

        for (final TextButton b : this.buttons) {
            this.table.add(b).prefWidth(Gdx.graphics.getWidth() * 0.2f).row();
        }
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void draw() {
        this.getBatch().begin();
        this.background.draw(this.getBatch(), 0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        this.getBatch().end();
        super.draw();
    }

    private class ButtonHandler extends ClickListener {
        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == MainMenuStage.this.btn_exit) {
                Gdx.app.exit();
            } else if (event.getListenerActor() == MainMenuStage.this.btn_tutorial) {
                try {
                    MainMenuStage.this.parent.setStage(new TutorialListStage(
                            MainMenuStage.this.parent));
                    MainMenuStage.this.dispose();
                } catch (final Exception e) {
                    e.printStackTrace();
                }

            } else if (event.getListenerActor() == MainMenuStage.this.btn_multi) {
                MainMenuStage.this.parent.setStage(new MultiPlayerLobby(
                        MainMenuStage.this.parent));
                MainMenuStage.this.dispose();
            }
        }
    }
}
