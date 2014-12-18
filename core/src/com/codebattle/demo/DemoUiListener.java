package com.codebattle.demo;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DemoUiListener extends ClickListener {
    @Override
    public void clicked(final InputEvent event, final float x, final float y) {
        // TODO Auto-generated method stub
        super.clicked(event, x, y);
        System.out.println("Clicked");
    }
}
