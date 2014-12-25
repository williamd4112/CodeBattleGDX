package com.codebattle.gui;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class APIList extends Table
{
	//private String[] items = {"moveRight" , "moveLeft" , "moveUp" , "moveDown" , "attack" , "skill1","skill2","skill3"};
	private String[] defaultItems = {""};
	private Map<String , String> map;
	private ScrollPane pane;
	private List<String> list;
	private Label label;
	
	public APIList(final Skin skin)
	{
		super();
		this.label = new Label("" , skin);
		this.list = new List<String>(skin);
		list.setItems(defaultItems);
		//list.setItems(items);
		this.pane = new ScrollPane(list, skin);
		
		this.list.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				System.out.println(list.getSelected());
				label.setText(list.getSelected());
			}
			
		});
	}
	
	public void resize(int width , int height)
	{
		this.reset();
		this.setDebug(true);
		this.add(pane).height(height * 0.2f).width(width * 0.2f);
		this.add(label);
	}
	
	public void setAPIList(Map<String , String> apiList)
	{
		this.map = apiList;
		String[] items = new String[apiList.size()];
		this.list.setItems(items);
	}
	
	public void resetAPIList()
	{
		this.list.setItems(this.defaultItems);
	}
}
