package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;

/**
 * Record the sprite chunk info
 */
public class Region
{
    public int x;
    public int y;
    public int width;
    public int height;
    public int hTile;
    public int vTile;

    public Region(final XmlReader.Element regionElement)
    {
        this.x = Integer.parseInt(regionElement.getAttribute("x"));
        this.y = Integer.parseInt(regionElement.getAttribute("y"));
        this.width = Integer.parseInt(regionElement.getAttribute("width"));
        this.height = Integer.parseInt(regionElement.getAttribute("height"));
        this.hTile = Integer.parseInt(regionElement.getAttribute("hTile"));
        this.vTile = Integer.parseInt(regionElement.getAttribute("vTile"));
    }

    public Region(final int x, final int y, final int width, final int height,
            final int hTile, final int vTile)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hTile = hTile;
        this.vTile = vTile;
    }

    @Override
    public String toString()
    {
        return String.format("(%d , %d , %d , %d)\n", this.x, this.y, this.width, this.height);
    }
}
