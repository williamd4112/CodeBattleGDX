package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameSceneGUI extends Table {

    private final ControlGroup controlGroup;
    private final Editor editor;

    public GameSceneGUI(final ClickListener handler) {
        super();
        final Skin skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
        this.controlGroup = new ControlGroup(skin);
        this.editor = new Editor(handler, skin);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setFillParent(true);
        this.editor.resize(width, height);
        this.controlGroup.resize(width, height);
        this.add(this.controlGroup)
                .expand()
                .fill();
        this.add(this.editor)
                .width(width * 0.3f)
                .fill();
    }

    public Editor getEditor() {
        return this.editor;
    }

}
