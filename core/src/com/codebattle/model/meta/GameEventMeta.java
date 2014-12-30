package com.codebattle.model.meta;

import com.badlogic.gdx.utils.XmlReader;

public class GameEventMeta {
    public String name;
    public Bundle args;

    public GameEventMeta(XmlReader.Element eventElement) {
        this.name = eventElement.getAttribute("name");
        for (XmlReader.Element argElement : eventElement.getChildrenByName("arg")) {
            String key = argElement.getAttribute("key");
            String value = argElement.getText();
            args.bind(key, value);
        }
    }
}
