package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Animation;
import com.codebattle.utility.GameConstants;

public class TransferAnimation extends TargetBasedAnimation {
    private final int destX, destY;

    public TransferAnimation(final GameStage stage, final Animation animMeta,
            final GameObject target, final int x,
            final int y) throws Exception {
        super(stage, animMeta, target);
        this.destX = x * GameConstants.CELL_SIZE;
        this.destY = y * GameConstants.CELL_SIZE;
        this.setScale(0.3f);
    }

    @Override
    public void setup() {
        super.setup();
        this.target.setPosition(this.destX, this.destY);
    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {
        super.draw(batch, camera, delta);
        batch.draw(this.frames[this.frame], this.destX + GameConstants.CELL_SIZE / 2
                - this.frameWidth
                * this.scale / 2, this.destY + GameConstants.CELL_SIZE / 2 - this.frameHeight
                * this.scale / 2,
                this.frameWidth * this.scale, this.frameHeight * this.scale);
    }

    @Override
    public void finished() {

    }

}
