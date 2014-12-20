package com.codebattle.model.units;

/**
 * Enumeration of Interval(Animation Frame switch interval , not translation interval)
 * @author williamd
 *
 */
public enum Interval
{
	NORMAL(20),
	HIGH(15),
	VERYHIGH(5);
	
	final public int val;
	Interval(int val){
		this.val = val;
	}
}
