package com.codebattle.model.animation;

import com.codebattle.utility.AnimationUtil;

public class FrameTimer {
    private int time = 0;
    private int frame = 0;
    private final int range;
    private final int interval;

    public FrameTimer(final int interval, final int range) {
        this.interval = interval;
        this.range = range;
    }

    public void act() {
        this.time++;
        if (this.time >= this.interval) {
            this.time = 0;
            this.frame = AnimationUtil.frameOscillate(this.frame, 0, this.range);
        }
    }

    public int getFrame() {
        return this.frame;
    }
}
