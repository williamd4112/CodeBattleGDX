package com.codebattle.model.event;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.MethodMeta;

/**
 * Read method name and arguments in scene file
 * arguments usage depends on method
 * @author williamd
 *
 */
public class Effect {
    private MethodMeta meta;

    public Effect(GameStage stage, XmlReader.Element effectElement) {
        XmlReader.Element methodElement = effectElement.getChildByName("method");
        this.meta = new MethodMeta(methodElement);
    }

    public void execute() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

    }
}
