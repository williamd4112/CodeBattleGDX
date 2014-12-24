package com.codebattle.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorDescription;

import java.io.IOException;

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
}
