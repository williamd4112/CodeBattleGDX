package com.codebattle.model.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.units.Direction;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class PortraitAnimation extends BaseAnimation {

    final public GameStage stage;
    private GameActor actor;
    private Texture portrait;

    private Texture[] effects;
    private FrameTimer timer;

    private float x = 0, time = 0;

    public PortraitAnimation(GameStage stage, GameActor actor) {
        super();
        this.actor = actor;
        this.stage = stage;

    }

    @Override
    public void update(float delta) {
        this.stage.setCameraTarget(actor);

    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {
        this.x += Functions.exp6(time) * (camera.viewportWidth / 12);
        this.time += 0.05f;

        Vector3 screen = camera.unproject(new Vector3(x, camera.viewportHeight / 2
                + (this.portrait.getHeight() * 0.5f) / 2, 0));

        batch.draw(portrait, screen.x, screen.y, this.portrait.getWidth() * 0.5f,
                this.portrait.getHeight() * 0.5f);
        screen = camera.unproject(new Vector3(0, 0, 0));
        batch.draw(this.effects[this.timer.getFrame()], screen.x, screen.y,
                camera.viewportWidth, camera.viewportHeight);
    }

    @Override
    public boolean isFinished() {
        return (this.x >= Gdx.graphics.getWidth());
    }

    @Override
    public void finished() {
        this.actor.setDirection(Direction.HOLD_DEF);

    }

    @Override
    public void setup() {
        try {
            this.actor.setDirection(Direction.HOLD_ATK);
            this.portrait = TextureFactory.getInstance().loadTextureFromFile(
                    actor.source + "_attack", ResourceType.ANIMATION);
            this.effects = TextureFactory.getInstance().loadStripTextureFromFile(
                    GameConstants.SKILL_EFFECT_ANIM, ResourceType.ANIMATION,
                    GameConstants.SKILL_EFFECT_COUNT);
            this.timer = new FrameTimer(5, this.effects.length - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
