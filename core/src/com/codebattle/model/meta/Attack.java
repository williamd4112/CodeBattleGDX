package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;

public class Attack {
    public Animation animMeta;
    public String soundName;

    private int atk;

    public Attack(XmlReader.Element attackElement) {
        XmlReader.Element animElement = attackElement.getChildByName("animation");
        this.animMeta = new Animation(animElement);

        XmlReader.Element soundElement = attackElement.getChildByName("sound");
        this.soundName = soundElement.getText();

        XmlReader.Element atkElement = attackElement.getChildByName("atk");
        this.atk = Integer.parseInt(atkElement.getText());
    }

    public int getATK() {
        return this.atk;
    }
}