package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.codebattle.model.meta.Region;

import java.util.HashMap;
import java.util.Map;

public class TextureFactory {
    private static TextureFactory instance = null;

    private final Map<String, Texture> pool;

    private TextureFactory() {
        this.pool = new HashMap<String, Texture>();
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
    public TextureRegion[][] loadCharacterFramesFromFile(final String resName, final int x,
            final int y, final int horizontal, final int vertical, final int hTileSize,
            final int vTileSize) throws Exception {

        TextureRegion[][] regions = null;
        try {
            regions = new TextureRegion[vertical][horizontal];
            final Texture texture = this.loadTextureFromFile(resName, ResourceType.CHARACTER);

            for (int v = 0; v < vertical; v++) {
                for (int h = 0; h < horizontal; h++) {
                    regions[v][h] = new TextureRegion(texture, x + h * hTileSize, y + v
                            * vTileSize, hTileSize, vTileSize);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return regions;
    }

    public TextureRegion[][] loadCharacterFramesFromFile(final String resName,
            final Region region) throws Exception {
        final int x = region.x;
        final int y = region.y;
        final int horizontal = region.width / region.hTile;
        final int vertical = region.height / region.vTile;
        final int hTileSize = region.hTile;
        final int vTileSize = region.vTile;

        return this.loadCharacterFramesFromFile(resName, x, y, horizontal, vertical,
                hTileSize,
                vTileSize);
    }

    public TextureRegion[] loadAnimationFramesFromFile(final String resName,
            final Region region)
            throws Exception {
        final int x = region.x;
        final int y = region.y;
        final int horizontal = region.width / region.hTile;
        final int vertical = region.height / region.vTile;
        final int hTileSize = region.hTile;
        final int vTileSize = region.vTile;

        TextureRegion[] regions;
        int index = 0;
        regions = new TextureRegion[horizontal * vertical];
        final Texture texture = this.loadTextureFromFile(resName, ResourceType.ANIMATION);
        for (int h = 0; h < horizontal; h++) {
            for (int v = 0; v < vertical; v++) {
                regions[index++] = new TextureRegion(texture, x + h * hTileSize, y + v
                        * vTileSize, hTileSize, vTileSize);
            }
        }

        return regions;
    }

    public TextureRegion[]
            loadSelectCursorFromFile(final String resName, final Region region)
                    throws Exception {
        final int x = region.x;
        final int y = region.y;
        final int horizontal = region.width / region.hTile;
        final int vertical = region.height / region.vTile;
        final int hTileSize = region.hTile;
        final int vTileSize = region.vTile;

        int index = 0;
        final TextureRegion[] regions = new TextureRegion[horizontal * 2];
        final Texture texture = this.loadTextureFromFile(resName, ResourceType.CURSOR);
        for (int v = 0; v < vertical; v++) {
            for (int h = 0; h < horizontal; h++) {
                regions[index++] = new TextureRegion(texture, x + h * hTileSize, y + v
                        * vTileSize, hTileSize, vTileSize);
            }
        }

        return regions;
    }

    public TextureRegion[] loadFrameRow(final String resName, final Region region,
            final ResourceType type)
            throws Exception {
        final Texture texture = this.loadTextureFromFile(resName, type);
        final int x = region.x;
        final int y = region.y;
        final int horizontal = region.width / region.hTile;
        final int vertical = region.height / region.vTile;
        final int hTileSize = region.hTile;
        final int vTileSize = region.vTile;

        final TextureRegion[] frames = new TextureRegion[horizontal];
        for (int i = 0; i < horizontal; i++) {
            frames[i] =
                    new TextureRegion(texture, x + i * hTileSize, y, hTileSize, vTileSize);
        }

        return frames;
    }

    public Drawable loadDrawable(final String resName, final ResourceType type)
            throws Exception {
        final Texture tex = this.loadTextureFromFile(resName, type);
        final Drawable drawable = new TextureRegionDrawable(new TextureRegion(tex));
        return drawable;
    }

    public Texture[] loadStripTextureFromFile(final String resPrefix,
            final ResourceType type, final int count)
            throws Exception {
        final Texture[] regions = new Texture[count];
        for (int id = 0; id < count; id++) {
            regions[id] = this.loadTextureFromFile(resPrefix + String.valueOf(id), type);
        }

        return regions;
    }

    public Texture loadTextureFromFile(final String resName, final ResourceType type)
            throws Exception {
        String resPath = "";
        switch (type) {
        case CHARACTER:
            resPath = GameConstants.GAMEACTOR_MAP_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case PORTRAIT:
            resPath = GameConstants.GAMEACTOR_PORTRAIT_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case ANIMATION:
            resPath = GameConstants.GAMEACTOR_ANIMATION_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case IMAGE:
            resPath = GameConstants.IMAGE_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case CURSOR:
            resPath = GameConstants.GUI_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case LEVELOBJECT:
            resPath = GameConstants.LEVELOBJECT_TEXTURE_DIR + resName
                    + GameConstants.DEFAULT_TEXTURE_EXTENSION;
            break;
        case PICTURE:
            resPath = GameConstants.IMAGE_TEXTURE_DIR + resName;
            break;
        default:
            throw new Exception("not supported resource type");
        }

        if (this.pool.containsKey(resPath)) {
            return this.pool.get(resPath);
        }

        final Texture texture = new Texture(Gdx.files.internal(resPath));
        this.pool.put(resPath, texture);

        return texture;
    }
}
