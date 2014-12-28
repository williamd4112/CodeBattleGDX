package com.codebattle.model.gameactor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.animation.GameActorMovementAnimation;
import com.codebattle.model.animation.GameActorTurnAnimation;
import com.codebattle.model.structure.Attack;
import com.codebattle.model.structure.GameActorType;
import com.codebattle.model.structure.Skill;
import com.codebattle.model.units.Direction;
import com.codebattle.model.units.Interval;
import com.codebattle.model.units.Speed;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.SoundUtil;

/**
 * GameActor
 * @name : actor's name , used to load the corresponding resource
 * @direction : actor's direction
 * @interval : animation update frequency
 * @frameIndex : animation frame index
 * @frameCount : animation frame count
 * @frames : texture's regions
 */

public class GameActor extends GameObject {
    final public GameActorType type;
    final private GameActorProperties properties;

    private Direction direction;
    private TextureRegion[][] frames;
    private Interval interval;
    private Speed speed;

    private int frame = 0;
    private int culmuSteps = 0;

    public GameActor(GameStage stage, Owner owner, int id, String source, String name,
            GameActorType type, TextureRegion[][] frames, float sx, float sy) throws Exception {
        super(stage, owner, source, name, id, sx, sy);
        this.type = type;
        this.properties = new GameActorProperties(type.prop);
        this.frames = frames;
        this.direction = Direction.HOLD_DEF;
        this.interval = Interval.HIGH;
        this.speed = Speed.VERYFAST;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[this.direction.val][this.frame / this.interval.val], this.getX(),
                this.getY());
        batch.setColor(color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.frame = (this.frame < this.interval.val * GameConstants.CHR_HSLICES - 1) ? this.frame + 1
                : 0;
    }

    @Override
    public void onAttacked(Attack attack) {
        this.decreaseHP(attack.getATK());
        System.out.println("onAttacked(" + attack.getATK() + "): " + this.getName() + " : "
                + this.getProp().hp);

    }

    @Override
    public boolean onInteract(GameObject contacter) {
        System.out.printf("%s contact to %s\n", contacter.getName(), this.getName());
        return false;
    }

    @Override
    public void onSelected(Owner owner) {
        SoundUtil.playSE(GameConstants.ONSELECT_SE);
    }

    @Override
    public void attack(int x, int y) {
        // Suicide avoiding
        if (this.vx == x && this.vy == y)
            return;

        // Check in-range
        if (this.isInRange(x, y)) {
            this.stage.emitAttackEvent(this, this.type.getAttackMode(), x, y);
        }
    }

    @Override
    public void interact(int x, int y) {
        if (this.isInRange(1, x, y)) {
            this.stage.emitInteractEvent(this, x, y);
        }
    }

    @Override
    public void skill(int x, int y) {
        if (this.isInRange(this.type.getSkill()
                .getRange(), x, y)) {
            System.out.println(this.getName() + " emit skill at " + x + " , " + y);
            this.stage.emitSkillEvent(this, this.type.getSkill(), x, y);
        }

    }

    @Override
    public void onSkill(Skill skill) {
        System.out.println("onSkill: " + skill.animMeta.source);
    }

    /**
     * User interface
     */
    public void moveDown(int pace) {
        this.turn(Direction.DOWN);
        this.move(Direction.DOWN, pace);
    }

    public void moveLeft(int pace) {
        this.turn(Direction.LEFT);
        ;
        this.move(Direction.LEFT, pace);
    }

    public void moveRight(int pace) {
        this.turn(Direction.RIGHT);
        this.move(Direction.RIGHT, pace);
    }

    public void moveUp(int pace) {
        this.turn(Direction.UP);
        this.move(Direction.UP, pace);
    }

    public void move(Direction direction, int pace) {
        // Check steps
        int checkedPace = this.pathCheck(direction, pace);
        this.culmuSteps += checkedPace;
        if (this.culmuSteps >= this.properties.maxsteps) {
            checkedPace -= this.culmuSteps - this.properties.maxsteps;
        }

        int nx = (int) (this.vx + direction.udx * checkedPace);
        int ny = (int) (this.vy + direction.udy * checkedPace);

        this.updateVirtualMap(this, nx, ny);

        this.stage.addAnimation(new GameActorMovementAnimation(this.stage, this, direction,
                checkedPace));
    }

    private void turn(Direction direction) {
        this.stage.addAnimation(new GameActorTurnAnimation(this, direction));
    }

    /**
     * Check the path along a specific direction and return how far this actor can move until blocked
     * @param direction
     * @param pace
     * @return
     */
    public int pathCheck(Direction direction, int pace) {
        int step;
        for (step = 1; step <= pace && isPassiable(direction, step); step++)
            ;
        step--;

        return step;
    }

    @Override
    public boolean isPassiable(int x, int y) {
        if (!this.isInbounding(x, y))
            return false;
        return this.stage.getVirtualMap()
                .getVirtualCells()[y][x].isPassible();
    }

    public boolean isPassiable(Direction direction, int step) {
        int x = (int) (this.vx + direction.udx * step);
        int y = (int) (this.vy + direction.udy * step);

        return isPassiable(x, y);
    }

    public boolean isInRange(int x, int y) {
        return isInRange(this.properties.range, x, y);
    }

    public boolean isInRange(int range, int x, int y) {
        int distance = (int) Math.sqrt(Math.pow(x - vx, 2) + Math.pow(y - vy, 2));
        return (distance <= range) ? true : false;
    }

    /**
     * Getters
     */
    public Direction getDirection() {
        return this.direction;
    }

    public Speed getSpeed() {
        return this.speed;
    }

    public Interval getInterval() {
        return this.interval;
    }

    public int getFrame() {
        return this.frame;
    }

    public GameActorProperties getProp() {
        return this.properties;
    }

    /**
     * Setters
     */
    public void setFrame(int frame) {
        this.frame = (frame < GameConstants.CHR_HSLICES) ? frame : 0;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public void setCulmuSteps(int steps) {
        this.culmuSteps = steps;
    }

    public void resetCulmuSteps() {
        this.setCulmuSteps(0);
    }

    public int increaseHP(int diff) {
        int newValue = (this.properties.hp + diff);
        int max = GameActorFactory.getInstance()
                .getGameActorType(this).prop.hp;
        this.properties.hp = (newValue >= max) ? max : newValue;
        return this.properties.hp;
    }

    public int decreaseHP(int diff) {
        int newValue = this.properties.hp - diff;
        this.properties.hp = (newValue >= 0) ? newValue : 0;
        if (this.properties.hp == 0) {
            this.state = GameObjectState.DEATH;
            System.out.println(this.getName() + " is dead.");
        }

        return this.properties.hp;
    }

    @Override
    public boolean isBlock() {
        return (this.state == GameObjectState.DEATH) ? true : false;
    }
}
