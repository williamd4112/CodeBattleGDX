package com.codebattle.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class StateTable extends Table
{
	private String[] list = {"HP" , "MP" , "ATK" , "DEF" , "RANGE"};
	private int[] values = {100, 50, 10, 5, 1};
	
	private List<Label> keyLabels;
	private List<Label> valLabels;
	
	public StateTable(Skin skin)
	{
		super();
		this.keyLabels = new ArrayList<Label>();
		this.valLabels = new ArrayList<Label>();
		for(String s : list) {
			this.keyLabels.add(new Label(s , skin));
		}
		for(int i : values) {
			this.valLabels.add(new Label(String.valueOf(i) , skin));
		}
	}
	
	public void resize(int width , int height)
	{
		this.reset();
		this.setDebug(true);
		
		for(int i = 0 ; i < this.keyLabels.size() ; i++) {
			Label key = keyLabels.get(i);
			Label val = valLabels.get(i);
			key.setSize(width * 0.02f, width * 0.02f);
			val.setSize(width * 0.02f, width * 0.02f);
			this.add(key).left();
			this.add(val).left();
			this.row();
		}
	}

}
