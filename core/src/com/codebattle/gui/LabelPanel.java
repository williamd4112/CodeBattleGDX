package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class LabelPanel extends BasePanel implements Resizeable {

    private TextArea textArea;

    // private Label l;

    public LabelPanel(String content, Skin skin) {
        super(skin);
        // this.l = new Label("Hello World", skin);
        this.textArea = new TextArea(content, skin);
        this.textArea.setDisabled(true);
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.pad(5);
        this.add(this.textArea)
                .prefWidth(width * 0.95f)
                .prefHeight(height * 0.3f);
    }

    public void setText(String text) {
        this.textArea.setText(text);
    }
}
