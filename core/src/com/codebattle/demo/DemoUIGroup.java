package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DemoUIGroup extends Table{
	
	final private Skin skin;
	final private TextArea edtTxt;
	final private Label edtLabel;
	final private TextButton edtBtn;
	
	public DemoUIGroup()
	{
		skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
		edtLabel = new Label("Script Editor" , skin);
		edtTxt = new TextArea("Enter your script here..." , skin);
		edtBtn = new TextButton("submit" , skin);
		edtBtn.addListener(new ClickListener() {
		    public void clicked(InputEvent event, float x, float y) {
		        	System.out.println(edtTxt.getText());
		    }
		});
		set();
	}
	
	public void resize(int width , int height)
	{
		setSize(width, height);
		reset();
		set();
	}
	
	public void set()
	{
		add(edtLabel).expandX().align(Align.right).width(this.getWidth() * 0.3f);
		row();
		add(edtTxt).expand().fillY().align(Align.right).width(this.getWidth() * 0.3f);
		row();
		add(edtBtn).expandX().align(Align.right).width(this.getWidth() * 0.3f);
	}
	
	public String getEdtText()
	{
		return edtTxt.getText();
	}

}
