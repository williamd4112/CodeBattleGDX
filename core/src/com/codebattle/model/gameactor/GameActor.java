package com.codebattle.model.gameactor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.GameObject;
import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.MoveableGameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.VirtualCell;
import com.codebattle.model.animation.OnAttackAnimation;
import com.codebattle.model.animation.PortraitAnimation;
import com.codebattle.model.animation.SkillAnimation;
import com.codebattle.model.levelobject.ScriptableObject;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Interval;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameObjectFactory;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.SoundUtil;
import com.codebattle.utility.TextureFactory;

/**
 * GameActor
 * @name : actor's name , used to load the corresponding resource
 * @direction : actor's direction
 * @interval : animation update frequency
 * @frameIndex : animation frame index
 * @frameCount : animation frame count
 * @frames : texture's regions
 */

public class GameActor extends MoveableGameObject implements StateShowable {
    private TextureRegion[][] frames;
    private Interval interval;
    private int frame = 0;

    private String alias;

    public GameActor(GameStage stage, Owner owner, int id, String source, String name,
            GameObjectType type, TextureRegion[][] frames, float sx, float sy)
            throws Exception {
        super(stage, owner, source, name, id, type, sx, sy, type.prop.maxsteps);
        this.frames = frames;
        this.interval = Interval.HIGH;
        this.alias = this.getName();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[this.direction.val][this.frame / this.interval.val],
                this.getX(), this.getY());
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
        this.stage.addAnimation(new OnAttackAnimation(this));
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
        SoundUtil.playSES(type.getSelectSoundNames());
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

    public void writeCell(int x, int y, String type, String script) {
        VirtualCell cell = this.stage.getVirtualMap().getCell(x, y);
        if (cell != null) {
            cell.setScript(type, script);
        }
    }

    public void writeObject(int x, int y, String type, String script) {
        VirtualCell cell = this.stage.getVirtualMap().getCell(x, y);
        GameObject obj = cell.getObject();
        if (obj != null) {
            if (obj instanceof ScriptableObject) {
                ((ScriptableObject) obj).setScript(type, script);
            }
        }
    }

    @Override
    public void skill(int x, int y) {
        if (this.isInRange(this.type.getSkill().getRange(), x, y)) {
            System.out.println(this.getName() + " emit skill at " + x + " , " + y);
            try {
                this.stage.addAnimation(new PortraitAnimation(stage, this));
                this.stage.addAnimation(new SkillAnimation(stage, type.getSkill(), this));
                this.stage.emitSkillEvent(this, this.type.getSkill(), x, y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onSkill(Skill skill, GameObject emitter) {
        System.out.println("onSkill: " + skill.animMeta.source);
        skill.execute(this, emitter, this.vx, this.vy);
    }

    @Override
    public void onDestroyed() {
        System.out.println(this.getName() + " is dead.");
        this.stage.getVirtualSystem(owner).decreaseLift(this.type.prop.hp);
    }

    public boolean isInRange(int x, int y) {
        return isInRange(this.properties.range, x, y);
    }

    public boolean isInRange(int range, int x, int y) {
        int distance = (int) Math.sqrt(Math.pow(x - vx, 2) + Math.pow(y - vy, 2));
        return (distance <= range) ? true : false;
    }

    @Override
    public boolean isBlock() {
        return (this.state == GameObjectState.DEATH) ? false : true;
    }

    @Override
    public boolean isPassiable(int x, int y) {
        if (!this.isInbounding(x, y))
            return false;
        if (this.type.through)
            return true;
        return this.stage.getVirtualMap().getVirtualCells()[y][x].isPassible();
    }

    /**
     * Getters
     */
    public Interval getInterval() {
        return this.interval;
    }

    public int getFrame() {
        return this.frame;
    }

    public GameObjectProperties getProp() {
        return this.properties;
    }

    public String getAlias() {
        return this.alias;
    }

    /**
     * Setters
     */
    public void setFrame(int frame) {
        this.frame = (frame < GameConstants.CHR_HSLICES) ? frame : 0;
    }

    public void rename(String alias) {
        this.alias = alias;
    }

    public int increaseHP(int diff) {
        int newValue = (this.properties.hp + diff);
        int max = GameObjectFactory.getInstance().getGameObjectType(this).prop.hp;
        this.properties.hp = (newValue >= max) ? max : newValue;
        return this.properties.hp;
    }

    public int decreaseHP(int diff) {
        int newValue = this.properties.hp - diff;
        this.properties.hp = (newValue >= 0) ? newValue : 0;
        if (this.properties.hp == 0) {
            this.state = GameObjectState.DEATH;
        }

        return this.properties.hp;
    }

    /**
     * GUI Interface
     */
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
        try {
            Texture texture = TextureFactory.getInstance().loadTextureFromFile(
                    source + "_portrait", ResourceType.PORTRAIT);
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
            return drawable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
}
