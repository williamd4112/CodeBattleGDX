package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.Animation;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.TextureFactory;

abstract public class TargetBasedAnimation extends BaseAnimation {

    final public GameStage stage;
    final public GameObject target;

    // Frame variable
    protected Animation animMeta;
    protected TextureRegion[] frames;
    protected int repeat;
    protected int duration;
    protected int interval;
    protected float x = 0;
    protected int frame = 0;
    protected int frameWidth;
    protected int frameHeight;

    public TargetBasedAnimation(GameStage stage, Animation animMeta, GameObject target)
            throws Exception {
        super();
        this.stage = stage;
        this.animMeta = animMeta;
        this.target = target;
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
    public boolean isFinished() {
        return (this.duration <= 0);
    }

    @Override
    public void setup() {
        try {
            this.frames = TextureFactory.getInstance()
                    .loadAnimationFramesFromFile(animMeta.source, animMeta.region);
            this.repeat = animMeta.repeat;
            this.interval = animMeta.interval;
            this.duration = this.repeat * frames.length * this.interval;
            this.frameWidth = this.frames[0].getRegionWidth();
            this.frameHeight = this.frames[0].getRegionHeight();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
