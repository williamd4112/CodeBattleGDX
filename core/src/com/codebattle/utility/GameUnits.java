package com.codebattle.utility;

public class GameUnits {
	
	public final static int CELL_SIZE = 32;
	public final static int CHR_VSLICES = 4;
	public final static int CHR_HSLICES = 4;
	public final static int CHR_WIDTH = 32;
	public final static int CHR_HEIGHT = 48;
	public final static int ANI_CYCLE = 3; 
	public final static int SLEEP_TIME = 10;

	public enum Speed{
		NORMAL(0.2f),
		FAST(0.4f),
		VERYFAST(0.8f);
		
		private float val;
		Speed(float val)
		{
			this.val = val;
		}
		
		public float Val()
		{
			return this.val;
		}
	}
	
	public enum Interval{
		NORMAL(30),
		HIGH(20),
		VERYHIGH(10);
		
		private int val;
		Interval(int val){
			this.val = val;
		}
		
		public float Val()
		{
			return this.val;
		}
	}
	
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
	
}
