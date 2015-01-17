package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameObjectProperties;

import java.util.LinkedList;
import java.util.List;

public class GameObjectType {
    private final String name;
    private final int level;
    public boolean through;
    public int cost;
    public GameObjectProperties prop;
    public Region region;
    private final Attack attack;
    private final Skill skill;

    private final List<String> selectSoundList;

    public GameObjectType(final XmlReader.Element type) throws NoSuchMethodException,
            SecurityException {
        // Read type name
        this.name = type.getAttribute("name");

        // Read Level
        this.level = Integer.parseInt(type.getAttribute("level"));

        // Read through ability
        this.through = Boolean.parseBoolean(type.getChildByName("through").getText());

        // Read cost
        this.cost = Integer.parseInt(type.getChildByName("cost").getText());

        // Read basic info
        this.prop = new GameObjectProperties(type);

        // Read selct_sound name
        this.selectSoundList = new LinkedList<String>();
        for (final XmlReader.Element soundElement : type.getChildrenByName("sound")) {
            this.selectSoundList.add(soundElement.getText());
        }

        // Read actors movement animation region
        final XmlReader.Element regionElement = type.getChildByName("region");
        this.region = new Region(regionElement);

        // Read actor attack
        final XmlReader.Element attackElement = type.getChildByName("attack");
        this.attack = new Attack(attackElement);

        // Read actor skill
        this.skill = new Skill(type.getChildByName("skill"));
    }

    public Attack getAttackMode() {
        return this.attack;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public int getLevel() {
        return this.level;
    }

    public String getTypeName() {
        return this.name;
    }

    public List<String> getSelectSoundNames() {
        return this.selectSoundList;
    }
}
