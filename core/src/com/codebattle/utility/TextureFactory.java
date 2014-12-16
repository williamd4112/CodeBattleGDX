package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureFactory {
    private static TextureFactory instance = null;

    private TextureFactory() {
    }

    public static TextureFactory getInstance() {
        if (null == instance) {
            instance = new TextureFactory();
        }

        return instance;
    }

    public TextureRegion[][] loadTextureRegionFromFile(final String resourcePath,
            final int horizontal, final int vertical, final int hTileSize, final int vTileSize) {
        final TextureRegion[][] regions = new TextureRegion[vertical][horizontal];
        final Texture texture = this.loadTextureFromFile(resourcePath);

        for (int v = 0; v < vertical; v++) {
            for (int h = 0; h < horizontal; h++) {
                regions[v][h] = new TextureRegion(texture, h * hTileSize, v * vTileSize,
                        hTileSize, vTileSize);
            }
        }

        return regions;
    }

    public Texture loadTextureFromFile(final String resourcePath) {
        return new Texture(Gdx.files.internal("graphics/characters/" + resourcePath + ".png"));
    }
}
