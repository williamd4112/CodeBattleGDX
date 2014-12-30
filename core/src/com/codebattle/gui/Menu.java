package com.codebattle.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Menu extends Table implements Resizeable {
    private final String[] items = { "Options", "Save", "Load", "Exit" };
    private boolean isShow = false;

    private final TextButton menuBtn;
    private final List<TextButton> btnList;

    public Menu(final Skin skin) {
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
