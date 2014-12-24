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

abstract public class Animation {
	
	boolean isSetup = false;
	
	abstract public void update();
	abstract public void draw(Batch batch , Camera camera, float delta);
	abstract public boolean isFinished();
	abstract public void finished();
	
	public void setup()
	{
		if(this.isSetup) return;
		this.isSetup = true;
	}
}
