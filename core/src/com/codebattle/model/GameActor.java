package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.utility.TextureFactory;

/*
 * GameActor
 * @name : actor's name , used to load the corresponding resource
 * @direction : actor's direction
 * @interval : animation update frequency
 * @frameIndex : animation frame index
 * @frameCount : animation frame count
 * @frames : texture's regions
 * */

public class GameActor extends Actor
{
	public enum Direction
	{
		DOWN(0),
		LEFT(1),
		RIGHT(2),
		UP(3);
		
		public int value;
		private Direction(int value)
		{
			this.value = value;
		}
		
		public void set(int value)
		{
			this.value = value;
		}
	}
	
	final String name;
	private Direction direction;
	private TextureRegion[][] frames;
	final private int sleepTime = 10;
	private int interval;
	private int frameIndex;
	private int frameCount;
	private float speed;
	
	public GameActor(String name , float sx , float sy)
	{
		super();
		this.name = name;
		this.frames = TextureFactory.getInstance().loadTextureRegionFromFile(name, 4, 4, 32, 48);
		this.direction = Direction.DOWN;
		this.frameIndex = 0;
		this.frameCount = 0;
		this.interval = 30; //default
		this.speed = 0.2f;
		this.setX(sx);
		this.setY(sy);
	}
	
	/*Turn the actor's facing*/
	public void turn(int value)
	{
		direction.set(value);
	}
	
	/*Stop animation , reseting all animation variables*/
	public void stop()
	{
		this.frameCount = 0;
		this.frameIndex = 0;
	}
	
	public void moveRight(int pace)
	{
		while(pace > 0) {
		}
	}
	
	/*Move to the destination point*/
	private void moveTo(float x	 , float y)
	{
		float destX = x , destY = y;
		try {
			while(!isReached(destX , destY)) {
				switch(this.direction) {
				case DOWN:
					this.moveBy(0 , -speed);
					break;
				case LEFT:
					this.moveBy(speed, 0);
					break;
				case RIGHT:
					this.moveBy(-speed, 0);
					break;
				case UP:
					this.moveBy(0, speed);
					break;
				default:
					break;
				}
				Thread.sleep(this.sleepTime);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Judging if reached end*/
	private boolean isReached(float destX , float destY)
	{
		switch(this.direction) {
		case DOWN:
			return (this.getX() == destX && this.getY() <= destY) ? true : false;
		case LEFT:
			return (this.getX() <= destX && this.getY() == destY) ? true : false;
		case RIGHT:
			return (this.getX() >= destX && this.getY() == destY) ? true : false;
		case UP:
			return (this.getX() == destX && this.getY() >= destY) ? true : false;
		default:
			break;
		}
		
		return true;
	}
	
	@Override
	public void moveBy(float x, float y) {
		// TODO Auto-generated method stub
		super.moveBy(x, y);
		frameCount = (frameCount < interval) ? frameCount + 1 : 0;
		
		if(frameCount >= interval)
			frameIndex = (frameIndex < 3) ? frameIndex + 1 : 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(frames[this.direction.value][frameIndex], this.getX(), this.getY());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
}
