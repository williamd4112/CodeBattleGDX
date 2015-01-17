package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.Batch;

abstract public class ParallelAnimation
{
    abstract public void setup();

    abstract public void update();

    abstract public void draw(Batch batch);
}
