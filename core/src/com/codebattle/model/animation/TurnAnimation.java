package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.units.Direction;

public class TurnAnimation extends BaseAnimation {

    final Direction direction;
    final MoveableGameObject obj;

    public TurnAnimation(final MoveableGameObject obj, final Direction direction) {
        this.obj = obj;
        this.direction = direction;
    }

    @Override
    public void setup() {

    }

    @Override
    public void update(final float delta) {
        this.obj.setDirection(this.direction);
    }

    @Override
    public boolean isFinished() {
        return this.obj.getDirection() == this.direction ? true : false;
    }

    @Override
    public String toString() {
        return String.format("GameTurn(%s , %s)", this.obj.getName(), this.direction);
    }

    @Override
    public void finished() {

    }

    @Override
    public void draw(final Batch batch, final Camera camera, final float delta) {

    }
}
