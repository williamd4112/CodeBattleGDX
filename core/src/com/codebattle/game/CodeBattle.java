package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.codebattle.scene.SinglePlayerGameScene;

public class CodeBattle extends Game {
    @Override
    public void create() {

        try {
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
