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
    public static GameEvent
            create(final GameStage stage, final XmlReader.Element eventElement)
                    throws NoSuchMethodException, SecurityException {
        final String eventName = eventElement.getAttribute("name");
        final String trigger = eventElement.getAttribute("trigger");
        final String type = eventElement.getAttribute("type");

        final GameEvent event =
                new GameEvent(stage, eventName, trigger, toGameEventType(type));

        // Bind a list of condition
        final XmlReader.Element conditionElement = eventElement.getChildByName("condition");
        for (final XmlReader.Element conditionMethodElement : conditionElement.getChildrenByName("method")) {
            final GameMethod condition = new GameMethod(conditionMethodElement);
            condition.bind("Stage", stage);
            event.addConditionMethod(condition);
        }

        // Bind a list of effect
        final XmlReader.Element effectElement = eventElement.getChildByName("effect");
        for (final XmlReader.Element effectMethodElement : effectElement.getChildrenByName("method")) {
            final GameMethod effect = new GameMethod(effectMethodElement);
            effect.bind("Stage", stage);
            event.addEffectMethod(effect);
        }

        return event;
    }

    public static GameEventType toGameEventType(final String type) {
        if (type.equals("FOREVER")) {
            return GameEventType.FOREVER;
        } else if (type.equals("ONCE")) {
            return GameEventType.ONCE;
        } else {
            return GameEventType.ONCE;
        }
    }
}
