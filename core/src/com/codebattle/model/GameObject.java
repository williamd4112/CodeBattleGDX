package com.codebattle.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.utility.GameConstants;

/**
 * Define a general object over this game
 * Everything which is functional in the map is derived from this class
 * @author williamd
 *
 */
abstract public class GameObject extends Actor{
	
	//Where is this object
	//Repeated object avoidance
	final public GameStage stage;
	final protected int id;
	
	//Virtual Coordinate , used to calculate in actorsmap
	protected int vx;
	protected int vy;
	
	//Store actor's state, different state may cause some effect
	//Note: Death state used in virtual map (temporary removing actor
	protected GameObjectState state;
	
	public GameObject(GameStage stage, String name, int id, float sx, float sy)
	{
		super();
		this.stage = stage;
		this.setName(name);
		this.id = id;
		this.state = GameObjectState.ALIVE;
		this.setX(sx);
		this.setY(sy);
		this.resetVirtualCoordinate();
	}
	
	protected void updateVirtualMap(GameObject obj, int newX , int newY)
	{
		System.out.printf("update object (%d , %d) to (%d , %d)\n",obj.vx,obj.vy,newX,newY);
		this.stage.getVirtualMap().updateVirtualMap(this, newX, newY);
	}
	
	protected void resetVirtualCoordinate()
	{
		this.vx = (int) (this.getX() / GameConstants.CELL_SIZE);
		this.vy = (int) (this.getY() / GameConstants.CELL_SIZE);
		System.out.printf("Restore %s to (%d , %d)\n", this.getName(), this.vx, this.vy);
	}
	
	public boolean isInbounding(int x , int y)
	{
		return (x >= 0 && x < this.stage.getMapWidth() && y >= 0 && y < this.stage.getMapHeight()) ? true : false;
	}
	
	public boolean isAlive()
	{
		return (this.state == GameObjectState.ALIVE);
	}
	
	@Override
	public String getName()
	{
		return (this.id == 0) ? super.getName() : super.getName() + String.valueOf(id);
	}
	
	public int getVX()
	{
		return this.vx;
	}
	
	public int getVY()
	{
		return this.vy;
	}
	
	public void setVirtualCoordinate(int x, int y)
	{
		this.vx = x;
		this.vy = y;
	}
	
	abstract public boolean isPassiable(int x, int y);
	abstract public void onAttacked(GameObject attacker);
	abstract public void attack(int x, int y);
	abstract public boolean onInteract(GameObject contacter);
	abstract public void interact(int x, int y);
}
