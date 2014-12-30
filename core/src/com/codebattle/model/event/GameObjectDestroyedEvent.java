package com.codebattle.model.event;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameEventMeta;

public class GameObjectDestroyedEvent extends GameEvent {

    private GameObject target;

    public GameObjectDestroyedEvent(GameStage stage, GameEventMeta meta, long id) {
        super(id, meta.name);
    }

    @Override
    public boolean check() {
        return (target.isAlive()) ? false : true;
    }

    @Override
    public void execute() {
        System.out.println("GameObjectDestroyed Event triggered");

    }

}
