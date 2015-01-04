package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorProperties;

public class GameActorType {
    public GameActorProperties prop;
    public Region region;
    private Attack attack;
    private Skill skill;
    private String select_sound;

    public GameActorType(XmlReader.Element type) throws NoSuchMethodException, SecurityException {

        // Read basic info
        this.prop = new GameActorProperties(type);

        // Read selct_sound name
        this.select_sound = type.getChildByName("sound")
                .getText();

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

    public String getSelectSoundName() {
        return this.select_sound;
    }
}
