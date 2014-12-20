package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codebattle.model.GameActor;
import com.codebattle.model.units.Direction;

public class GameActorTurnAnimation extends Animation{
	
	final Direction direction;
	final GameActor actor;
	
	public GameActorTurnAnimation(GameActor actor, Direction direction)
	{
		this.actor = actor;
		this.direction = direction;
	}
	
	@Override
	public void setup() 
	{
		super.setup();	
	}
	
	@Override
	public void update() 
	{
		this.actor.setDirection(direction);
	}

	@Override
	public boolean isFinished() 
	{
		return (this.actor.getDirection() == this.direction) ? true : false;
	}

	@Override
	public String toString()
	{
		return String.format("GameTurn(%s , %s)",this.actor.getName() ,this.direction);
	}

	@Override
	public void finished() 
	{
		
	}

	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		
	}
}
