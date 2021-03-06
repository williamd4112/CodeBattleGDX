package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;

public class Animation {
    public String type;
    public String source;
    public int repeat;
    public int interval;
    public Region region;

    public Animation(final XmlReader.Element animationElement) {
        this.type = animationElement.getAttribute("class");
        this.source = animationElement.getAttribute("source");
        this.repeat = Integer.parseInt(animationElement.getAttribute("duration"));
        this.interval = Integer.parseInt(animationElement.getAttribute("interval"));

        final XmlReader.Element regionElement = animationElement.getChildByName("region");
        this.region = new Region(regionElement);
    }
}
