package com.codebattle.model;

import com.codebattle.model.animation.GameActorTurnAnimation;
import com.codebattle.model.animation.MovementAnimation;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;

abstract public class MoveableGameObject extends GameObject {

    protected Direction direction;
    protected Speed speed;

    protected int culmuSteps = 0;

    final protected int maxsteps;

    public MoveableGameObject(GameStage stage, Owner owner, String source, String name, int id,
            float sx, float sy, int maxsteps) {
        super(stage, owner, source, name, id, sx, sy);
        this.direction = Direction.HOLD_DEF;
        this.speed = Speed.VERYFAST;
        this.maxsteps = maxsteps;
    }

    /**
     * User interface
     */
    public void moveDown(int pace) {
        this.turn(Direction.DOWN);
        this.move(Direction.DOWN, pace);
    }

    public void moveLeft(int pace) {
        this.turn(Direction.LEFT);
        ;
        this.move(Direction.LEFT, pace);
    }

    public void moveRight(int pace) {
        this.turn(Direction.RIGHT);
        this.move(Direction.RIGHT, pace);
    }

    public void moveUp(int pace) {
        this.turn(Direction.UP);
        this.move(Direction.UP, pace);
    }

    public void move(Direction direction, int pace) {
        // Check steps
        int checkedPace = this.pathCheck(direction, pace);
        this.culmuSteps += checkedPace;
        if (this.culmuSteps >= maxsteps) {
            checkedPace -= this.culmuSteps - maxsteps;
        }

        int nx = (int) (this.vx + direction.udx * checkedPace);
        int ny = (int) (this.vy + direction.udy * checkedPace);

        this.updateVirtualMap(this, nx, ny);

        this.stage.addAnimation(new MovementAnimation(this.stage, this, direction, checkedPace));
    }

    private void turn(Direction direction) {
        this.stage.addAnimation(new GameActorTurnAnimation(this, direction));
    }

    /**
     * Check the path along a specific direction and return how far this actor can move until blocked
     * @param direction
     * @param pace
     * @return
     */
    public int pathCheck(Direction direction, int pace) {
        int step;
        for (step = 1; step <= pace && isPassiable(direction, step); step++)
            ;
        step--;

        return step;
    }

    @Override
    public boolean isPassiable(int x, int y) {
        if (!this.isInbounding(x, y))
            return false;
        return this.stage.getVirtualMap()
                .getVirtualCells()[y][x].isPassible();
    }

    public boolean isPassiable(Direction direction, int step) {
        int x = (int) (this.vx + direction.udx * step);
        int y = (int) (this.vy + direction.udy * step);

        return isPassiable(x, y);
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public void setCulmuSteps(int steps) {
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
