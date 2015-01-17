package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;
import com.codebattle.utility.GameConstants;

public class MovementAnimation extends BaseAnimation {

    final public GameStage stage;

    private final GameObject obj;
    private Speed speed;
    private final float dx, dy;
    private float pixelDiff;

    /**
     * Binding a actor and direction, perform a same direction movement (e.g. 5 steps to upward)
     * @param textureRegions
     * @param direction
     * @param steps
     */
    public MovementAnimation(final GameStage stage, final MoveableGameObject actor,
            final Direction direction,
            final int steps) {
        this.stage = stage;
        this.obj = actor;
        this.speed = actor.getSpeed();
        this.dx = direction.udx * this.speed.value;
        this.dy = direction.udy * this.speed.value;
        this.pixelDiff = steps * GameConstants.CELL_SIZE;
    }

    public MovementAnimation(final GameStage stage, final GameObject obj,
            final Direction direction, final int steps,
            final Speed speed) {
        this.stage = stage;
        this.obj = obj;
        this.speed = speed;
        this.dx = direction.udx * this.speed.value;
        this.dy = direction.udy * this.speed.value;
        this.pixelDiff = steps * GameConstants.CELL_SIZE;
    }

    @Override
    public void setup() {

    }

    @Override
    public void update(final float delta) {
        this.stage.setCameraTarget(this.obj);
        if (this.pixelDiff > 0) {
            this.obj.moveBy(this.dx, this.dy);
            this.pixelDiff -= this.speed.value;
        }
    }

    @Override
    public boolean isFinished() {
        return this.pixelDiff <= 0 ? true : false;
    }

    @Override
    public String toString() {
        return String.format("GameMovment(%s , %s)", this.obj.getName(), this.pixelDiff);
    }

    @Override
    public void finished() {
        if (this.obj instanceof MoveableGameObject) {
            ((MoveableGameObject) this.obj).setDirection(Direction.HOLD_DEF);
        }
    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {

    }

    public void setSpeed(final Speed speed) {
        this.speed = speed;
    }

}
