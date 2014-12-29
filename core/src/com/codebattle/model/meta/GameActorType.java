package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorProperties;

public class GameActorType {
    public GameActorProperties prop;
    public Region region;
    private Attack attack;
    private Skill skill;

    public GameActorType(XmlReader.Element type) {

        // Read basic info
        this.prop = new GameActorProperties(type);

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
}
