package com.codebattle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.codebattle.utility.GameConstants;

import java.util.LinkedList;
import java.util.Map;

public class APIList extends Table {
    private final Object[] defaultItems = { "" };

    private Map<String, String> map;
    private final ScrollPane pane;
    private final List<Object> list;
    private final Label label;

    public APIList(final Skin skin) {
        super();
        this.setColor(Color.BLACK);
        this.label = new Label("", skin);
        this.list = new List<Object>(skin);
        this.list.setItems(this.defaultItems);
        this.pane = new ScrollPane(this.list, skin);
        this.list.addListener(new ChangeListener() {

            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                final String selection = APIList.this.list.getSelected().toString();
                if (APIList.this.map != null) {
                    if (APIList.this.map.containsKey(selection)) {
                        APIList.this.label.addAction(Actions.alpha(0));
                        APIList.this.label.setText(APIList.this.map.get(selection));
                        APIList.this.label.addAction(Actions.sequence(Actions.show(),
                                Actions.fadeIn(0.8f)));
                    }
                }
            }

        });
    }

    public void resize(final int width, final int height) {
        this.reset();
        // this.setDebug(true);
        // this.label.setFontScale(width * 0.0009f);
        this.add(this.pane).height(height * 0.2f).width(width * 0.2f);
        this.add(this.label).pad(10).top();
    }

    public void setAPIList(final Map<String, String> apiList) {
        this.map = apiList;
        final java.util.List<Object> items = new LinkedList<Object>();
        for (final String key : apiList.keySet()) {
            items.add(key);
        }
        this.list.setItems(items.toArray());
    }

    public void resetAPIList() {
        this.setAPIList(GameConstants.AVAILABLE_GAMEACTORS);
        // this.list.setItems(this.defaultItems);
        this.label.setText("");
    }
}
