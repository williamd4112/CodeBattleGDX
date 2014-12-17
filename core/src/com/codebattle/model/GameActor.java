package com.codebattle.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.codebattle.utility.GameUnits;
import com.codebattle.utility.TextureFactory;

public class GameActor extends Actor {
    // Actor's direction
    private Direction direction;
    // Texture's regions
    private final TextureRegion[][] frames;
    // Animation update frequency
    private final Interval interval;
    // Animation frame index
    private int frameIndex;
    // Animation frame count
    private int frameCount;
    private final Speed speed;

    /**
     * Create a game actor.
     *
     * @param name     Actor name
     * @param sx       Position X
     * @param sy       Position Y
     */
    public GameActor(final String name, final float sx, final float sy) {
        super();
        this.setName(name);
        this.frames = TextureFactory.getInstance()
                .loadTextureRegionFromFile(name, GameUnits.CHR_VSLICES, GameUnits.CHR_HSLICES,
                        GameUnits.CHR_WIDTH, GameUnits.CHR_HEIGHT);
        this.direction = Direction.DOWN;
        this.frameIndex = 0;
        this.frameCount = 0;
        this.interval = Interval.HIGH;
        this.speed = Speed.VERYFAST;
        this.setX(sx);
        this.setY(sy);
    }

    /*
     * User interface: turn
     */

    public void turnDown() {
        this.setDirection(Direction.DOWN);
    }

    public void turnLeft() {
        this.setDirection(Direction.LEFT);
    }

    public void turnRight() {
        this.setDirection(Direction.RIGHT);
    }

    public void turnUp() {
        this.setDirection(Direction.UP);
    }

    /*
     * User interface: move
     */

    public void moveDown(final int pace) {
        this.move(pace, Direction.DOWN);
    }

    public void moveLeft(final int pace) {
        this.move(pace, Direction.LEFT);
    }

    public void moveRight(final int pace) {
        this.move(pace, Direction.RIGHT);
    }

    public void moveUp(final int pace) {
        this.move(pace, Direction.UP);
    }

    /**
     * Stop the animation and reset all animation variables.
     */
    private void stop() {
        this.frameCount = 0;
        this.frameIndex = 0;
    }

    /**
     * Set a new direction.
     *
     * @param dir   New direction
     */
    private void setDirection(final Direction dir) {
        this.direction = dir;
    }

    /**
     * Move toward a direction in many paces.
     *
     * @param pace  Pace
     * @param dir   Direction
     */
    private void move(final int pace, final Direction dir) {
        try {
            this.setDirection(dir);

            for (float diff = pace * GameUnits.CELL_SIZE; diff > 0; diff -= this.speed.value) {
                switch (dir) {
                case DOWN: {
                    this.moveBy(0, -this.speed.value);
                    break;
                }
                case LEFT: {
                    this.moveBy(-this.speed.value, 0);
                    break;
                }
                case RIGHT: {
                    this.moveBy(this.speed.value, 0);
                    break;
                }
                case UP: {
                    this.moveBy(0, this.speed.value);
                    break;
                }
                default:
                    break;
                }

                Thread.sleep(GameUnits.SLEEP_TIME);
            }

            this.stop();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveBy(final float x, final float y) {
        // TODO Auto-generated method stub
        super.moveBy(x, y);
        this.frameCount = this.frameCount < this.interval.value ? this.frameCount + 1 : 0;

        if (this.frameCount >= this.interval.value) {
            this.frameIndex = this.frameIndex < GameUnits.ANI_CYCLE ? this.frameIndex + 1 : 0;
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(this.frames[this.direction.value][this.frameIndex], this.getX(), this.getY());
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
    }

    /**
     * Get a row (specific direction) frames.
     *
     * @param dir   Direction
     * @return texture region
     */
    public TextureRegion[] getDirectionFrames(final Direction dir) {
        return this.frames[dir.value];
    }

    public TextureRegion[] getCurrentDirectionFrames() {
        return this.frames[this.direction.value];
    }
}
