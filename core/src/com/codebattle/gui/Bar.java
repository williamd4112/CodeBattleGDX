package com.codebattle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Bar extends BasePanel implements Resizeable {

    private Label label;

    public Bar(String s, Skin skin) {
        super(skin);
        this.label = new Label(s, skin);
        this.setColor(Color.BLACK);
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.add(label).left();
    }

    public void setText(String s) {
        label.setText(s);
    }

    public String getText() {
        return this.label.getText().toString();
    }
}
