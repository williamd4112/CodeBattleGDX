package com.codebattle.model.event;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.VirtualMap;

public interface GameStageEventListener {
    public void onStageStart();

    public void onGameObjectDestroyed(GameObject obj);

    public void onVirtualMapUpdate(VirtualMap map);

    public void onRoundComplete(GameStage stage);
}
