package com.codebattle.utility;

public class AnimationUtil {
	
	public static int frameOscillate(int current, int lowerBound , int upperBound)
	{
		if(current < upperBound) {
			return current + 1;
		}else {
			return lowerBound;
		}
	}
}
