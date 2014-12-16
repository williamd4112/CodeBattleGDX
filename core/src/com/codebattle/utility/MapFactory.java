package com.codebattle.utility;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Map factory.
 *
 * A utility of map, obtain a series of function to process the map.
 */
public class MapFactory {
    /**
     * Load a map from a file.
     *
     * @param mapName   Name of the map
     * @return A tiled map.
     */
    public static TiledMap loadMapFromFile(final String mapName) {
        return new TmxMapLoader().load("level/" + mapName + ".tmx");
    }
}
