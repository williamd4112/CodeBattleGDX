package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureFactory {
	
	private static TextureFactory instance = null;
	
	private TextureFactory(){}
	
	public static TextureFactory getInstance()
	{
		return (instance == null) ? (instance = new TextureFactory()) : instance;
	}
	
	public Texture loadTextureFromFile(String resName)
	{
		return new Texture(Gdx.files.internal("graphics/characters/" + resName + ".png"));
		
	}
	
	public TextureRegion[][] loadTextureRegionFromFile(String resName , int horizontal , int vertical , int hTileSize , int vTileSize)
	{
		TextureRegion[][] regions = new TextureRegion[vertical][horizontal];
		Texture texture = loadTextureFromFile(resName);
		
		for(int v = 0 ; v < vertical ; v++) {
			for(int h = 0 ; h < horizontal ; h++) {
				regions[v][h] = new TextureRegion(texture , h * hTileSize , v * vTileSize , hTileSize , vTileSize);
			}
		}
		
		return regions;
	}
}
