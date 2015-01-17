package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.meta.GameObjectDescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XMLUtil {

    public static GameObjectDescription readGameActorDescFromFile(final String path)
            throws IOException, NoSuchMethodException, SecurityException {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element root = reader.parse(Gdx.files.internal(path));

        return new GameObjectDescription(root);
    }

    public static XmlReader.Element readXMLFromFile(final String path) throws IOException {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element root = reader.parse(Gdx.files.internal(path));

        return root;
    }

    public static Map<String, String> readAPIFromFile(final String path) throws IOException {
        final HashMap<String, String> map = new HashMap<String, String>();
        final XmlReader.Element context = readXMLFromFile(path);
        for (final XmlReader.Element methodElement : context.getChildrenByName("method")) {
            final String name = methodElement.getAttribute("name");
            final String doc = methodElement.getText();
            map.put(name, doc);
        }

        return map;
    }

    public static XmlReader.Element childByName(final XmlReader.Element root,
            final String name) {
        return root.getChildByName(name);
    }

    public static XmlReader.Element stringToElement(final String str) {
        final XmlReader reader = new XmlReader();
        return reader.parse(str);
    }
}
