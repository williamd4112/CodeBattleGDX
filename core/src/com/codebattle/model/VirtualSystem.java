package com.codebattle.model;

import java.util.LinkedList;
import java.util.List;

import com.codebattle.model.animation.SummonAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameObjectFactory;
import com.codebattle.utility.SoundUtil;

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

    private List<SystemListener> listeners;

    public VirtualSystem(GameStage stage, Owner owner) {
        this.listeners = new LinkedList<SystemListener>();
        this.stage = stage;
        this.hp = GameConstants.INIT_HP;
        this.resource = GameConstants.INIT_RES;
        this.owner = owner;
    }

    public void createGameActor(String source, String type, int vx, int vy) throws Exception {
        if (stage.isOutBoundInVirtualMap(vx, vy))
            return;
        if (!stage.getVirtualMap().getCell(vx, vy).isPassible()
                || stage.getVirtualMap().getCell(vx, vy).getObject() != null)
            return;

        int x = GameConstants.CELL_SIZE * vx, y = GameConstants.CELL_SIZE * vy;
        GameActor actor = GameObjectFactory.getInstance().createGameActor(stage, owner,
                source, type, x, y);
        this.stage.addGameObject(actor);
        this.decreaseResource(actor.type.cost);

        SummonAnimation anim = new SummonAnimation(this.stage, actor);
        this.stage.addAnimation(anim);

        SoundUtil.playSE(GameConstants.SUMMON_SE);
        SoundUtil.playSES(actor.type.getSelectSoundNames());
        System.out.println("system@" + this.owner + ": createGameActor " + actor.getName());
    }

    public int getHP() {
        return this.hp;
    }

    public int getResource() {
        return this.resource;
    }

    public void increaseResource(int diff) {
        this.resource += diff;
        this.emitResourceChange();
    }

    public void increaseResource(int diff, GameObject source) {
        try {
            this.increaseResource(diff);
            this.stage.addAnimation(new SummonAnimation(stage, source));
            SoundUtil.playSE(GameConstants.SUMMON_SE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decreaseResource(int diff) {
        int newValue = (this.resource - diff);
        this.resource = (newValue <= 0) ? 0 : newValue;
        this.emitResourceChange();
    }

    public void decreaseLift(int diff) {
        int newValue = (this.hp - diff);
        this.hp = (newValue <= 0) ? 0 : newValue;
        this.emitLifeChange();
    }

    public void emitLifeChange() {
        for (SystemListener listener : this.listeners)
            listener.onLifeChange(this.hp);
        System.out.println("system@" + owner + " : " + this.hp);
    }

    public void emitResourceChange() {
        for (SystemListener listener : this.listeners)
            listener.onResourceChange(this.resource);
    }

    public void addSystemListener(SystemListener listener) {
        this.listeners.add(listener);
    }
}
