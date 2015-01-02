package com.codebattle.model.event;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameMethod;

public class GameEvent {

    final public GameStage stage;

    // Optional field
    private long id;

    final private String name;
    final private String trigger;
    final private GameEventType type;
    final private List<GameMethod> conditions;
    final private List<GameMethod> effects;

    public GameEvent(GameStage stage, String name, String trigger, GameEventType type) {
        this.stage = stage;
        this.name = name;
        this.trigger = trigger;
        this.type = type;
        this.conditions = new LinkedList<GameMethod>();
        this.effects = new LinkedList<GameMethod>();
    }

    public boolean validate() {
        try {
            for (GameMethod m : conditions)
                if (!m.validate())
                    return false;
            return true;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void execute() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        for (GameMethod m : this.effects)
            m.execute();
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

    public void setID(long id) {
        this.id = id;
    }

    public void addEffectMethod(GameMethod m) {
        this.effects.add(m);
    }

    public void addConditionMethod(GameMethod m) {
        this.conditions.add(m);
    }

}
