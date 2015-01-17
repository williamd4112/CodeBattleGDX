package com.codebattle.model;

import com.badlogic.gdx.utils.XmlReader;

public interface Readable {
    public void read(XmlReader.Element context) throws NoSuchMethodException,
            SecurityException;
}
