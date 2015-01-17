package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

public class StateTable extends Table implements Resizeable {
    private final String[] list = { "", "", "", "", "" };
    private final String[] values = { "", "", "", "", "" };

    private final List<Label> keyLabels;
    private final List<Label> valLabels;

    public StateTable(final Skin skin) {
        super();
        this.keyLabels = new ArrayList<Label>();
        this.valLabels = new ArrayList<Label>();
        for (final String s : this.list) {
            this.keyLabels.add(new Label(s, skin));
        }
        for (final String v : this.values) {
            this.valLabels.add(new Label(v, skin));
        }
    }

    @Override
    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);

        for (int i = 0; i < this.keyLabels.size(); i++) {
            final Label key = this.keyLabels.get(i);
            final Label val = this.valLabels.get(i);
            // key.setFontScale(width * 0.0009f);
            // val.setFontScale(width * 0.0009f);
            this.add(key)
                    .prefSize(width * 0.01f)
                    .spaceRight(width * 0.025f)
                    .left();
            this.add(val)
                    .prefSize(width * 0.01f)
                    .left();
            this.row();
        }
    }

    public void setKeys(final String[] keys) {
        this.resetKeys();
        for (int i = 0; i < keys.length; i++) {
            this.keyLabels.get(i)
                    .setText(keys[i]);
        }
    }

    public void setValues(final String[] values) {
        this.resetValues();
        for (int i = 0; i < values.length; i++) {
            this.valLabels.get(i)
                    .setText(values[i]);
        }
    }

    public void resetKeys() {
        for (int i = 0; i < this.keyLabels.size(); i++) {
            this.keyLabels.get(i)
                    .setText(this.list[i]);
        }
    }

    public void resetValues() {
        for (int i = 0; i < this.valLabels.size(); i++) {
            this.valLabels.get(i)
                    .setText(this.values[i]);
        }
    }

    public void resetShowable() {
        this.resetKeys();
        this.resetValues();
    }

}
