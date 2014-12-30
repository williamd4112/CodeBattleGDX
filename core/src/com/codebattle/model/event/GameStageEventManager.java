package com.codebattle.model.event;

import java.util.LinkedHashMap;
import java.util.Map;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.VirtualSystem;

public class GameStageEventManager implements GameStageEventListener {
    final public GameStage stage;

    private long id = 0;
    private Map<String, GameEvent> eventQueue;

    public GameStageEventManager(GameStage stage) {
        this.stage = stage;
        this.eventQueue = new LinkedHashMap<String, GameEvent>();
    }

    @Override
    public void onGameObjectDestroyed(GameObject obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVirtualSystemResourceChange(VirtualSystem sys) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameObjectPositionChange(GameObject obj) {
        // TODO Auto-generated method stub

    }

    public void addEvent(GameEvent e) {

    }
}
