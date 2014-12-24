package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

public class StateTable extends Table {
    private final String[] list = { "HP", "MP", "ATK", "DEF", "RANGE" };
    private final int[] values = { 100, 50, 10, 5, 1 };

    private final List<Label> keyLabels;
    private final List<Label> valLabels;

    public StateTable(final Skin skin) {
        super();
        this.keyLabels = new ArrayList<Label>();
        this.valLabels = new ArrayList<Label>();
        for (final String s : this.list) {
            this.keyLabels.add(new Label(s, skin));
        }
        for (final int i : this.values) {
            this.valLabels.add(new Label(String.valueOf(i), skin));
        }
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);

        for (int i = 0; i < this.keyLabels.size(); i++) {
            final Label key = this.keyLabels.get(i);
            final Label val = this.valLabels.get(i);
            key.setSize(width * 0.02f, width * 0.02f);
            val.setSize(width * 0.02f, width * 0.02f);
            this.add(key)
                    .left();
            this.add(val)
                    .left();
            this.row();
        }
    }

}
