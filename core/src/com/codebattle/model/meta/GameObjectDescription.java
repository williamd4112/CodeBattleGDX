package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.model.Readable;

import java.util.HashMap;
import java.util.Map;

/**
 * Store actor's data including
 * GameActorDescription is each GameActorProperties' base
 * (but only copy some info , excluding source image meta data)
 * @source: source sprite(.png file path
 * @hp: health point
 * @mp: magic power
 * @atk: attack point
 * @def: def point
 * @range: attack range
 * @maxsteps: max movement steps
 * @author williamd
 *
 */
public class GameObjectDescription implements Readable {
    /**
     * Source Image
     */
    public String source;

    /**
     * Map<GameActorName , Map<TypeName , GameActorType>>
     */
    final public Map<String, GameObjectType> types;

    public GameObjectDescription(final Element context) throws NoSuchMethodException,
            SecurityException {
        this();
        this.read(context);
    }

    public GameObjectDescription() {
        this.types = new HashMap<String, GameObjectType>();
    }

    @Override
    public void read(final Element context) throws NoSuchMethodException, SecurityException {
        this.source = context.getChildByName("source")
                .getText();
        for (final XmlReader.Element type : context.getChildrenByNameRecursively("type")) {
            final GameObjectType gameActorType = new GameObjectType(type);
            this.types.put(gameActorType.prop.typeName, gameActorType);
        }
    }

    @Override
    public String toString() {
        String basic = "";
        for (final String key : this.types.keySet()) {
            final GameObjectType type = this.types.get(key);
            basic += String.format(
                    "Type: %s\nHp: %d\nMp: %d\nAtk: %d\nDef: %d\nRange: %d\nMaxsteps: %d\n",
                    key, type.prop.hp, type.prop.mp, type.prop.atk, type.prop.def,
                    type.prop.range, type.prop.maxsteps);
            basic += type.region.toString();
        }

        return basic;
    }
}
