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
import com.codebattle.utility.SoundUtil;

public class LevelObject extends ScriptableObject implements StateShowable {

    private TextureRegion[] frames;
    private FrameTimer timer;

    // Optional
    private PointLight light = null;
    private PointLightMeta lightMeta = null;
    private Oscillator oscillator = null;

    public LevelObject(GameStage stage, Owner owner, String source, String name, int id,
            GameObjectType type, TextureRegion[] frames, PointLightMeta lightMeta, float sx,
            float sy, int maxsteps) {
        super(stage, owner, source, name, id, type, sx, sy, maxsteps);
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
    public void act(float delta) {
        super.act(delta);
        this.timer.act();

        if (this.light != null) {
            this.light.setDistance(this.lightMeta.radius + oscillator.getValue());
            this.light.setPosition(this.getCenterX(), this.getCenterY());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[timer.getFrame()], this.getX(), this.getY());
        batch.setColor(color);
    }

    @Override
    public void attack(int x, int y) {
        // Suicide avoiding
        if (this.vx == x && this.vy == y)
            return;

        // Check in-range
        System.out.printf("attack@%s: ", this.getName());
        if (this.isInRange(x, y)) {
            this.stage.emitAttackEvent(this, this.type.getAttackMode(), x, y);
        }
    }

    @Override
    public void skill(int x, int y) {
        // TODO Auto-generated method stub

    }

    public boolean isInRange(int x, int y) {
        return isInRange(this.properties.range, x, y);
    }

    public boolean isInRange(int range, int x, int y) {
        int distance = (int) Math.sqrt(Math.pow(x - vx, 2) + Math.pow(y - vy, 2));
        return (distance <= range) ? true : false;
    }

    @Override
    public void interact(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSelected(Owner owner) {
        SoundUtil.playSE(GameConstants.ONSELECT_SE);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onAttacked(Attack attack) {
        this.stage.addAnimation(new OnAttackAnimation(this));
    }

    @Override
    public void onSkill(Skill skill, GameObject emitter) {
        System.out.println("onSkill: " + skill.animMeta.source);
        skill.execute(this, emitter);
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
        return String.format("(%d , %d)", vx, vy);
    }

    @Override
    public boolean isBlock() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onInteract(GameObject contacter) {
        // TODO Auto-generated method stub
        return false;
    }

}
