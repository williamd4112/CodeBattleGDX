package com.codebattle.model;

import com.badlogic.gdx.Gdx;


public class GameActorMovementAnimation extends Animation{
	
	private final GameActor actor;
	private final Speed speed;
	private final float dx , dy;
	private final Interval interval;
	private float pixelDiff;
	
	private int frame = 0;
	
	/**
	 * Binding a actor and direction, perform a same direction movement (e.g. 5 steps to upward)
	 * @param textureRegions
	 * @param direction
	 * @param steps
	 */
	public GameActorMovementAnimation(GameActor actor, Direction direction, int steps)
	{
		this.actor = actor;
		this.speed = actor.getSpeed();
		this.interval = actor.getInterval();
		this.dx = direction.udx * this.speed.val;
		this.dy = direction.udy * this.speed.val;
		this.pixelDiff = steps * GameUnits.CELL_SIZE;
	}
	
	@Override
	public void update() 
	{			
		updatePosition();
		updateFrame();
	}

	@Override
	public boolean isFinished() 
	{
		return (this.pixelDiff <= 0) ? true : false;
	}
	
	@Override
	public String toString()
	{
		return String.format("GameMovment(%s , %s)",this.actor.getName() ,this.pixelDiff);
	}
	
	/**
	 * Update data
	 */
	public void updatePosition()
	{
		if(this.pixelDiff > 0) {
			this.actor.moveBy(this.dx, this.dy);
			this.pixelDiff -= this.speed.val;
			this.updateFrame();
		}
	}
	
	public void updateFrame()
	{
		if(this.frame < this.interval.val) {
			this.frame++;
		}else {
			this.frame = 0;
			this.actor.setFrame(this.actor.getFrame() + 1);
		}
	}
}
