package com.codebattle.model;

/**
 * Enumeration of speed
 * NORMAL
 * FAST
 * VERYFAST
 * @author williamd
 */
public enum Speed
{
	NORMAL(0.2f),
	FAST(0.4f),
	VERYFAST(0.8f);
	
	final public float val;
	Speed(float val)
	{
		this.val = val;
	}
}
