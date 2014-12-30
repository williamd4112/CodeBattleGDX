package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface StateShowable {
    public String[] getKeys();

    public String[] getValues();

    public Drawable getPortrait();

    public String getNameInfo();

}
