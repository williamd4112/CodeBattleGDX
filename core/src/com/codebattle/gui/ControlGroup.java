package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ControlGroup extends Table {
    // private TextButton button;
    private Menu menu;
    private SystemIndicator sysIndicator;
    private Panel panel;

    public ControlGroup(Skin skin) {
        super();
        this.menu = new Menu(skin);
        this.sysIndicator = new SystemIndicator(skin);
        this.panel = new Panel(skin);
    }

    public void resize(int width, int height) {
        this.reset();
        this.sysIndicator.resize(width, height);
        this.menu.resize(width, height);
        this.panel.resize(width, height);

        this.add(menu)
                .expand()
                .top()
                .left();
        this.row();
        this.add(sysIndicator)
                .left()
                .row();
        this.add(panel)
                .bottom()
                .left();
    }

    public Panel getPanel() {
        return this.panel;
    }

    public SystemIndicator getSystemIndicator() {
        return this.sysIndicator;
    }
}
