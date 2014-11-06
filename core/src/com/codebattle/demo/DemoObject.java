package com.codebattle.demo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DemoObject extends Actor {
	
    private TextureRegion region;
    private Texture texture;
    
	public DemoObject(String source) {
    	texture = new Texture("graphics/characters/" + source);
        region = new TextureRegion(texture , 0 , 0 , 32 , 48);
    }
    
    @Override
	public Actor hit(float x, float y, boolean touchable) {
		// TODO Auto-generated method stub
		return this;
	}
	
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY() , 0 , 0 , 32 , 48);
    }
    
    public void move(int direction , int distance)
    {
		try {
	    	while(distance > 0){
	    		switch(direction) {
	    		case 2:
	    			moveBy(0, -1);
	    			break;
	    		case 4:
	    			moveBy(-1 , 0);
	    			break;
	    		case 6:
	    			moveBy(1 , 0);
	    			break;
	    		case 8:
	    			moveBy(0 , 1);
	    			break;
	    		default:
	    			break;
	    		}
	    		--distance;
	    	}
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
