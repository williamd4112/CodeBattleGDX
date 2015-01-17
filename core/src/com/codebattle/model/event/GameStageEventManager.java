package com.codebattle.model.event;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.VirtualMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameStageEventManager implements GameStageEventListener {
    final public GameStage stage;

    private long id = 0;

    // Classify the events to a corresponding list
    private Map<String, List<GameEvent>> eventListMap;

    public GameStageEventManager(final GameStage stage) {
        this.stage = stage;
        this.initEventsMap();
    }

    @Override
    public void onStageStart() {
        System.out.println("EventManager_onStageStart: ");
        this.handleEvent("onStageStart");
    }

    @Override
    public void onGameObjectDestroyed(final GameObject obj) {
        System.out.println("EventManager_onGameObjectDestroyed: " + obj.getName());
        this.handleEvent("onGameObjectDestroyed");
    }

    @Override
    public void onVirtualMapUpdate(final VirtualMap map) {
        System.out.println("EventManager_onVirualMapUpdate");
        this.handleEvent("onVirtualMapUpdate");
    }

    @Override
    public void onRoundComplete(final GameStage stage) {
        System.out.println("EventManager_onRoundComplete");
        this.handleEvent("onRoundComplete");

    }

    public void addGameEvent(final GameEvent e) {
        final String type = e.getTrigger();
        if (this.eventListMap.containsKey(type)) {
            this.eventListMap.get(type)
                    .add(e);
            e.setID(this.id++);
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

    private void handleEvent(final String type) {
        try {
            final List<GameEvent> list = this.eventListMap.get(type);
            final Iterator<GameEvent> it = list.iterator();
            while (it.hasNext()) {
                final GameEvent e = it.next();
                if (e.validate()) {
                    e.execute();
                    if (e.getGameEventType() == GameEventType.ONCE) {
                        it.remove();
                    }
                }
            }
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
    }

}
