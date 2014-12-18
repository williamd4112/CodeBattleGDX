package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.utility.TextureFactory;

/**
 * GameActor
 * @name : actor's name , used to load the corresponding resource
 * @direction : actor's direction
 * @interval : animation update frequency
 * @frameIndex : animation frame index
 * @frameCount : animation frame count
 * @frames : texture's regions
 *
 */

public class GameActor extends Actor
{	
	final GameStage stage;
	final int id;
	private Direction direction;
	private TextureRegion[][] frames;
	private Interval interval;
	private Speed speed;
	
	private int frame = 0;
	
	//Virtual Coordinate , used to calculate in actorsmap
	private int vx;
	private int vy;
	
	public GameActor(GameStage stage, int id, String name, float sx, float sy)
	{
		super();
		this.id = id;
		this.stage = stage;
		this.setName(name);
		this.frames = TextureFactory.getInstance().loadTextureRegionFromFile(name, 
				GameUnits.CHR_HSLICES, GameUnits.CHR_VSLICES, GameUnits.CHR_WIDTH, GameUnits.CHR_HEIGHT);
		this.direction = Direction.LEFT;
		this.interval = Interval.NORMAL; 
		this.speed = Speed.VERYFAST;
		this.setX(sx);
		this.setY(sy);
		this.vx = (int) (sx / GameUnits.CELL_SIZE);
		this.vy = (int) (sy / GameUnits.CELL_SIZE);
	}
	
	/**
	 * User interface
	 */
	public void turnDown()
	{
		this.stage.addAnimation(new GameActorTurnAnimation(this , Direction.DOWN));
	}
	
	public void turnLeft()
	{
		this.stage.addAnimation(new GameActorTurnAnimation(this , Direction.LEFT));
	}
	
	public void turnRight()
	{
		this.stage.addAnimation(new GameActorTurnAnimation(this , Direction.RIGHT));
	}
	
	public void turnUp()
	{
		this.stage.addAnimation(new GameActorTurnAnimation(this , Direction.UP));
	}
	
	public void moveDown(int pace)
	{
		this.turnDown();
		this.move(Direction.DOWN, pace);
	}
	
	public void moveLeft(int pace)
	{
		this.turnLeft();
		this.move(Direction.LEFT, pace);
	}
	
	public void moveRight(int pace)
	{
		this.turnRight();
		this.move(Direction.RIGHT, pace);
	}
	
	public void moveUp(int pace)
	{
		this.turnUp();
		this.move(Direction.UP, pace);
	}
	
	public void move(Direction direction , int pace)
	{
		int checkedPace = this.pathCheck(direction, pace);
		int nx = (int) (this.vx + direction.udx * checkedPace);
		int ny = (int) (this.vy + direction.udy * checkedPace);

		this.updateActorsMap(this.vx, this.vy, nx, ny);
		this.updateVirtualCoordinate(nx, ny);
		
		this.stage.addAnimation(new GameActorMovementAnimation(this, direction, checkedPace));
	}
		
	/**
	 * Check the path along a specific direction and return how far this actor can move until blocked
	 * @param direction
	 * @param pace
	 * @return
	 */
	public int pathCheck(Direction direction , int pace)
	{
		int step;
		for(step = 1 ; step <= pace && isPassiable(direction , step) ; step++);
		step--;
		
		return step;
	}
	
	private boolean isPassiable(int x , int y)
	{
		return (this.stage.getActorsMap()[y][x]) ? false : true;
	}
	
	private boolean isPassiable(Direction direction , int step)
	{
		int x = (int) (this.vx + direction.udx * step);
		int y = (int) (this.vy + direction.udy * step);
		
		return isPassiable(x , y);
	}
	
	/**
	 * Update stage's actors map
	 */
	private void updateActorsMap(int lastX , int lastY , int newX , int newY)
	{
		this.stage.updateActorsMap(lastX, lastY, newX, newY);
	}
	
	private void updateVirtualCoordinate(int nx , int ny)
	{
		this.vx = nx;
		this.vy = ny;
	}
	
	/*Set a new direction for this actor*/
	public void setDirection(Direction dir)
	{
		this.direction = dir;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(this.frames[this.direction.val][this.frame], this.getX(), this.getY());
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
		return this.frames[dir.val];
	}
	
	public TextureRegion[] getCurrentDirectionFrames()
	{
		return this.frames[this.direction.val];
	}
	
	/**
	 * Getters
	 */
	public Direction getDirection()
	{
		return this.direction;
	}
	
	public Speed getSpeed()
	{
		return this.speed;
	}
	
	public Interval getInterval()
	{
		return this.interval;
	}
	
	public int getFrame()
	{
		return this.frame;
	}
	
	public float getVX()
	{
		return this.vx;
	}
	
	public float getVY()
	{
		return this.vy;
	}
	
	@Override
	public String getName()
	{
		return (this.id == 0) ? super.getName() : super.getName() + String.valueOf(id);
	}
	
	/**
	 * Setters
	 */
	public void setFrame(int frame)
	{
		this.frame = (frame < GameUnits.CHR_HSLICES) ? frame : 0;
	}
}
