package com.codebattle.demo;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DemoUIListener extends ClickListener {

	@Override
	public void clicked(InputEvent event, float x, float y) {
		// TODO Auto-generated method stub
		super.clicked(event, x, y);
		System.out.println("Clicked");
	}
	

}
