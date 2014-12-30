package com.codebattle.model.event;

import com.codebattle.model.GameObject;
import com.codebattle.model.VirtualSystem;

public interface GameStageEventListener {
    public void onGameObjectDestroyed(GameObject obj);

    public void onVirtualSystemResourceChange(VirtualSystem sys);

    public void onGameObjectPositionChange(GameObject obj);
}
