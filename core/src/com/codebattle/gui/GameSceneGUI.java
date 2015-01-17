package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.model.GameStage;
import com.codebattle.utility.GameConstants;

public class GameSceneGUI extends Table implements Resizeable {

    final GameStage stage;

    private final ControlGroup controlGroup;
    private final Editor editor;

    public GameSceneGUI(final GameStage stage) {
        super();
        this.stage = stage;
        this.controlGroup = new ControlGroup(stage, GameConstants.DEFAULT_SKIN);
        this.editor = new Editor(stage, GameConstants.DEFAULT_SKIN);
    }

    @Override
    public void resize(final int width, final int height) {
        this.reset();
        this.setFillParent(true);
        this.editor.resize(width, height);
        this.controlGroup.resize(width, height);
        this.add(this.controlGroup).expand().fill();
        this.add(this.editor).width(width * 0.3f).fill();
    }

    public Editor getEditor() {
        return this.editor;
    }

    public ControlGroup getControlGroup() {
        return this.controlGroup;
    }

    public void resetShowable() {
        this.controlGroup.getPanel().resetShowable();
        this.controlGroup.getPanel().resetAPI();
    }

}
