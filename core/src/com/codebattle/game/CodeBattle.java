package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.codebattle.demo.DemoFileIO;
import com.codebattle.scene.GameScene;
import com.codebattle.scene.SinglePlayerGameScene;

public class CodeBattle extends Game {
    @Override
    public void create() {
    	
        try {
			this.setScreen(new SinglePlayerGameScene("scene_demo"));
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
