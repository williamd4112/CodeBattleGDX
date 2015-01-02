package com.codebattle.model.event;

import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameMethod;

public class Events {

    /**
     * Pass a xml event element, return a gameevent object
     * @param stage
     * @param eventElement
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static GameEvent create(GameStage stage, XmlReader.Element eventElement)
            throws NoSuchMethodException, SecurityException {
        String eventName = eventElement.getAttribute("name");
        String trigger = eventElement.getAttribute("trigger");
        String type = eventElement.getAttribute("type");

        GameEvent event = new GameEvent(stage, eventName, trigger, toGameEventType(type));

        // Bind a list of condition
        XmlReader.Element conditionElement = eventElement.getChildByName("condition");
        for (XmlReader.Element conditionMethodElement : conditionElement.getChildrenByName("method")) {
            GameMethod condition = new GameMethod(conditionMethodElement);
            condition.bind("Stage", stage);
            event.addConditionMethod(condition);
        }

        // Bind a list of effect
        XmlReader.Element effectElement = eventElement.getChildByName("effect");
        for (XmlReader.Element effectMethodElement : effectElement.getChildrenByName("method")) {
            GameMethod effect = new GameMethod(effectMethodElement);
            effect.bind("Stage", stage);
            event.addEffectMethod(effect);
        }

        return event;
    }

    public static GameEventType toGameEventType(String type) {
        if (type.equals("FOREVER"))
            return GameEventType.FOREVER;
        else if (type.equals("ONCE"))
            return GameEventType.ONCE;
        else
            return GameEventType.ONCE;
    }
}
