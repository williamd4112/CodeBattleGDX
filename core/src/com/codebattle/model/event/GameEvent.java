package com.codebattle.model.event;

import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class GameEvent {

    final public GameStage stage;

    // Optional field
    private long id;

    final private String name;
    final private String trigger;
    final private GameEventType type;
    final private List<GameMethod> conditions;
    final private List<GameMethod> effects;

    public GameEvent(final GameStage stage, final String name, final String trigger,
            final GameEventType type) {
        this.stage = stage;
        this.name = name;
        this.trigger = trigger;
        this.type = type;
        this.conditions = new LinkedList<GameMethod>();
        this.effects = new LinkedList<GameMethod>();
    }

    public boolean validate() {
        try {
            for (final GameMethod m : this.conditions) {
                if (!m.validate()) {
                    return false;
                }
            }
            return true;
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void execute() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        for (final GameMethod m : this.effects) {
            m.execute();
        }
    }

    public String getName() {
        return this.name;
    }

    public String getTrigger() {
        return this.trigger;
    }

    public GameEventType getGameEventType() {
        return this.type;
    }

    public long getID() {
        return this.id;
    }

    public void setID(final long id) {
        this.id = id;
    }

    public void addEffectMethod(final GameMethod m) {
        this.effects.add(m);
    }

    public void addConditionMethod(final GameMethod m) {
        this.conditions.add(m);
    }

}
