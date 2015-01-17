package com.codebattle.model.levelobject;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.animation.FrameTimer;
import com.codebattle.model.animation.OnAttackAnimation;
import com.codebattle.model.animation.Oscillator;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.meta.Skill;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameMethods;
import com.codebattle.utility.GameUtil;
import com.codebattle.utility.SoundUtil;

public class LevelObject extends ScriptableObject implements StateShowable {

    private final TextureRegion[] frames;
    private final FrameTimer timer;

    // Optional
    private PointLight light = null;
    private PointLightMeta lightMeta = null;
    private Oscillator oscillator = null;

    public LevelObject(final GameStage stage, final Owner owner, final String source,
            final String name, final int id,
            final GameObjectType type, final TextureRegion[] frames,
            final PointLightMeta lightMeta, final float sx,
            final float sy, final int maxsteps, final String readonlyScript,
            final boolean isFixed) {
        super(stage, owner, source, name, id, type, sx, sy, maxsteps, readonlyScript, isFixed);
        this.frames = frames;
        this.timer = new FrameTimer(10, frames.length - 1);

        // Optional light
        if (lightMeta != null) {
            this.lightMeta = lightMeta;
            lightMeta.x += sx;
            lightMeta.y += sy;

            this.light = this.stage.addPointLight(lightMeta);
            this.oscillator = new Oscillator(10, 0.08f);
        }

    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        this.timer.act();

        if (this.light != null) {
            this.light.setDistance(this.lightMeta.radius + this.oscillator.getValue());
            this.light.setPosition(this.getCenterX(), this.getCenterY());
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        final Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[this.timer.getFrame()], this.getX(), this.getY());
        batch.setColor(color);
    }

    @Override
    public void attack(final int x, final int y) {
        // Suicide avoiding
        if (this.vx == x && this.vy == y) {
            return;
        }

        // Check in-range
        System.out.printf("attack@%s: ", this.getName());
        if (this.isInRange(x, y)) {
            this.stage.emitAttackEvent(this, this.type.getAttackMode(), x, y);
        }
    }

    @Override
    public void skill(final int x, final int y) {
        // TODO Auto-generated method stub

    }

    public boolean isInRange(final int x, final int y) {
        return this.isInRange(this.properties.range, x, y);
    }

    public boolean isInRange(final int range, final int x, final int y) {
        final int distance =
                (int) Math.sqrt(Math.pow(x - this.vx, 2) + Math.pow(y - this.vy, 2));
        return distance <= range ? true : false;
    }

    @Override
    public void interact(final int x, final int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSelected(final Owner owner) {
        SoundUtil.playSE(GameConstants.ONSELECT_SE);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onAttacked(final Attack attack) {
        this.stage.addAnimation(new OnAttackAnimation(this));
        this.transmitDamage(attack.getATK());
    }

    @Override
    public void onSkill(final Skill skill, final GameObject emitter) {
        // System.out.println("onSkill: " + skill.animMeta.source);
        skill.execute(this, emitter, this.vx, this.vy);
    }

    public void setOwner(final String owner) {
        this.owner = GameUtil.toOwner(owner);
    }

    public void generateResource() {
        this.stage.getVirtualSystems()[this.owner.index].increaseResource(
                GameConstants.VIRTUAL_SYSTEM_RESOURCE_ADDING, this);
    }

    public void transmitDamage(final int damage) {
        this.stage.getVirtualSystem(this.owner).decreaseLift(damage);
    }

    public void scan() {
        final int lbx = this.vx - this.properties.range, lby =
                this.vy - this.properties.range;
        final int rtx = this.vx + this.properties.range, rty =
                this.vy + this.properties.range;
        for (int y = lby; y < rty; y++) {
            for (int x = lbx; x < rtx; x++) {
                this.attack(x, y);
            }
        }
    }

    public void heal(final int val) {
        final int leftTopX = this.vx - this.properties.range;
        final int leftTopY = this.vy + this.properties.range;
        final int rightBottomX = this.vx + this.properties.range;
        final int rightBottomY = this.vy - this.properties.range;
        for (int y = leftTopY; y > rightBottomY; y--) {
            for (int x = leftTopX; x < rightBottomX; x++) {
                if (x == this.vx && y == this.vy) {
                    continue;
                }
                GameMethods.heal(this.stage, this, val, x, y);
            }
        }
    }

    public void encharge(final int val) {
        final int leftTopX = this.vx - this.properties.range;
        final int leftTopY = this.vy + this.properties.range;
        final int rightBottomX = this.vx + this.properties.range;
        final int rightBottomY = this.vy - this.properties.range;
        for (int y = leftTopY; y > rightBottomY; y--) {
            for (int x = leftTopX; x < rightBottomX; x++) {
                if (x == this.vx && y == this.vy) {
                    continue;
                }
                GameMethods.encharge(this.stage, this, val, x, y);
            }
        }
    }

    @Override
    public String[] getKeys() {

        return new String[] { "HP", "MP", "ATK", "DEF", "RANGE" };
    }

    @Override
    public String[] getValues() {

        return new String[] { String.valueOf(this.properties.hp),
                String.valueOf(this.properties.mp), String.valueOf(this.properties.atk),
                String.valueOf(this.properties.def), String.valueOf(this.properties.range) };
    }

    @Override
    public Drawable getPortrait() {
        return null;
    }

    @Override
    public String getNameInfo() {
        return String.format("%s(%s)", this.getName(), this.properties.typeName);
    }

    @Override
    public String getPositionInfo() {
        return String.format("(%d , %d)", this.vx, this.vy);
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onInteract(final GameObject contacter) {
        // TODO Auto-generated method stub
        return false;
    }

}
