package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.model.GameStage;

public class ControlGroup extends Table {
    final GameStage stage;

    private Menu menu;
    private SystemIndicator sysIndicator;
    private Panel panel;

    public ControlGroup(GameStage stage, Skin skin) {
        super();
        this.stage = stage;
        this.menu = new Menu(stage, skin);
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
