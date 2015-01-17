package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.gui.editor.JSCodeEditorContainer;
import com.codebattle.gui.editor.JSCodeEditorMode;
import com.codebattle.gui.editor.JSCodeEditorStateListener;
import com.codebattle.model.GameStage;

public class Editor extends Table implements JSCodeEditorStateListener {
    final public GameStage stage;

    private boolean isShow = true;
    private final EditorBar bar;

    private final JSCodeEditorContainer editor;
    private final ScrollPane editorPane;
    private final Bar editorStateBar;

    private final TextButton button;

    public Editor(final GameStage stage, final Skin skin) {
        super();
        this.stage = stage;
        this.bar = new EditorBar(skin);

        this.editor = new JSCodeEditorContainer(skin);
        this.editor.addStateListener(this);
        this.editorPane = new ScrollPane(this.editor, skin);
        this.editorStateBar = new Bar("VISUAL Row: 0 Col: 0", skin);

        this.button = new TextButton("Submit", skin);
    }

    public void addHandler(final ClickListener handler) {
        this.button.addListener(handler);
    }

    public String getText() {
        return this.editor.getText();
    }

    public void setText(final String text) {
        this.editor.setText(text);
    }

    public void setDisable(final boolean b) {
        // this.editor.setDisabled(b);
        this.button.setDisabled(b);
        this.editor.setVisible(!b);
        this.setVisible(!b);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.bar.resize(width, height);
        this.editorStateBar.resize(width, height);
        this.add(this.bar).expandX().fillX().row();
        this.add(this.editorPane).expand().fill();
        this.row();
        this.add(this.editorStateBar).expandX().fillX().row();
        this.add(this.button).fill();
    }

    private class EditorBar extends BasePanel {
        private final TextButton minBtn;
        private final Label title;

        public EditorBar(final Skin skin) {
            super(skin);
            this.minBtn = new TextButton("-", skin);
            this.minBtn.addListener(new ClickListener() {

                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    super.clicked(event, x, y);
                    Editor.this.isShow = !Editor.this.isShow;
                    if (!Editor.this.isShow) {
                        Editor.this.editorPane.addAction(Actions.sequence(
                                Actions.fadeOut(0.8f),
                                Actions.hide()));
                        Editor.this.button.addAction(Actions.sequence(
                                Actions.moveBy(
                                        0,
                                        Gdx.graphics.getHeight() - 2
                                                * Editor.this.button.getHeight(), 0.8f),
                                Actions.hide()));
                    } else {
                        Editor.this.editorPane.addAction(Actions.sequence(Actions.show(),
                                Actions.alpha(0.8f, 0.8f)));
                        Editor.this.button.addAction(Actions.sequence(
                                Actions.show(),
                                Actions.moveBy(
                                        0,
                                        -(Gdx.graphics.getHeight() - 2 * Editor.this.button.getHeight()),
                                        0.8f)));
                    }
                }

            });
            this.title = new Label("Editor", skin);
        }

        public void resize(final int width, final int height) {
            this.reset();
            this.add(this.title).padLeft(10).expandX().left();
            this.add(this.minBtn).fill().right();
        }

    }

    @Override
    public void onCursorExceedHeight(final int lines) {
        this.editorPane.setScrollY(100);

    }

    @Override
    public void onCursorExceedWidth(final float width) {
        this.editorPane.setScrollX(100);

    }

    @Override
    public void onCursorChange(final JSCodeEditorMode mode, final int col, final int row) {
        this.editorStateBar.setText(String.format("%s   Row: %d Col: %d", mode.name(), row,
                col));
    }
}
