package com.codebattle.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codebattle.demo.DemoScene;
import com.codebattle.demo.DemoUI;
import com.codebattle.scene.GameScene;

public class CodeBattle extends Game {
	
	@Override
	public void create () {
		this.setScreen(new GameScene("demo"));
	}

	@Override
	public void render () {
		super.render();
	}
}
