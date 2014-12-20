package com.codebattle.model.units;

/**
 * Enumeration of speed
 * NORMAL
 * FAST
 * VERYFAST
 * @author williamd
 */
public enum Speed {
    NORMAL(0.2f), FAST(0.4f), VERYFAST(2.0f);

    final public float value;

    Speed(final float value) {
        this.value = value;
    }
}
