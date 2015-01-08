package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Animation;
import com.codebattle.utility.GameConstants;

public class TransferAnimation extends TargetBasedAnimation {
    private int destX, destY;

    public TransferAnimation(GameStage stage, Animation animMeta, GameObject target, int x,
            int y) throws Exception {
        super(stage, animMeta, target);
        this.destX = x * GameConstants.CELL_SIZE;
        this.destY = y * GameConstants.CELL_SIZE;
        this.setScale(0.3f);
    }

    @Override
    public void setup() {
        super.setup();
        this.target.setPosition(destX, destY);
    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {
        super.draw(batch, camera, delta);
        batch.draw(this.frames[frame], destX + GameConstants.CELL_SIZE / 2 - frameWidth
                * scale / 2, destY + GameConstants.CELL_SIZE / 2 - frameHeight * scale / 2,
                frameWidth * scale, frameHeight * scale);
    }

    @Override
    public void finished() {

    }

}
