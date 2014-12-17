package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameActorAnimation extends Animation {
    private final GameActor actor;
    private final TextureRegion[] frames;
    private final float destX;
    private final float destY;

    /**
     * Create a game actor animation.
     *
     * @param actor     Game actor
     * @param destX     Destination X
     * @param destY     Destination Y
     */
    public GameActorAnimation(final GameActor actor, final float destX, final float destY) {
        this.actor = actor;
        this.frames = actor.getCurrentDirectionFrames();
        this.destX = destX;
        this.destY = destY;
    }

    @Override
    public void update(final float delta) {

    }

    @Override
    public void render(final float delta) {

    }
}
