package com.codebattle.utility;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.levelobject.GlowObject;

public class GameObjects {
    public static GameObject create(GameStage stage, XmlReader.Element gameobjectElement)
            throws Exception {
        final String clazz = gameobjectElement.getAttribute("class");
        final Owner owner = GameUtil.toOwner(gameobjectElement.getAttribute("owner"));
        final String name = gameobjectElement.getAttribute("name");
        final String type = gameobjectElement.getAttribute("type");
        final float x = Float.parseFloat(gameobjectElement.getAttribute("x"))
                * GameConstants.CELL_SIZE;
        final float y = Float.parseFloat(gameobjectElement.getAttribute("y"))
                * GameConstants.CELL_SIZE;

        if (clazz.equals("GameActor")) {
            return GameActorFactory.getInstance()
                    .createGameActor(stage, owner, name, type, x, y);
        } else if (clazz.equals("GlowObject")) {
            return new GlowObject(stage, XMLUtil.readXMLFromFile(GameConstants.LEVEL_PROP_DIR
                    + name + ".xml"), x, y);
        } else {
            return null;
        }
    }
}
