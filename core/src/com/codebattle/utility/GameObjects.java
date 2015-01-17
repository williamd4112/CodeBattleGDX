package com.codebattle.utility;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.PointLightMeta;

public class GameObjects {
    public static GameObject create(final GameStage stage,
            final XmlReader.Element gameobjectElement)
            throws Exception {
        final String clazz = gameobjectElement.getAttribute("class");
        final Owner owner = GameUtil.toOwner(gameobjectElement.getAttribute("owner"));
        final String name = gameobjectElement.getAttribute("name");
        final String type = gameobjectElement.getAttribute("type");
        final float x = Float.parseFloat(gameobjectElement.getAttribute("x"))
                * GameConstants.CELL_SIZE;
        final float y = Float.parseFloat(gameobjectElement.getAttribute("y"))
                * GameConstants.CELL_SIZE;

        PointLightMeta lightMeta = null;
        final XmlReader.Element pointlightElement =
                gameobjectElement.getChildByName("pointlight");
        if (pointlightElement != null) {
            lightMeta = new PointLightMeta(pointlightElement);
        }

        final String readonlyScript =
                gameobjectElement.getChildByName("script") == null ? null
                        : gameobjectElement.getChildByName("script").getText();
        final boolean isFixed = gameobjectElement.getChildByName("fixed") == null ? false
                : Boolean.parseBoolean(gameobjectElement.getChildByName("fixed").getText());

        if (clazz.equals("GameActor")) {
            return GameObjectFactory.getInstance().createGameActor(stage, owner, name, type,
                    x, y);
        } else if (clazz.equals("LevelObject")) {
            return GameObjectFactory.getInstance().createLevelObject(stage, name, type,
                    owner, lightMeta, x, y, readonlyScript, isFixed);
        } else {
            return null;
        }
    }
}
