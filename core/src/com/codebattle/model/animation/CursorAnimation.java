package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.meta.Region;
import com.codebattle.model.units.Interval;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.TextureFactory;

public class CursorAnimation extends ParallelAnimation {

    private final TextureRegion[] frames;
    private final int upperBound, lowerBound;
    private int frame;
    private int time = 0;
    private final Interval interval;
    private boolean visiable = false;
    private float x, y;

    /**
     * Pass a cursor name (because not only one kind of cursor
     * @param resName
     * @throws Exception
     */
    public CursorAnimation(final String resName, final Region region,
            final Interval interval, final int x, final int y) throws Exception
    {
        this.frames = TextureFactory.getInstance().loadSelectCursorFromFile(resName, region);
        this.interval = interval;
        this.upperBound = this.frames.length - 1;
        this.lowerBound = 0;
        this.frame = 0;
    }

    public CursorAnimation() throws Exception
    {
        this(GameConstants.ONSELECT_CURSOR, GameConstants.ONSELECT_CURSOR_REGION,
                GameConstants.ONSELECT_CURSOR_INTERVAL, 0, 0);
    }

    public void setVisiable(final boolean flag)
    {
        this.visiable = flag;
    }

    public void setPosition(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setup()
    {

    }

    @Override
    public void update()
    {
        if (this.time++ >= this.interval.val) {
            this.frame =
                    AnimationUtil.frameOscillate(this.frame, this.lowerBound, this.upperBound);
            this.time = 0;
        }
    }

    @Override
    public void draw(final Batch batch)
    {
        if (!this.visiable) {
            return;
        }
        batch.draw(this.frames[this.frame], this.x, this.y);
    }

}
