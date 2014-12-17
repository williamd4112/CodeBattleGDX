package com.codebattle.model;

/**
 * Enumeration of Interval(Animation Frame switch interval , not translation interval)
 * @author williamd
 *
 */
public enum Interval {
    NORMAL(30), HIGH(20), VERYHIGH(10);

    final public int value;

    Interval(final int value) {
        this.value = value;
    }
}