package com.codebattle.model.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.meta.Region;
import com.codebattle.model.units.Interval;
import com.codebattle.utility.AnimationUtil;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.TextureFactory;

public class CursorAnimation extends ParallelAnimation{

	private TextureRegion[] frames;
	private int upperBound , lowerBound;
	private int frame;
	private int time = 0;
	private Interval interval;
	private boolean visiable = false;
	private float x , y;
	
	/**
	 * Pass a cursor name (because not only one kind of cursor
	 * @param resName
	 * @throws Exception 
	 */
	public CursorAnimation(String resName , Region region, Interval interval, int x , int y) throws Exception
	{
		this.frames = TextureFactory.getInstance().loadSelectCursorFromFile(resName, region);
		this.interval = interval;
		this.upperBound = this.frames.length - 1;
		this.lowerBound = 0;
		this.frame = 0;
	}
	
	public CursorAnimation() throws Exception
	{
		this(GameConstants.ONSELECT_CURSOR , GameConstants.ONSELECT_CURSOR_REGION , GameConstants.ONSELECT_CURSOR_INTERVAL , 0 , 0);
	}
	
	public void setVisiable(boolean flag)
	{
		this.visiable = flag;
	}
	
	public void setPosition(float x , float y)
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
		if(this.time++ >= this.interval.val) {
			this.frame = AnimationUtil.frameOscillate(this.frame, lowerBound, upperBound);
			this.time = 0;
		}
	}

	@Override
	public void draw(Batch batch) 
	{
		if(!this.visiable) return;
		batch.draw(frames[frame], x, y);
	}

}
