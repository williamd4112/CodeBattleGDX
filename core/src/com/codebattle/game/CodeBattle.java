package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.codebattle.scene.SinglePlayerGameScene;
import com.codebattle.utility.GameConstants;

public class CodeBattle extends Game {
    @Override
    public void create() {

        try {
        	GameConstants.init();
            ShaderProgram.pedantic = false;
            // this.setScreen(new DemoLayout());
            this.setScreen(new SinglePlayerGameScene("scene_demo"));
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        super.render();
    }
}
