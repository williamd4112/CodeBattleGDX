package com.codebattle.model.meta;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameObjectProperties;

public class GameObjectType {
    public boolean through;
    public int cost;
    public GameObjectProperties prop;
    public Region region;
    private Attack attack;
    private Skill skill;

    private List<String> selectSoundList;

    public GameObjectType(XmlReader.Element type) throws NoSuchMethodException,
            SecurityException {
        // Read through ability
        this.through = Boolean.parseBoolean(type.getChildByName("through").getText());

        // Read cost
        this.cost = Integer.parseInt(type.getChildByName("cost").getText());

        // Read basic info
        this.prop = new GameObjectProperties(type);

        // Read selct_sound name
        this.selectSoundList = new LinkedList<String>();
        for (XmlReader.Element soundElement : type.getChildrenByName("sound")) {
            this.selectSoundList.add(soundElement.getText());
        }

        // Read actors movement animation region
        XmlReader.Element regionElement = type.getChildByName("region");
        this.region = new Region(regionElement);

        // Read actor attack
        XmlReader.Element attackElement = type.getChildByName("attack");
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

    public List<String> getSelectSoundNames() {
        return this.selectSoundList;
    }
}
