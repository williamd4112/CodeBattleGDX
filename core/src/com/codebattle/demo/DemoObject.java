package com.codebattle.demo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DemoObject extends Actor {
    private final TextureRegion region;
    private final Texture texture;

    public DemoObject(final String source) {
        this.texture = new Texture("graphics/characters/" + source);
        this.region = new TextureRegion(this.texture, 0, 0, 32, 48);
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        final Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(this.texture, this.getX(), this.getY(), 0, 0, 32, 48);
    }

    public void move(final int direction, int distance) {
        try {
            while (distance > 0) {
                switch (direction) {
                case 2: {
                    this.moveBy(0, -1);
                    break;
                }
                case 4: {
                    this.moveBy(-1, 0);
                    break;
                }
                case 6: {
                    this.moveBy(1, 0);
                    break;
                }
                case 8: {
                    this.moveBy(0, 1);
                    break;
                }
                default:
                    break;
                }

                distance -= 1;
            }

            Thread.sleep(100);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
