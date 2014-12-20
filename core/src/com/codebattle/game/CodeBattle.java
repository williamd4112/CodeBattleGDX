package com.codebattle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.codebattle.model.GameActorProperties;
import com.codebattle.scene.GameScene;
import com.codebattle.utility.GameConstants;

public class CodeBattle extends Game {
    @Override
    public void create() {
        this.setScreen(new GameScene("demo2"));
    }

    @Override
    public void render() {
        super.render();
    }
}
