package com.codebattle.model.animation;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Animation;

public class SummonAnimation extends TargetBasedAnimation {

    public SummonAnimation(GameStage stage, Animation animMeta, GameObject target)
            throws Exception {
        super(stage, animMeta, target);

    }

    @Override
    public void finished() {
        // TODO Auto-generated method stub

    }

}
