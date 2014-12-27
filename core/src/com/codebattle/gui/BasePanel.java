package com.codebattle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

abstract public class BasePanel extends Table {

    TextFieldStyle style;

    public BasePanel(final Skin skin) {
        super();
        this.setStyle(skin);
    }

    public void setStyle(final Skin skin) {
        this.style = skin.get(TextFieldStyle.class);
        final Drawable background = this.style.disabledBackground != null ? this.style.disabledBackground
                : this.style.focusedBackground != null ? this.style.focusedBackground
                        : this.style.background;
        this.setBackground(background);
        this.invalidateHierarchy();
    }
}
