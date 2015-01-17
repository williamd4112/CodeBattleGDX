package com.codebattle.gui.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.codebattle.gui.Resizeable;

public class JSCodeEditorContainer extends VerticalGroup implements Resizeable {
    private JSCodeEditorCore core;

    public JSCodeEditorContainer(Skin skin) {
        super();
        this.core = new JSCodeEditorCore(skin);
        this.core.setFillParent(true);
        this.addActor(core);
    }

    public String getText() {
        return this.core.getText();
    }

    public void setText(String s) {
        this.core.setText(s);
    }

    public void addStateListener(JSCodeEditorStateListener l) {
        this.core.addCodeEditorListener(l);
    }

    @Override
    public void resize(int width, int height) {

    }

}
