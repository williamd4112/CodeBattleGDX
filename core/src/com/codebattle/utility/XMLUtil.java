package com.codebattle.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.structure.GameActorDescription;

public class XMLUtil {

    public static GameActorDescription readGameActorDescFromFile(final String path)
            throws IOException {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element root = reader.parse(Gdx.files.internal(path));

        return new GameActorDescription(root);
    }

    public static XmlReader.Element readXMLFromFile(final String path) throws IOException {
        final XmlReader reader = new XmlReader();
        final XmlReader.Element root = reader.parse(Gdx.files.internal(path));

        return root;
    }

    public static Map<String, String> readAPIFromFile(final String path) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        XmlReader.Element context = readXMLFromFile(path);
        for (XmlReader.Element methodElement : context.getChildrenByName("method")) {
            String name = methodElement.getAttribute("name");
            String doc = methodElement.getText();
            map.put(name, doc);
        }

        return map;
    }

    public static XmlReader.Element childByName(XmlReader.Element root, String name) {
        return root.getChildByName(name);
    }
}
