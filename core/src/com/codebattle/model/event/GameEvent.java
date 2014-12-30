package com.codebattle.model.event;

abstract public class GameEvent {

    final private long id;
    final private String name;

    public GameEvent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    abstract public boolean check();

    abstract public void execute();
}
