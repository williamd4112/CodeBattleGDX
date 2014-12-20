package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	abstract public void draw(SpriteBatch batch , float delta);
	abstract public boolean isFinished();
	abstract public void finished();
	
	public void setup()
	{
		if(this.isSetup) return;
		this.isSetup = true;
			
	}
}
