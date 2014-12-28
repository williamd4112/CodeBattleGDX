package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codebattle.model.GameStage;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Speed;
import com.codebattle.utility.GameConstants;

public class GameActorMovementAnimation extends BaseAnimation{
	
	final public GameStage stage;
	
	private final GameActor actor;
	private final Speed speed;
	private final float dx , dy;
	private float pixelDiff;
	
	/**
	 * Binding a actor and direction, perform a same direction movement (e.g. 5 steps to upward)
	 * @param textureRegions
	 * @param direction
	 * @param steps
	 */
	public GameActorMovementAnimation(GameStage stage, GameActor actor, Direction direction, int steps)
	{
		this.stage = stage;
		this.actor = actor;
		this.speed = actor.getSpeed();
		this.dx = direction.udx * this.speed.value;
		this.dy = direction.udy * this.speed.value;
		this.pixelDiff = steps * GameConstants.CELL_SIZE;
	}
	
	@Override
	public void setup() 
	{

	}
	
	@Override
	public void update() 
	{			
		this.stage.setCameraTarget(this.actor);
		if(this.pixelDiff > 0) {
			this.actor.moveBy(this.dx, this.dy);
			this.pixelDiff -= this.speed.value;
		}
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

	@Override
	public void finished() 
	{	
		this.actor.setDirection(Direction.HOLD_DEF);
	}

	@Override
	public void draw(Batch batch, Camera camera,  float delta) 
	{
		
	}
	
}
