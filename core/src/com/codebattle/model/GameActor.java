package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.utility.GameUnits;
import com.codebattle.utility.GameUnits.Direction;
import com.codebattle.utility.GameUnits.Interval;
import com.codebattle.utility.GameUnits.Speed;
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
	private Direction direction;
	private TextureRegion[][] frames;
	private Interval interval;
	private int frameIndex;
	private int frameCount;
	private Speed speed;
		
	public GameActor(String name , float sx , float sy)
	{
		super();
		this.setName(name);
		this.frames = TextureFactory.getInstance().loadTextureRegionFromFile(name, 
				GameUnits.CHR_VSLICES, GameUnits.CHR_HSLICES, GameUnits.CHR_WIDTH, GameUnits.CHR_HEIGHT);
		this.direction = Direction.DOWN;
		this.frameIndex = 0;
		this.frameCount = 0;
		this.interval = Interval.HIGH; 
		this.speed = Speed.VERYFAST;
		this.setX(sx);
		this.setY(sy);
	}
	
	/*User interface : turn*/
	public void turnDown()
	{
		this.setDirection(Direction.DOWN);
	}
	
	public void turnLeft()
	{
		this.setDirection(Direction.LEFT);
	}
	
	public void turnRight()
	{
		this.setDirection(Direction.RIGHT);
	}
	
	public void turnUp()
	{
		this.setDirection(Direction.UP);
	}
	
	/*User interface : move*/
	public void moveDown(int pace)
	{
		this.move(pace, Direction.DOWN);
	}
	
	public void moveLeft(int pace)
	{
		this.move(pace, Direction.LEFT);
	}
	
	public void moveRight(int pace)
	{
		this.move(pace, Direction.RIGHT);
	}
	
	public void moveUp(int pace)
	{
		this.move(pace, Direction.UP);
	}
	
	/*Stop animation , reseting all animation variables*/
	private void stop()
	{
		this.frameCount = 0;
		this.frameIndex = 0;
	}
	
	/*Set a new direction for this actor*/
	private void setDirection(Direction dir)
	{
		this.direction = dir;
	}

	/*Move to specific direction in a paces*/
	private void move(int pace , Direction dir)
	{
		try {
			this.setDirection(dir);
			for(float diff = pace * GameUnits.CELL_SIZE ; diff > 0 ; diff -= speed.Val()) {
				switch(dir) {
				case DOWN:
					this.moveBy(0 , -speed.Val());
					break;
				case LEFT:
					this.moveBy(-speed.Val() , 0);
					break;
				case RIGHT:
					this.moveBy(speed.Val() , 0);
					break;
				case UP:
					this.moveBy(0 , speed.Val());
					break;
				default:
					break;
				}
				Thread.sleep(GameUnits.SLEEP_TIME);
			}
			stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void moveBy(float x, float y) {
		// TODO Auto-generated method stub
		super.moveBy(x, y);
		frameCount = (frameCount < interval.Val()) ? frameCount + 1 : 0;
		
		if(frameCount >= interval.Val())
			frameIndex = (frameIndex < GameUnits.ANI_CYCLE) ? frameIndex + 1 : 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(frames[this.direction.Val()][frameIndex], this.getX(), this.getY());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	/**
	 * Get a row (specific direction) frames
	 * @param dir
	 * @return
	 */
	public TextureRegion[] getDirectionFrames(Direction dir)
	{
		return this.frames[dir.Val()];
	}
	
	public TextureRegion[] getCurrentDirectionFrames()
	{
		return this.frames[this.direction.Val()];
	}
	
}
