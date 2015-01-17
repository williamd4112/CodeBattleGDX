package com.codebattle.model.animation;

import box2dLight.PointLight;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.utility.GameConstants;

public class SummonAnimation extends TargetBasedAnimation {

    private final PointLight light;

    public SummonAnimation(final GameStage stage, final GameObject target) throws Exception {
        super(stage, GameConstants.SUMMON_ANIMMETA, target);
        final PointLightMeta lightMeta = GameConstants.SUMMON_LIGHTMETA;
        lightMeta.x = (int) target.getX() + GameConstants.CELL_SIZE / 2;
        lightMeta.y = (int) target.getY() + GameConstants.CELL_SIZE / 2;
        this.light = this.stage.addPointLight(lightMeta);
        this.setScale(0.3f);
    }

    @Override
    public void finished() {
        this.light.remove();
    }

    public void removeLight() {
        this.light.remove();
    }
}
