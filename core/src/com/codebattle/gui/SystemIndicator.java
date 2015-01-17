package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.codebattle.model.SystemListener;
import com.codebattle.utility.GameConstants;

public class SystemIndicator extends BasePanel implements SystemListener {

    private final Label resKey;
    private final Label resVal;
    private final Label hpKey;
    private final Label hpVal;

    public SystemIndicator(final Skin skin) {
        super(skin);
        this.resKey = new Label("Resource: ", skin);
        this.resVal = new Label(String.valueOf(GameConstants.INIT_RES), skin);
        this.hpKey = new Label("Life: ", skin);
        this.hpVal = new Label(String.valueOf(GameConstants.INIT_HP), skin);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.pad(5);
        this.add(this.resKey).space(5);
        this.add(this.resVal).space(5);
        this.add(this.hpKey).space(5);
        this.add(this.hpVal).space(5);
    }

    @Override
    public void onLifeChange(final int newValue) {
        this.hpVal.setText(String.valueOf(newValue));

    }

    @Override
    public void onResourceChange(final int newValue) {
        this.resVal.setText(String.valueOf(newValue));

    }
}
