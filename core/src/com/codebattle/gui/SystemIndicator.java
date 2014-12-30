package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.codebattle.model.SystemListener;

public class SystemIndicator extends BasePanel implements SystemListener {

    private Label resKey;
    private Label resVal;
    private Label hpKey;
    private Label hpVal;

    public SystemIndicator(Skin skin) {
        super(skin);
        this.resKey = new Label("Resource: ", skin);
        this.resVal = new Label(String.valueOf(4000), skin);
        this.hpKey = new Label("Life: ", skin);
        this.hpVal = new Label(String.valueOf(4000), skin);
    }

    public void resize(int width, int height) {
        this.reset();
        this.pad(5);
        this.add(resKey)
                .space(5);
        this.add(resVal)
                .space(5);
        this.add(hpKey)
                .space(5);
        this.add(hpVal)
                .space(5);
    }

    @Override
    public void onLifeChange(int newValue) {
        this.hpVal.setText(String.valueOf(newValue));

    }

    @Override
    public void onResourceChange(int newValue) {
        this.resVal.setText(String.valueOf(newValue));

    }
}
