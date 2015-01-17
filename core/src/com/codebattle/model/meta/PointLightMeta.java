package com.codebattle.model.meta;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.GameConstants;

public class PointLightMeta {

    public Color color;
    public int x;
    public int y;
    public int radius;

    public PointLightMeta(final XmlReader.Element lightElement) {
        this.color = Color.valueOf(lightElement.getAttribute("color"));
        this.radius = Integer.parseInt(lightElement.getAttribute("radius"));
        this.x = Integer.parseInt(lightElement.getAttribute("x")) * GameConstants.CELL_SIZE
                + GameConstants.CELL_SIZE / 2;
        this.y = Integer.parseInt(lightElement.getAttribute("y")) * GameConstants.CELL_SIZE
                + GameConstants.CELL_SIZE / 2;
    }
}
