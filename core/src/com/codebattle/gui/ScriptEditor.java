package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScriptEditor extends Table {
    private final Skin skin;
    private final TextArea scriptEditor;
    private final Label scriptEditorLabel;
    private final TextButton submitButton;

    public ScriptEditor(final ClickListener submitHandler) {
        this.skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
        this.scriptEditorLabel = new Label("Script Editor", this.skin);
        this.scriptEditor = new TextArea("Enter your script here...", this.skin);
        this.submitButton = new TextButton("submit", this.skin);
        this.submitButton.addListener(submitHandler);
    }

    /**
     * Called by game scene recursively.
     *
     * @param width     New screen width
     * @param height    New screen height
     */
    public void resize(final int width, final int height) {
        this.setSize(width, height);
        this.reset();
        this.set();
    }

    /**
     * Get user's input.
     *
     * @return User's input.
     */
    public String getScript() {
        return this.scriptEditor.getText();
    }

    /**
     * Set the position for all GUI components.
     */
    private void set() {
        this.add(this.scriptEditorLabel)
                .expandX()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
        this.row();
        this.add(this.scriptEditor)
                .expand()
                .fillY()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
        this.row();
        this.add(this.submitButton)
                .expandX()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
    }
}
