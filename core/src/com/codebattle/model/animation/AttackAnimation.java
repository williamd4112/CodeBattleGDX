package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Attack;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.SoundUtil;

public class AttackAnimation extends TargetBasedAnimation {

    final public Attack attack;

    public AttackAnimation(GameStage stage, Attack attack, GameObject target) throws Exception {
        super(stage, attack.animMeta, target);
        this.attack = attack;

    }

    @Override
    public void update() {
        this.stage.setCameraTarget(this.target);
        if (this.duration % this.interval == 0)
            this.frame = AnimationUtil.frameOscillate(this.frame, 0, this.frames.length - 1);
        this.duration--;

    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {
        batch.draw(this.frames[frame], target.getX() + GameConstants.CELL_SIZE / 2 - frameWidth
                / 2, target.getY() + GameConstants.CELL_SIZE / 2 - frameHeight / 2);

    }

    @Override
    public void finished() {
        // if (!this.target.isAlive())
        // this.stage.removeGameObject(target);
    }

    @Override
    public void setup() {
        super.setup();
        SoundUtil.playSE(attack.soundName);
    }

}
