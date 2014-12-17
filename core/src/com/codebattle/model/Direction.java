package com.codebattle.model;

/**
 * Enumeration of Direction
 * @author williamd
 *
 */
public enum Direction {
    DOWN(0), LEFT(1), RIGHT(2), UP(3);

    final public int value;

    private Direction(final int value) {
        this.value = value;
    }
}
