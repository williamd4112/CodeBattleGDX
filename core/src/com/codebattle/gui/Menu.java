package com.codebattle.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.model.GameStage;
import com.codebattle.scene.StartupScene;

public class Menu extends Table implements Resizeable {
    final GameStage stage;

    private final String[] items = { "Options", "Save", "Load", "Exit" };
    private boolean isShow = false;

    private final TextButton menuBtn;
    private final List<TextButton> btnList;

    private final TextButton btn_exit;

    public Menu(final GameStage stage, final Skin skin) {
        this.stage = stage;
        this.btnList = new ArrayList<TextButton>();
        this.menuBtn = new TextButton("Menu", skin);
        this.menuBtn.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                Menu.this.isShow = !Menu.this.isShow;
                for (final TextButton btn : Menu.this.btnList) {
                    btn.setVisible(Menu.this.isShow);
                }
            }

        });

        for (final String s : this.items) {
            final TextButton btn = new TextButton(s, skin);
            this.btnList.add(btn);
            btn.setVisible(false);
        }

        this.btn_exit = btnList.get(3);
        this.btn_exit.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.parent.parent.setScene(new StartupScene(stage.parent.parent));
                stage.parent.dispose();

            }

        });
    }

    @Override
    public void resize(final int width, final int height) {
        this.reset();

        this.add(this.menuBtn)
                .left()
                .row()
                .fillX();
        for (final TextButton btn : this.btnList) {
            this.add(btn)
                    .left()
                    .row()
                    .fillX();
        }
    }

    public void setShow(final boolean flag) {
        this.isShow = flag;
    }

}
