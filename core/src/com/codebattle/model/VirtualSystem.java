package com.codebattle.model;

import com.badlogic.gdx.utils.Array;
import com.codebattle.model.animation.SummonAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameObjectFactory;
import com.codebattle.utility.SoundUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * In Game System
 * player can use this object to emulate os
 * 1.generate gameobjects
 * 2.store file
 * 3.write file
 * 4.manage player hp and cost
 * @author williamd
 *
 */
public class VirtualSystem {
    final private GameStage stage;
    final public Owner owner;

    private int hp;
    private int resource;

    private final List<SystemListener> listeners;

    public VirtualSystem(final GameStage stage, final Owner owner) {
        this.listeners = new LinkedList<SystemListener>();
        this.stage = stage;
        this.hp = GameConstants.INIT_HP;
        this.resource = GameConstants.INIT_RES;
        this.owner = owner;
    }

    public void createGameActor(final String source, final String type, final int vx,
            final int vy) throws Exception {
        if (this.stage.isOutBoundInVirtualMap(vx, vy)) {
            return;
        }
        if (!this.stage.getVirtualMap().getCell(vx, vy).isPassible()
                || this.stage.getVirtualMap().getCell(vx, vy).getObject() != null) {
            return;
        }

        final int x = GameConstants.CELL_SIZE * vx, y = GameConstants.CELL_SIZE * vy;
        final GameActor actor =
                GameObjectFactory.getInstance().createGameActor(this.stage, this.owner,
                        source, type, x, y);
        this.stage.addGameObject(actor);
        this.decreaseResource(actor.type.cost);

        final SummonAnimation anim = new SummonAnimation(this.stage, actor);
        anim.addSound(GameConstants.SUMMON_SE);
        for (final String s : actor.type.getSelectSoundNames()) {
            anim.addSound(s);
        }
        this.stage.addAnimation(anim);

        System.out.println("system@" + this.owner + ": createGameActor " + actor.getName());
    }

    public int getHP() {
        return this.hp;
    }

    public int getResource() {
        return this.resource;
    }

    public Array<GameActor> getSortedActorList() {
        return this.stage.getGameActorsByOwner(this.owner);
    }

    public void increaseResource(final int diff) {
        this.resource += diff;
        this.emitResourceChange();
    }

    public void increaseResource(final int diff, final GameObject source) {
        try {
            this.increaseResource(diff);
            this.stage.addAnimation(new SummonAnimation(this.stage, source));
            SoundUtil.playSE(GameConstants.SUMMON_SE);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void decreaseResource(final int diff) {
        final int newValue = this.resource - diff;
        this.resource = newValue <= 0 ? 0 : newValue;
        this.emitResourceChange();
    }

    public void decreaseLift(final int diff) {
        final int newValue = this.hp - diff;
        this.hp = newValue <= 0 ? 0 : newValue;
        this.emitLifeChange();
    }

    public void emitLifeChange() {
        for (final SystemListener listener : this.listeners) {
            listener.onLifeChange(this.hp);
        }
        System.out.println("system@" + this.owner + " : " + this.hp);
    }

    public void emitResourceChange() {
        for (final SystemListener listener : this.listeners) {
            listener.onResourceChange(this.resource);
        }
    }

    public void addSystemListener(final SystemListener listener) {
        this.listeners.add(listener);
    }
}
