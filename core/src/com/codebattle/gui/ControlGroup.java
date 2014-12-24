package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ControlGroup extends Table {
    // private TextButton button;
    private final Menu menu;
    private final Panel panel;

    public ControlGroup(final Skin skin) {
        super();
        // this.button = new TextButton("Menu",skin);
        this.menu = new Menu(skin);
        this.panel = new Panel(skin);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);
        this.menu.resize(width, height);
        this.panel.resize(width, height);
        this.add(this.menu)
                .expand()
                .top()
                .left();
        // this.add(button).expand().top().left();
        this.row();
        this.add(this.panel)
                .bottom()
                .left();
    }
}
