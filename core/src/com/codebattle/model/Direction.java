package com.codebattle.model;


/**
 * Enumeration of Direction
 * @author williamd
 *
 */
public enum Direction
{	
	HOLD_DEF(0, 0, 0),
	HOLD_ATK(1, 0, 0),
	LEFT(2, -1, 0),
	RIGHT(3, 1, 0),
	DOWN(4, 0, -1),
	UP(5, 0, 1);
	
	final public int val;
	final public float udx , udy;
	private Direction(int value , float udx , float udy)
	{
		this.val = value;
		this.udx = udx;
		this.udy = udy;
	}
}