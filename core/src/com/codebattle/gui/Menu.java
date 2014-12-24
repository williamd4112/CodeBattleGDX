package com.codebattle.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Menu extends Table
{
	private String[] items = {"Options" , "Save" , "Load" , "Exit"};
	private boolean isShow = false;
	
	private TextButton menuBtn;
	private List<TextButton> btnList;
	
	public Menu(Skin skin)
	{
		this.btnList = new ArrayList<TextButton>();
		this.menuBtn = new TextButton("Menu" , skin);
		this.menuBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				isShow = !isShow;
				for(TextButton btn : btnList) {
					btn.setVisible(isShow);
				}
			}
			
		});
		
		for(String s : items) {
			TextButton btn = new TextButton(s, skin);
			this.btnList.add(btn);
			btn.setVisible(false);
		}
	}
	
	public void resize(int width, int height)
	{
		this.reset();
		this.setDebug(true);
		this.add(menuBtn).left().row().fillX();
		for(TextButton btn : btnList) {
			this.add(btn).left().row().fillX();
		}
	}
	
	public void setShow(boolean flag)
	{
		this.isShow = flag;
	}
	
}
