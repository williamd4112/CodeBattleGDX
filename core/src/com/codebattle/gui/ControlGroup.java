package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ControlGroup extends Table
{	
	//private TextButton button;
	private Menu menu;
	private Panel panel;
	
	public ControlGroup(Skin skin)
	{
		super();
	    //this.button = new TextButton("Menu",skin);
	    this.menu = new Menu(skin);
		this.panel = new Panel(skin);
	}
	
	public void resize(int width , int height)
	{
		this.reset();
	    this.setDebug(true);
	    this.menu.resize(width, height);
	    this.panel.resize(width, height);
	    this.add(menu).expand().top().left();
	    //this.add(button).expand().top().left();
	    this.row();
	    this.add(panel).bottom().left();
	}
}
