package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.units.Direction;

public class GameActorTurnAnimation extends BaseAnimation {

    final Direction direction;
    final MoveableGameObject obj;

    public GameActorTurnAnimation(MoveableGameObject obj, Direction direction) {
        this.obj = obj;
        this.direction = direction;
    }

    @Override
    public void setup() {

    }

    @Override
    public void update() {
        this.obj.setDirection(direction);
    }

    @Override
    public boolean isFinished() {
        return (this.obj.getDirection() == this.direction) ? true : false;
    }

    @Override
    public String toString() {
        return String.format("GameTurn(%s , %s)", this.obj.getName(), this.direction);
    }

    @Override
    public void finished() {

    }

    @Override
    public void draw(Batch batch, Camera camera, float delta) {

    }
}
