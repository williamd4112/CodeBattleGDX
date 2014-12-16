package com.codebattle.utility;

public class GameUnits {
    public static final int CELL_SIZE = 32;
    public static final int CHR_VSLICES = 4;
    public static final int CHR_HSLICES = 4;
    public static final int CHR_WIDTH = 32;
    public static final int CHR_HEIGHT = 48;
    public static final int ANI_CYCLE = 3;
    public static final int SLEEP_TIME = 10;

    public enum Speed {
        NORMAL(0.2f), FAST(0.4f), VERYFAST(0.8f);

        private final float value;

        Speed(final float value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }
    }

    public enum Interval {
        NORMAL(30), HIGH(20), VERYHIGH(10);

        private final int value;

        Interval(final int value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }
    }

    public enum Direction {
        DOWN(0), LEFT(1), RIGHT(2), UP(3);

        private final int value;

        private Direction(final int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
