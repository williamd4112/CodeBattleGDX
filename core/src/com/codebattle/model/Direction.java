package com.codebattle.model;


/**
 * Enumeration of Direction
 * @author williamd
 *
 */
public enum Direction
{
	DOWN(0),
	LEFT(1),
	RIGHT(2),
	UP(3);
	
	final private int val;
	private Direction(int value)
	{
		this.val = value;
	}
	
	public int Val()
	{
		return this.val;
	}
}