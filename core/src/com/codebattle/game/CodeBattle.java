package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.codebattle.scene.GameScene;

public class CodeBattle extends Game {
    @Override
    public void create() {
        this.setScreen(new GameScene("demo"));
    }

    @Override
    public void render() {
        super.render();
    }
}
