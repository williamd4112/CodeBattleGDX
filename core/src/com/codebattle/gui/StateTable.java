package com.codebattle.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class StateTable extends Table implements Resizeable {
    private String[] list = { "", "", "", "", "" };
    private String[] values = { "", "", "", "", "" };

    private List<Label> keyLabels;
    private List<Label> valLabels;

    public StateTable(Skin skin) {
        super();
        this.keyLabels = new ArrayList<Label>();
        this.valLabels = new ArrayList<Label>();
        for (String s : list) {
            this.keyLabels.add(new Label(s, skin));
        }
        for (String v : values) {
            this.valLabels.add(new Label(v, skin));
        }
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.setDebug(true);

        for (int i = 0; i < this.keyLabels.size(); i++) {
            Label key = keyLabels.get(i);
            Label val = valLabels.get(i);
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

    public void setKeys(String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            this.keyLabels.get(i)
                    .setText(keys[i]);
        }
    }

    public void setValues(String[] values) {
        for (int i = 0; i < values.length; i++) {
            this.valLabels.get(i)
                    .setText(values[i]);
        }
    }

    public void resetShowable() {
        for (int i = 0; i < this.valLabels.size(); i++) {
            this.valLabels.get(i)
                    .setText(values[i]);
        }

        for (int i = 0; i < this.keyLabels.size(); i++) {
            this.keyLabels.get(i)
                    .setText(list[i]);
        }
    }

}
