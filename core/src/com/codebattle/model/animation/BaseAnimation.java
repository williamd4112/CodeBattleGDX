package com.codebattle.model.animation;

import java.util.LinkedList;
import java.util.List;

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

    // Sound variable
    protected List<String> sounds;

    public BaseAnimation() {
        this.sounds = new LinkedList<String>();
    }

    abstract public void update(float delta);

    abstract public void draw(Batch batch, Camera camera, float delta);

    abstract public boolean isFinished();

    abstract public void finished();

    abstract public void setup();
}
