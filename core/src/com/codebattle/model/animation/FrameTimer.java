package com.codebattle.model.animation;

import com.codebattle.utility.AnimationUtil;

public class FrameTimer {
    private int time = 0;
    private int frame = 0;
    private int range;
    private int interval;

    public FrameTimer(int interval, int range) {
        this.interval = interval;
        this.range = range;
    }

    public void act() {
        time++;
        if (time >= interval) {
            time = 0;
            frame = AnimationUtil.frameOscillate(frame, 0, range);
        }
    }

    public int getFrame() {
        return this.frame;
    }
}
