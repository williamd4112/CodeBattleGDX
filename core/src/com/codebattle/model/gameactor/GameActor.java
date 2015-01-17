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
import com.codebattle.model.animation.TargetBasedAnimation;
import com.codebattle.model.levelobject.ScriptableObject;
import com.codebattle.model.meta.Attack;
import com.codebattle.model.meta.GameObjectType;
import com.codebattle.model.meta.Skill;
import com.codebattle.model.units.Interval;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameMethods;
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
    private final TextureRegion[][] frames;
    private final Interval interval;
    private int frame = 0;

    private String alias;

    public GameActor(final GameStage stage, final Owner owner, final int id,
            final String source, final String name,
            final GameObjectType type, final TextureRegion[][] frames, final float sx,
            final float sy)
            throws Exception {
        super(stage, owner, source, name, id, type, sx, sy, type.prop.maxsteps);
        this.frames = frames;
        this.interval = Interval.HIGH;
        this.alias = this.getName();
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        final Color color = batch.getColor();
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, parentAlpha
                * this.getColor().a);
        batch.draw(this.frames[this.direction.val][this.frame / this.interval.val],
                this.getX(), this.getY());
        batch.setColor(color);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        this.frame =
                this.frame < this.interval.val * GameConstants.CHR_HSLICES - 1 ? this.frame + 1
                        : 0;
    }

    @Override
    public void onAttacked(final Attack attack) {
        this.stage.addAnimation(new OnAttackAnimation(this));
        this.decreaseHP(attack.getATK());
        System.out.println("onAttacked(" + attack.getATK() + "): " + this.getName() + " : "
                + this.getProp().hp);
    }

    @Override
    public boolean onInteract(final GameObject contacter) {
        System.out.printf("%s contact to %s\n", contacter.getName(), this.getName());
        return false;
    }

    @Override
    public void onSelected(final Owner owner) {
        SoundUtil.playSES(this.type.getSelectSoundNames());
        SoundUtil.playSE(GameConstants.ONSELECT_SE);
    }

    @Override
    public void attack(final int x, final int y) {
        if (!this.checkOperation()) {
            return;
        }
        this.addOperation();
        // Suicide avoiding
        if (this.vx == x && this.vy == y) {
            return;
        }

        // Check in-range
        if (this.isInRange(x, y) && this.isOutMinRange(this.properties.minrange, x, y)) {
            this.stage.emitAttackEvent(this, this.type.getAttackMode(), x, y);
        }
    }

    @Override
    public void interact(final int x, final int y) {
        if (this.isInRange(1, x, y)) {
            this.stage.emitInteractEvent(this, x, y);
        }
    }

    public void writeCell(final int x, final int y, final String type, final String script) {
        try {
            if (!this.checkOperation()) {
                return;
            }
            this.addOperation();
            if (!this.levelCheck(GameConstants.WRITE_LEVEL)) {
                return;
            }
            if (!this.mpCheck(GameConstants.WRITE_COST)) {
                return;
            }
            if (this.isInRange(this.type.prop.range, x, y)) {
                final VirtualCell cell = this.stage.getVirtualMap().getCell(x, y);
                if (cell != null) {
                    cell.setScript(type, script);
                    final TargetBasedAnimation anim = new TargetBasedAnimation(this.stage,
                            GameConstants.WRITE_ANIMMETA, cell, 0.3f);
                    anim.setEmitter(this);
                    anim.setEmitter(this);
                    anim.addSound(GameConstants.WRITE_SE);
                    this.stage.addAnimation(anim);

                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void writeObject(final int x, final int y, final String type, final String script) {
        try {
            if (!this.checkOperation()) {
                return;
            }
            this.addOperation();
            if (!this.levelCheck(GameConstants.WRITE_LEVEL)) {
                return;
            }
            if (!this.mpCheck(GameConstants.WRITE_COST)) {
                return;
            }
            if (this.isInRange(this.type.prop.range, x, y)) {
                final VirtualCell cell = this.stage.getVirtualMap().getCell(x, y);
                final GameObject obj = cell.getObject();
                if (obj != null) {
                    if (obj instanceof ScriptableObject) {
                        ((ScriptableObject) obj).setScript(type, script);
                        final TargetBasedAnimation anim =
                                new TargetBasedAnimation(this.stage,
                                        GameConstants.WRITE_ANIMMETA, obj, 0.3f);
                        anim.setEmitter(this);
                        anim.addSound(GameConstants.WRITE_SE);
                        anim.setEmitter(this);
                        this.stage.addAnimation(anim);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void skill(final int x, final int y) {
        if (!this.checkOperation()) {
            return;
        }
        this.addOperation();
        if (!this.mpCheck(this.type.getSkill().getCost())) {
            return;
        }
        if (this.isInRange(this.type.getSkill().getRange(), x, y)) {
            System.out.println(this.getName() + " emit skill at " + x + " , " + y);
            try {
                this.stage.addAnimation(new PortraitAnimation(this.stage, this));
                this.stage.addAnimation(new SkillAnimation(this.stage, this.type.getSkill(),
                        this));
                this.stage.emitSkillEvent(this, this.type.getSkill(), x, y);
                this.decreaseMP(this.type.getSkill().getCost());
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void heal(final int x, final int y, final int diff) {
        if (!this.checkOperation()) {
            return;
        }
        this.addOperation();
        if (!this.levelCheck(GameConstants.HEAL_LEVEL)) {
            return;
        }
        try {
            GameMethods.heal(this.stage, this, diff, x, y).setEmitter(this);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void encharge(final int x, final int y, final int diff) {
        if (!this.checkOperation()) {
            return;
        }
        this.addOperation();
        if (!this.levelCheck(GameConstants.HEAL_LEVEL)) {
            return;
        }
        try {
            GameMethods.encharge(this.stage, this, diff, x, y).setEmitter(this);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSkill(final Skill skill, final GameObject emitter) {
        System.out.println("onSkill: " + skill.animMeta.source);
        skill.execute(this, emitter, this.vx, this.vy);
    }

    @Override
    public void onDestroyed() {
        System.out.println(this.getName() + " is dead.");
        this.stage.getVirtualSystem(this.owner).decreaseLift(this.type.prop.hp);
    }

    public boolean isInRange(final int x, final int y) {
        return this.isInRange(this.properties.range, x, y);
    }

    public boolean isInRange(final int range, final int x, final int y) {
        final int distance =
                (int) Math.sqrt(Math.pow(x - this.vx, 2) + Math.pow(y - this.vy, 2));
        return distance <= range ? true : false;
    }

    public boolean isOutMinRange(final int range, final int x, final int y) {
        final int distance =
                (int) Math.sqrt(Math.pow(x - this.vx, 2) + Math.pow(y - this.vy, 2));
        return distance >= range ? true : false;
    }

    private boolean levelCheck(final int needLevel) {
        return this.type.getLevel() >= needLevel;
    }

    private boolean mpCheck(final int needMp) {
        return this.properties.mp >= needMp;
    }

    @Override
    public boolean isBlock() {
        return this.state == GameObjectState.DEATH ? false : true;
    }

    @Override
    public boolean isPassiable(final int x, final int y) {
        if (!this.isInbounding(x, y)) {
            return false;
        }
        if (this.type.through) {
            return true;
        }
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

    public String getAlias() {
        return this.alias;
    }

    public int getSteps() {
        return this.culmuSteps;
    }

    /**
     * Setters
     */
    public void setFrame(final int frame) {
        this.frame = frame < GameConstants.CHR_HSLICES ? frame : 0;
    }

    public void rename(final String alias) {
        this.alias = alias;
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
            final Texture texture = TextureFactory.getInstance().loadTextureFromFile(
                    this.source + "_portrait", ResourceType.PORTRAIT);
            final Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
            return drawable;
        } catch (final Exception e) {
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
        return String.format("(%d , %d)", this.vx, this.vy);
    }
}
