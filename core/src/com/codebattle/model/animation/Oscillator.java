package com.codebattle.model.animation;

public class Oscillator {
    private final float amplitude, frequency;
    private float time = 0;

    public Oscillator(final float amplitude, final float frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    private void act() {
        if (this.time < 360) {
            this.time += this.frequency;
        } else {
            this.time = 0;
        }
    }

    public float getValue() {
        this.act();
        return (float) (this.amplitude * Math.cos(this.time));
    }
}
