package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @update: change some properties in a fix period
 * @draw: draw some texture
 * @isFinished: called by the main-loop's animation processing
 * @finished: called by the main-loop's animation processing
 * @author williamd
 *
 */

abstract public class BaseAnimation {

    abstract public void update(float delta);

    abstract public void draw(Batch batch, Camera camera, float delta);

    abstract public boolean isFinished();

    abstract public void finished();

    abstract public void setup();
}
