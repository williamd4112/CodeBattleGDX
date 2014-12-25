package com.codebattle.model;

import com.codebattle.utility.GameConstants;

/**
 * Virtual Cell used to calculate action or movement in virtuality
 * @actor : actor
 * @passiable : map
 * @author williamd
 *
 */
public class VirtualCell {
	
	final int x , y;
	
	private GameObject obj = null;
	private boolean passiable = true;
	
	public VirtualCell(GameObject actor, int x , int y, boolean passiable)
	{
		this.obj = actor;
		this.passiable = passiable;
		this.x = x;
		this.y = y;
	}
	
	public VirtualCell(GameObject actor, int x, int y)
	{
		this(actor, x, y, true);
	}
	
	public VirtualCell(int x , int y)
	{
		this(null, x, y, true);
	}
	
	public void setObject(GameObject obj , int x , int y)
	{
		this.obj = obj;
		if(obj != null)
			System.out.printf("set %s(%d , %d) at (%d , %d)\n", this.obj.getName(),this.obj.getVX(),this.obj.getVY(),x,y);
	}
	
	public void setPassiable(boolean flag)
	{
		this.passiable = flag;
	}
	
	public void removeObject(int x, int y)
	{
		if(this.obj != null)
		System.out.printf("remove %s(%d , %d) from (%d , %d)\n", this.obj.getName(),this.obj.getVX(),this.obj.getVY(),x,y);
		this.obj = null;
	}
	
	public GameObject getObject()
	{
		return this.obj;
	}
	
	public float getX()
	{
		return this.x * GameConstants.CELL_SIZE;
	}
	
	public float getY()
	{
		return this.y * GameConstants.CELL_SIZE;
	}
		
	public boolean isPassible()
	{
		if(this.obj != null) return (obj.isAlive()) ? false : true;
		return this.passiable;
	}
}
