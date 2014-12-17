package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.utility.GameUnits.Direction;

public class GameActorAnimation extends Animation{
	
	private final GameActor actor;
	private final TextureRegion[] frames;
	private final float destX , destY;
	
	/**
	 * Assign the animation's frames ()
	 * @param frames
	 */
	public GameActorAnimation(GameActor actor , float destX , float destY)
	{
		this.actor = actor;
		this.frames = actor.getCurrentDirectionFrames();
		this.destX = destX;
		this.destY = destY;
	}
	
	@Override
	public void update(float delta) 
	{
	
		
	}

	@Override
	public void render(float delta) 
	{
		
	}

}
