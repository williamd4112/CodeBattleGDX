package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class APIList extends Table {
    private final String[] items = { "moveRight", "moveLeft", "moveUp", "moveDown", "attack",
            "skill1", "skill2", "skill3" };

    private final ScrollPane pane;
    private final List<String> list;

    public APIList(final Skin skin) {
        super();
        this.list = new List<String>(skin);
        this.list.setItems(this.items);
        this.pane = new ScrollPane(this.list, skin);

    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);
        this.add(this.pane)
                .height(height * 0.2f)
                .width(width * 0.2f);
    }
}
