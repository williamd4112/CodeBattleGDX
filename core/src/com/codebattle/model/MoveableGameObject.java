package com.codebattle.model;

import com.codebattle.model.animation.MovementAnimation;
import com.codebattle.model.animation.TurnAnimation;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;

abstract public class MoveableGameObject extends GameObject {

    protected Direction direction;
    protected Speed speed;

    protected int culmuSteps = 0;

    final protected int maxsteps;

    public MoveableGameObject(final GameStage stage, final Owner owner, final String source,
            final String name,
            final int id, final GameObjectType type, final float sx, final float sy,
            final int maxsteps) {
        super(stage, owner, source, name, id, type, sx, sy);
        this.direction = Direction.HOLD_DEF;
        this.speed = Speed.VERYFAST;
        this.maxsteps = maxsteps;
    }

    /**
     * User interface
     */
    public void moveDown(final int pace) {
        this.turn(Direction.DOWN);
        this.move(Direction.DOWN, pace);
    }

    public void moveLeft(final int pace) {
        this.turn(Direction.LEFT);
        ;
        this.move(Direction.LEFT, pace);
    }

    public void moveRight(final int pace) {
        this.turn(Direction.RIGHT);
        this.move(Direction.RIGHT, pace);
    }

    public void moveUp(final int pace) {
        this.turn(Direction.UP);
        this.move(Direction.UP, pace);
    }

    public void move(final Direction direction, final int pace) {
        // Check steps
        if (this.culmuSteps >= this.maxsteps) {
            return;
        }
        int checkedPace = this.pathCheck(direction, pace);
        this.culmuSteps += checkedPace;
        if (this.culmuSteps >= this.maxsteps) {
            checkedPace -= this.culmuSteps - this.maxsteps;
        }

        final int nx = (int) (this.vx + direction.udx * checkedPace);
        final int ny = (int) (this.vy + direction.udy * checkedPace);

        this.updateVirtualMap(this, nx, ny);

        this.stage.addAnimation(new MovementAnimation(this.stage, this, direction,
                checkedPace));
    }

    private void turn(final Direction direction) {
        this.stage.addAnimation(new TurnAnimation(this, direction));
    }

    /**
     * Check the path along a specific direction and return how far this actor can move until blocked
     * @param direction
     * @param pace
     * @return
     */
    public int pathCheck(final Direction direction, final int pace) {
        int step;
        for (step = 1; step <= pace && this.isPassiable(direction, step); step++) {
            ;
        }
        step--;

        return step;
    }

    @Override
    public boolean isPassiable(final int x, final int y) {
        if (!this.isInbounding(x, y)) {
            return false;
        }
        return this.stage.getVirtualMap().getVirtualCells()[y][x].isPassible();
    }

    public boolean isPassiable(final Direction direction, final int step) {
        final int x = (int) (this.vx + direction.udx * step);
        final int y = (int) (this.vy + direction.udy * step);

        return this.isPassiable(x, y);
    }

    public void setDirection(final Direction dir) {
        this.direction = dir;
    }

    public void setCulmuSteps(final int steps) {
        this.culmuSteps = steps;
    }

    public void resetCulmuSteps() {
        this.setCulmuSteps(0);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Speed getSpeed() {
        return this.speed;
    }

}
