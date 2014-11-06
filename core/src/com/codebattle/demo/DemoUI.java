package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DemoUI implements Screen{
	
	final private Stage stage;
	private Table table;
	private ShapeRenderer shapeRenderer;
	private Skin skin;
	
	TextArea txtArea;
	ScrollPane pane;
	DemoObject obj;
	
	public DemoUI()
	{
		skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
		txtArea = new TextArea("String" , skin);
		
		stage = new Stage();
		ScrollPane pane2 = new ScrollPane(new Image(new Texture("badlogic.jpg")), skin);
		pane2.setScrollingDisabled(false, true);
		// pane2.setCancelTouchFocus(false);
		pane2.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return true;
			}
		});

		table = new Table();
		table.debug();
		//table.add(txtArea).expand().fill();
		//table.add(new Image(new Texture("badlogic.jpg")));
		//table.row();
		//table.add(new Image(new Texture("badlogic.jpg")));
		//table.row();
		//table.add(pane2).size(100);
	    //table.row();
		//table.add(new Image(new Texture("badlogic.jpg")));
		//table.row();
		//table.add(new Image(new Texture("badlogic.jpg")));

		ScrollPane pane = new ScrollPane(txtArea, skin);
		pane.setScrollingDisabled(true, false);
		// pane.setCancelTouchFocus(false);
		if (false) {
			// This sizes the pane to the size of it's contents.
			pane.pack();
			// Then the height is hardcoded, leaving the pane the width of it's contents.
			pane.setHeight(Gdx.graphics.getHeight());
		} else {
			// This shows a hardcoded size.
			pane.setWidth(300);
			pane.setHeight(Gdx.graphics.getHeight());
		}

		stage.addActor(pane);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(delta);
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height , true);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		shapeRenderer.dispose();
	}

}
