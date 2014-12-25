package com.codebattle.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.model.gameactor.GameActorProperties;

public class StateTable extends Table
{
	private String[] list = {"HP" , "MP" , "ATK" , "DEF" , "RANGE"};
	private String[] values = {"--", "--", "--", "--", "--"};
	
	private List<Label> keyLabels;
	private List<Label> valLabels;
	
	private GameActorProperties prop = null;
	
	public StateTable(Skin skin)
	{
		super();
		this.keyLabels = new ArrayList<Label>();
		this.valLabels = new ArrayList<Label>();
		for(String s : list) {
			this.keyLabels.add(new Label(s , skin));
		}
		for(String v : values) {
			this.valLabels.add(new Label(v , skin));
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
			this.add(key).spaceRight(width * 0.025f).left();
			this.add(val).left();
			this.row();
		}
	}
	
	public void setProperties(GameActorProperties prop)
	{
		this.prop = prop;
		String[] propArr = this.prop.getPropertyArray();
		for(int i = 0 ; i < this.valLabels.size() ; i++) {
			this.valLabels.get(i).setText(propArr[i]);
		}
	}
	
	public void resetProperties()
	{
		for(int i = 0 ; i < this.valLabels.size() ; i++) {
			this.valLabels.get(i).setText(values[i]);
		}
	}

}
