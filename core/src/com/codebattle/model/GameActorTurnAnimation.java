package com.codebattle.model;

public class GameActorTurnAnimation extends Animation{
	
	final Direction direction;
	final GameActor actor;
	
	public GameActorTurnAnimation(GameActor actor, Direction direction)
	{
		this.actor = actor;
		this.direction = direction;
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
}
