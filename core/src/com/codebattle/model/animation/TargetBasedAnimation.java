package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.VirtualCell;
import com.codebattle.model.meta.Animation;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.SoundUtil;
import com.codebattle.utility.TextureFactory;

public class TargetBasedAnimation extends BaseAnimation {

    final public GameStage stage;
    final public GameObject target;

    private VirtualCell cell;

    // Frame variable
    protected float scale = 1.0f;
    protected Animation animMeta;
    protected TextureRegion[] frames;
    protected int repeat;
    protected int duration;
    protected int interval;
    protected int frame = 0;
    protected int frameWidth;
    protected int frameHeight;

    // Optional
    protected MoveableGameObject emitter = null;

    public TargetBasedAnimation(GameStage stage, Animation animMeta, GameObject target)
            throws Exception {
        super();
        this.stage = stage;
        this.animMeta = animMeta;
        this.target = target;
    }

    public TargetBasedAnimation(GameStage stage, Animation animMeta, GameObject target,
            float scale) throws Exception {
        super();
        this.stage = stage;
        this.animMeta = animMeta;
        this.target = target;
        this.scale = scale;
    }

    public TargetBasedAnimation(GameStage stage, Animation animMeta, VirtualCell cell,
            float scale) throws Exception {
        super();
        this.stage = stage;
        this.animMeta = animMeta;
        this.cell = cell;
        this.target = null;
        this.scale = scale;
    }

    @Override
    public void update(float delta) {
        if (this.target != null)
            this.stage.setCameraTarget(target);
        else
            this.stage.setCameraTarget(cell);
        if (this.duration % this.interval == 0)
            this.frame = AnimationUtil.frameOscillate(this.frame, 0, this.frames.length - 1);
        this.duration--;

    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {
        float x = (target == null) ? cell.getX() : target.getX();
        float y = (target == null) ? cell.getY() : target.getY();
        batch.draw(this.frames[frame], x + GameConstants.CELL_SIZE / 2 - frameWidth * scale
                / 2, y + GameConstants.CELL_SIZE / 2 - frameHeight * scale / 2, frameWidth
                * scale, frameHeight * scale);

    }

    @Override
    public boolean isFinished() {
        return (this.duration <= 0);
    }

    @Override
    public void setup() {
        try {
            this.frames = TextureFactory.getInstance().loadAnimationFramesFromFile(
                    animMeta.source, animMeta.region);
            this.repeat = animMeta.repeat;
            this.interval = animMeta.interval;
            this.duration = this.repeat * frames.length * this.interval;
            this.frameWidth = this.frames[0].getRegionWidth();
            this.frameHeight = this.frames[0].getRegionHeight();
            SoundUtil.playSES(sounds);
            if (this.emitter != null)
                emitter.setDirection(Direction.HOLD_ATK);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setEmitter(MoveableGameObject emitter) {
        this.emitter = emitter;
    }

    public void addSound(String sound) {
        this.sounds.add(sound);
    }

    @Override
    public void finished() {
        if (this.emitter != null)
            emitter.setDirection(Direction.HOLD_DEF);
    }
}
