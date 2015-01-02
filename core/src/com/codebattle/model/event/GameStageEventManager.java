package com.codebattle.model.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.VirtualMap;

public class GameStageEventManager implements GameStageEventListener {
    final public GameStage stage;

    private long id = 0;

    // Classify the events to a corresponding list
    private Map<String, List<GameEvent>> eventListMap;

    public GameStageEventManager(GameStage stage) {
        this.stage = stage;
        this.initEventsMap();
    }

    @Override
    public void onStageStart() {
        System.out.println("EventManager_onStageStart: ");
        this.handleEvent("onStageStart");
    }

    @Override
    public void onGameObjectDestroyed(GameObject obj) {
        System.out.println("EventManager_onGameObjectDestroyed: " + obj.getName());
        this.handleEvent("onGameObjectDestroyed");
    }

    @Override
    public void onVirtualMapUpdate(VirtualMap map) {
        System.out.println("EventManager_onVirualMapUpdate");
        this.handleEvent("onVirtualMapUpdate");
    }

    @Override
    public void onRoundComplete(GameStage stage) {
        System.out.println("EventManager_onRoundComplete");
        this.handleEvent("onRoundComplete");

    }

    public void addGameEvent(GameEvent e) {
        String type = e.getTrigger();
        if (this.eventListMap.containsKey(type)) {
            this.eventListMap.get(type)
                    .add(e);
            e.setID(id++);
        }
    }

    /**
     * Initialize the events routing table
     */
    public void initEventsMap() {
        this.eventListMap = new LinkedHashMap<String, List<GameEvent>>();
        this.eventListMap.put("onStageStart", new ArrayList<GameEvent>());
        this.eventListMap.put("onGameObjectDestroyed", new ArrayList<GameEvent>());
        this.eventListMap.put("onVirtualMapUpdate", new ArrayList<GameEvent>());
        this.eventListMap.put("onRoundComplete", new ArrayList<GameEvent>());
    }

    private void handleEvent(String type) {
        try {
            List<GameEvent> list = this.eventListMap.get(type);
            Iterator<GameEvent> it = list.iterator();
            while (it.hasNext()) {
                GameEvent e = it.next();
                if (e.validate()) {
                    e.execute();
                    if (e.getGameEventType() == GameEventType.ONCE)
                        it.remove();
                }
            }
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
    }

}
