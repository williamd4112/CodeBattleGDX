package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

abstract public class BasePanel extends Table
{
	
	TextFieldStyle style;
	
	public BasePanel(Skin skin)
	{
		super();
		this.setStyle(skin);
	}
	
	public void setStyle(Skin skin)
	{
		this.style = skin.get(TextFieldStyle.class);
		final Drawable background = (style.disabledBackground != null) ? style.disabledBackground
				: ((style.focusedBackground != null) ? style.focusedBackground : style.background);
		this.setBackground(background);
		invalidateHierarchy();
	}
}
