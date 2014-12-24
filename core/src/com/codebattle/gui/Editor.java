package com.codebattle.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Editor extends Table{
	
	private boolean isShow = true;
	private EditorBar bar;
	private TextArea editText;
	private TextButton button;
	
	public Editor(ClickListener handler, Skin skin)
	{
		super();
		this.bar = new EditorBar(skin);
		
		this.editText = new TextArea("Editor" , skin);
		this.button = new TextButton("Submit" , skin);
		this.button.addListener(handler);
	}
	
	public String getText()
	{
		return this.editText.getText();
	}
	
	public void resize(int width, int height)
	{
		this.reset();
		this.bar.resize(width, height);
		this.add(bar).expandX().fillX().row();
		this.add(editText).expand().fill();
		this.row();
		this.add(button).fill();
	}
	
	private class EditorBar extends BasePanel
	{
		private TextButton minBtn;
		private Label title;
		
		public EditorBar(Skin skin) 
		{
			super(skin);
			this.minBtn = new TextButton("-" , skin);
			this.minBtn.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					isShow = !isShow;
					editText.setVisible(isShow);
					button.setVisible(isShow);
				}
				
			});
			this.title = new Label("Editor" , skin);
		}
		
		public void resize(int width , int height)
		{
			this.reset();
			this.add(title).padLeft(10).expandX().left();
			this.add(minBtn).fill().right();
		}
		
	}
}
