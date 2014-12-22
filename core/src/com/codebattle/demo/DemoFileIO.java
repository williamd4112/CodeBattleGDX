package com.codebattle.demo;

import java.io.IOException;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorDescription;
import com.codebattle.utility.XMLUtil;

public class DemoFileIO implements Screen {
	
	public DemoFileIO()
	{
		try {
			XmlReader.Element root = XMLUtil.readXMLFromFile("actors/Saber.xml");
			GameActorDescription desc = new GameActorDescription(root);
			System.out.println(desc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		
	}

}
