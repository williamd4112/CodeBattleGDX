package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameDialog extends Table implements Resizeable {

    private LabelPanel content;
    public String context;
    private Label l;
    private int index = 0;

    public GameDialog(String context, Skin skin) {
        super();
        this.content = new LabelPanel(context, skin);
        this.l = new Label(context, skin);
        this.context = context;
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.setFillParent(true);
        this.index = 0;
        this.content.resize(width, height);
        this.add(content)
                .expand()
                .bottom();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.index < this.context.length())
            this.content.setText(context.substring(0, this.index++));

    }
}
