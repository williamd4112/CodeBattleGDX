package com.codebattle.model.gameactor;

import com.badlogic.gdx.utils.XmlReader;

public class GameObjectProperties {
    final public String typeName;
    public int hp;
    public int mp;
    public int atk;
    public int def;
    public int range;
    public int minrange;
    public int maxsteps;
    public int maxoperation;

    public GameObjectProperties(final String type, final int hp, final int mp, final int atk,
            final int def, final int range,
            final int minrange, final int maxsteps, final int operation) {
        this.typeName = type;
        this.hp = hp;
        this.mp = mp;
        this.atk = atk;
        this.def = def;
        this.range = range;
        this.minrange = minrange;
        this.maxsteps = maxsteps;
        this.maxoperation = operation;
    }

    public GameObjectProperties(final XmlReader.Element type) {
        this.typeName = type.getAttribute("name");
        this.hp = Integer.parseInt(type.getChildByName("hp").getText());
        this.mp = Integer.parseInt(type.getChildByName("mp").getText());
        this.atk = Integer.parseInt(type.getChildByName("atk").getText());
        this.def = Integer.parseInt(type.getChildByName("def").getText());
        this.range = Integer.parseInt(type.getChildByName("range").getText());
        this.minrange = Integer.parseInt(type.getChildByName("minrange").getText());
        this.maxsteps = Integer.parseInt(type.getChildByName("maxsteps").getText());
        this.maxoperation = Integer.parseInt(type.getChildByName("maxoperation").getText());

    }

    public GameObjectProperties(final GameObjectProperties prop) {
        this(prop.typeName, prop.hp, prop.mp, prop.atk, prop.def, prop.range, prop.minrange,
                prop.maxsteps, prop.maxoperation);
    }

    public String[] getPropertyArray() {
        return new String[] { String.valueOf(this.hp), String.valueOf(this.mp),
                String.valueOf(this.atk),
                String.valueOf(this.def), String.valueOf(this.range) };
    }
}
