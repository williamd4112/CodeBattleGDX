package com.codebattle.model.structure;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.XMLUtil;

public class Skill {
    public Animation animMeta;
    public String soundName;
    private int range;

    public Skill(XmlReader.Element skillElement) {
        this.animMeta = new Animation(XMLUtil.childByName(skillElement, "animation"));
        this.soundName = XMLUtil.childByName(skillElement, "sound")
                .getText();
        this.range = Integer.parseInt(skillElement.getAttribute("range"));
    }

    public int getRange() {
        return this.range;
    }
}
