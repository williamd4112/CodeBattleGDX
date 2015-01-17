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

    public AttackAnimation(final GameStage stage, final Attack attack, final GameObject target)
            throws Exception {
        super(stage, attack.animMeta, target);
        this.attack = attack;

    }

    @Override
    public void update(final float delta) {
        this.stage.setCameraTarget(this.target);
        if (this.duration % this.interval == 0) {
            this.frame = AnimationUtil.frameOscillate(this.frame, 0, this.frames.length - 1);
        }
        this.duration--;

    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {
        batch.draw(this.frames[this.frame], this.target.getX() + GameConstants.CELL_SIZE / 2
                - this.frameWidth / 2, this.target.getY() + GameConstants.CELL_SIZE / 2
                - this.frameHeight
                / 2);

    }

    @Override
    public void finished() {
        // if (!this.target.isAlive())
        // this.stage.removeGameObject(target);
    }

    @Override
    public void setup() {
        super.setup();
        SoundUtil.playSES(this.attack.getSoundNames());
    }

}
