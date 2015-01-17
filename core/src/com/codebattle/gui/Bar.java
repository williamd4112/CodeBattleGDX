package com.codebattle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Bar extends BasePanel implements Resizeable {

    private final Label label;

    public Bar(final String s, final Skin skin) {
        super(skin);
        this.label = new Label(s, skin);
        this.setColor(Color.BLACK);
    }

    @Override
    public void resize(final int width, final int height) {
        this.reset();
        this.add(this.label).left();
    }

    public void setText(final String s) {
        this.label.setText(s);
    }

    public String getText() {
        return this.label.getText().toString();
    }
}
