package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.utility.GameConstants;

public class GameSceneGUI extends Table implements Resizeable {

    private ControlGroup controlGroup;
    private Editor editor;

    public GameSceneGUI(ClickListener handler) {
        super();

        this.controlGroup = new ControlGroup(GameConstants.DEFAULT_SKIN);
        this.editor = new Editor(handler, GameConstants.DEFAULT_SKIN);
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.setFillParent(true);
        this.editor.resize(width, height);
        this.controlGroup.resize(width, height);
        this.add(controlGroup)
                .expand()
                .fill();
        this.add(editor)
                .width(width * 0.3f)
                .fill();
    }

    public Editor getEditor() {
        return this.editor;
    }

    public ControlGroup getControlGroup() {
        return this.controlGroup;
    }

    public void resetShowable() {
        this.controlGroup.getPanel()
                .resetShowable();
        this.controlGroup.getPanel()
                .resetAPI();
    }

}
