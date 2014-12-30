package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;
import com.codebattle.utility.GameConstants;

public class MovementAnimation extends BaseAnimation {

    final public GameStage stage;

    private final MoveableGameObject obj;
    private final Speed speed;
    private final float dx, dy;
    private float pixelDiff;

    /**
     * Binding a actor and direction, perform a same direction movement (e.g. 5 steps to upward)
     * @param textureRegions
     * @param direction
     * @param steps
     */
    public MovementAnimation(GameStage stage, MoveableGameObject actor, Direction direction,
            int steps) {
        this.stage = stage;
        this.obj = actor;
        this.speed = actor.getSpeed();
        this.dx = direction.udx * this.speed.value;
        this.dy = direction.udy * this.speed.value;
        this.pixelDiff = steps * GameConstants.CELL_SIZE;
    }

    @Override
    public void setup() {

    }

    @Override
    public void update() {
        this.stage.setCameraTarget(this.obj);
        if (this.pixelDiff > 0) {
            this.obj.moveBy(this.dx, this.dy);
            this.pixelDiff -= this.speed.value;
        }
    }

    @Override
    public boolean isFinished() {
        return (this.pixelDiff <= 0) ? true : false;
    }

    @Override
    public String toString() {
        return String.format("GameMovment(%s , %s)", this.obj.getName(), this.pixelDiff);
    }

    @Override
    public void finished() {
        this.obj.setDirection(Direction.HOLD_DEF);
    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {

    }

}
