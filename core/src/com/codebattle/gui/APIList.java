package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class APIList extends Table
{
	private String[] items = {"moveRight" , "moveLeft" , "moveUp" , "moveDown" , "attack" , "skill1","skill2","skill3"};
	
	private ScrollPane pane;
	private List<String> list;
	
	public APIList(Skin skin)
	{
		super();
		this.list = new List<String>(skin);
		list.setItems(items);
		this.pane = new ScrollPane(list, skin);
		
	}
	
	public void resize(int width , int height)
	{
		this.reset();
		this.setDebug(true);
		this.add(pane).height(height * 0.2f).width(width * 0.2f);
	}
}
