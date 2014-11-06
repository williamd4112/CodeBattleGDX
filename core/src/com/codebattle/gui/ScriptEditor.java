package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/*
 * ScriptEditor
 * @skin : the style of the GUI
 * @edtTxt : a textarea which get user's input
 * @edtLabel : a normal label
 * @edtBtn : a submit button
 * */

public class ScriptEditor extends Table{
	
	final private Skin skin;
	final private TextArea edtTxt;
	final private Label edtLabel;
	final private TextButton edtBtn;
	
	/*
	 * ScriptEditor Constructor
	 * 
	 * */
	public ScriptEditor(ClickListener submitHandler)
	{
		skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
		edtLabel = new Label("Script Editor" , skin);
		edtTxt = new TextArea("Enter your script here..." , skin);
		edtBtn = new TextButton("submit" , skin);
		edtBtn.addListener(submitHandler);
	}
	
	/*Called by game scene recursively
	 *@param - width : new screen width
	 *@param - height : new screen height*/
	public void resize(int width , int height)
	{
		setSize(width, height);
		reset();
		set();
	}
	
	/*get the user's input*/
	public String getScript()
	{
		return edtTxt.getText();
	}
	
	/*set the position for all GUI components*/
	private void set()
	{
		add(edtLabel).expandX().align(Align.right).width(this.getWidth() * 0.3f);
		row();
		add(edtTxt).expand().fillY().align(Align.right).width(this.getWidth() * 0.3f);
		row();
		add(edtBtn).expandX().align(Align.right).width(this.getWidth() * 0.3f);
	}
	

}
