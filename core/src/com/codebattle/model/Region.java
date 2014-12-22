package com.codebattle.model;

/**
 * Record the sprite chunk info
 */
public class Region
{
	public int x;
	public int y;
	public int width;
	public int height;
	
	@Override
	public String toString()
	{
		return String.format("(%d , %d , %d , %d)\n", x,y,width,height);
	}
}
