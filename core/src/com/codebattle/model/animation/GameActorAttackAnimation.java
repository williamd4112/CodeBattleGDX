package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codebattle.model.GameActor;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.units.Direction;

public class GameActorAttackAnimation extends Animation{

	final public GameStage stage;
	final public GameActor attacker;
	final public GameObject target;
	
	private int duration;
	
	public GameActorAttackAnimation(GameStage stage, GameActor attacker, GameObject target)
	{
		this.stage = stage;
		this.attacker = attacker;
		this.target = target;
		this.duration = 30;	
	}
	
	@Override
	public void setup() 
	{
		super.setup();
		this.attacker.setDirection(Direction.HOLD_ATK);
	}
	
	@Override
	public void update() 
	{	
		setup();
		this.duration--;
	}

	@Override
	public boolean isFinished()
	{
		return (this.duration <= 0);
	}

	@Override
	public void finished() 
	{
		this.attacker.setDirection(Direction.HOLD_DEF);
		if(!this.target.isAlive())
			this.stage.removeGameObject(target);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		
	}

}
