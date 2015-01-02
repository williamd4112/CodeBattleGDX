package com.codebattle.model.animation;

public class Oscillator {
    private float amplitude, frequency;
    private float time = 0;

    public Oscillator(float amplitude, float frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    private void act() {
        if (time < 360)
            time += frequency;
        else
            time = 0;
    }

    public float getValue() {
        act();
        return (float) (this.amplitude * Math.cos(time));
    }
}
