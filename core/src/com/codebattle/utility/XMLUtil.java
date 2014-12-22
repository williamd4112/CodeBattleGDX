package com.codebattle.utility;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorDescription;

public class XMLUtil {
	
	public static GameActorDescription readGameActorDescFromFile(String path) throws IOException
	{
		XmlReader reader = new XmlReader();
		XmlReader.Element root = reader.parse(Gdx.files.internal(path));
		
		return new GameActorDescription(root);
	}
	
	public static XmlReader.Element readXMLFromFile(String path) throws IOException
	{
		XmlReader reader = new XmlReader();
		XmlReader.Element root = reader.parse(Gdx.files.internal(path));
		
		return root;
	}
}
