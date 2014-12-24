package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.codebattle.demo.DemoLightScene;
import com.codebattle.demo.DemoFileIO;
import com.codebattle.demo.DemoBox2DLightScene;
import com.codebattle.scene.GameScene;
import com.codebattle.scene.SinglePlayerGameScene;

public class CodeBattle extends Game {
    @Override
    public void create() {
    	
        try {
        	ShaderProgram.pedantic = false;
			//this.setScreen(new DemoLayout());
        	setScreen(new SinglePlayerGameScene("scene_demo"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void render() {
        super.render();
    }
}
