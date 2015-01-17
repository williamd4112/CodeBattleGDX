package com.codebattle.utility;

public class AnimationUtil {

    public static int frameOscillate(final int current, final int lowerBound,
            final int upperBound) {
        if (current < upperBound) {
            return current + 1;
        } else {
            return lowerBound;
        }

    }
}
