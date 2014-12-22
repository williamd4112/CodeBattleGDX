package com.codebattle.utility;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.Region;

public class TextureFactory {
    private static TextureFactory instance = null;
    
    private Map<String, Texture> pool;
    
    private TextureFactory() {
    	this.pool = new HashMap<String , Texture>();
    }

    public static TextureFactory getInstance() {
        if (null == instance) {
            instance = new TextureFactory();
        }

        return instance;
    }

    /**
     * Load characters frames from file
     * @param resName
     * @param x : start x
     * @param y : start y
     * @param horizontal
     * @param vertical
     * @param hTileSize
     * @param vTileSize
     * @return
     * @throws Exception
     */
    public TextureRegion[][] loadCharacterFramesFromFile(final String resName,
            final int x, final int y, final int horizontal, final int vertical, final int hTileSize, final int vTileSize) throws Exception {
        
    	TextureRegion[][] regions = null;
		try {
			regions = new TextureRegion[vertical][horizontal];
			final Texture texture = this.loadTextureFromFile(resName , ResourceType.CHARACTER);

			for (int v = 0; v < vertical; v++) {
			    for (int h = 0; h < horizontal; h++) {
			        regions[v][h] = new TextureRegion(texture, x + h * hTileSize, y + v * vTileSize,
			                hTileSize, vTileSize);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

        return regions;
    }
    
    public TextureRegion[][] loadCharacterFramesFromFile(final String resName, final Region region) throws Exception
    {
    	int x = region.x;
    	int y = region.y;
    	int horizontal = region.width / GameConstants.CELL_SIZE;
    	int vertical = region.height / GameConstants.CELL_SIZE;
    	int hTileSize = GameConstants.CELL_SIZE;
    	int vTileSize = GameConstants.CELL_SIZE;
    	
    	return loadCharacterFramesFromFile(resName, x, y, horizontal, vertical, hTileSize, vTileSize);
    }

    public Texture loadTextureFromFile(final String resName , ResourceType type) throws Exception
    {
    	String resPath = "";
    	switch(type) {
    	case CHARACTER:
    		resPath = GameConstants.GAMEACTOR_MAP_TEXTURE_DIR + resName + GameConstants.DEFAULT_TEXTURE_EXTENSION;
    		break;
    	case PORTRAIT:
    		resPath = GameConstants.GAMEACTOR_PORTRAIT_TEXTURE_DIR + resName + GameConstants.DEFAULT_TEXTURE_EXTENSION;
    		break;
    	case ANIMATION:
    		resPath = GameConstants.GAMEACTOR_ANIMATION_TEXTURE_DIR + resName + GameConstants.DEFAULT_TEXTURE_EXTENSION;
    		break;
    	default:
    		throw new Exception("not supported resource type");
    	}
    	
    	if(this.pool.containsKey(resPath)) return this.pool.get(resPath);
    	
    	Texture texture = new Texture(Gdx.files.internal(resPath));
    	this.pool.put(resPath, texture);
        
    	return texture;
    }
}
