package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Editor extends Table {

    private boolean isShow = true;
    private final EditorBar bar;
    private final TextArea editText;
    private final TextButton button;

    public Editor(final ClickListener handler, final Skin skin) {
        super();
        this.bar = new EditorBar(skin);
        this.editText = new TextArea("Editor", skin);
        this.editText.addAction(Actions.alpha(0.8f));

        this.button = new TextButton("Submit", skin);
        this.button.addListener(handler);
    }

    public String getText() {
        return this.editText.getText();
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.bar.resize(width, height);
        this.add(this.bar)
                .expandX()
                .fillX()
                .row();
        this.add(this.editText)
                .expand()
                .fill();
        this.row();
        this.add(this.button)
                .fill();
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
                        editText.addAction((Actions.sequence(Actions.fadeOut(0.8f),
                                Actions.hide())));
                        button.addAction(Actions.sequence(
                                Actions.moveBy(0,
                                        Gdx.graphics.getHeight() - 2 * button.getHeight(), 0.8f),
                                Actions.hide()));
                    } else {
                        editText.addAction((Actions.sequence(Actions.show(),
                                Actions.alpha(0.8f, 0.8f))));
                        button.addAction(Actions.sequence(Actions.show(), Actions.moveBy(0,
                                -(Gdx.graphics.getHeight() - 2 * button.getHeight()), 0.8f)));
                    }
                }

            });
            this.title = new Label("Editor", skin);
        }

        public void resize(final int width, final int height) {
            this.reset();
            this.add(this.title)
                    .padLeft(10)
                    .expandX()
                    .left();
            this.add(this.minBtn)
                    .fill()
                    .right();
        }

    }
}
