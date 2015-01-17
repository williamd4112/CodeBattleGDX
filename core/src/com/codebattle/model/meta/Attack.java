package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.Owner;

import java.util.LinkedList;
import java.util.List;

/**
 * Consist of Animation, Sound List, ATK, Range, Type(Resistance)
 * @author williamd
 *
 */
public class Attack {
    public Animation animMeta;
    public List<String> soundName;

    private final int atk;
    private Owner owner;

    public Attack(final XmlReader.Element attackElement) {
        final XmlReader.Element animElement = attackElement.getChildByName("animation");
        this.animMeta = new Animation(animElement);

        this.soundName = new LinkedList<String>();
        for (final XmlReader.Element soundElement : attackElement.getChildrenByName("sound")) {
            this.soundName.add(soundElement.getText());
        }

        final XmlReader.Element atkElement = attackElement.getChildByName("atk");
        this.atk = Integer.parseInt(atkElement.getText());
    }

    public int getATK() {
        return this.atk;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public List<String> getSoundNames() {
        return this.soundName;
    }
}
