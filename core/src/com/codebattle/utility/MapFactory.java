package com.codebattle.utility;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/*
 * MapFactory
 * - A utility of map , obtain a series of function to process map
 * */

public class MapFactory {
	
	/* Load a map from file
	 * @mapName: a name of the map
	 * */
	public static TiledMap loadMapFromFile(String mapName)
	{
		TiledMap map = new TmxMapLoader().load("level/" + mapName + ".tmx");
		
		return (map == null) ? null : map;
	}
}
