package com.codebattle.gui;

import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class APIList extends Table {
    private Object[] defaultItems = { "" };

    private Map<String, String> map;
    private ScrollPane pane;
    private List<Object> list;
    private Label label;

    public APIList(final Skin skin) {
        super();
        this.setColor(Color.BLACK);
        this.label = new Label("", skin);
        this.list = new List<Object>(skin);
        this.list.setItems(defaultItems);
        this.pane = new ScrollPane(list, skin);
        this.list.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selection = list.getSelected()
                        .toString();
                if (map != null) {
                    if (map.containsKey(selection)) {
                        label.addAction(Actions.alpha(0));
                        label.setText(map.get(selection));
                        label.addAction(Actions.sequence(Actions.show(), Actions.fadeIn(0.8f)));
                    }
                }
            }

        });
    }

    public void resize(int width, int height) {
        this.reset();
        // this.setDebug(true);
        // this.label.setFontScale(width * 0.0009f);
        this.add(pane)
                .height(height * 0.2f)
                .width(width * 0.2f);
        this.add(label)
                .pad(10)
                .top();
    }

    public void setAPIList(Map<String, String> apiList) {
        this.resetAPIList();
        this.map = apiList;
        java.util.List<Object> items = new LinkedList<Object>();
        for (String key : apiList.keySet())
            items.add(key);
        this.list.setItems(items.toArray());
    }

    public void resetAPIList() {
        this.list.setItems(this.defaultItems);
        this.label.setText("");
    }
}
