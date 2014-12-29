package com.codebattle.model;

import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.GameConstants;

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

    public VirtualSystem(GameStage stage, Owner owner) {
        this.stage = stage;
        this.hp = GameConstants.INIT_HP;
        this.resource = GameConstants.INIT_RES;
        this.owner = owner;
    }

    public void createGameActor(String source, String type, int vx, int vy) throws Exception {
        int x = GameConstants.CELL_SIZE * vx, y = GameConstants.CELL_SIZE * vy;
        GameActor actor = GameActorFactory.getInstance()
                .createGameActor(stage, owner, source, type, x, y);
        this.stage.addGameObject(actor);
        System.out.println("system@" + this.owner + ": createGameActor " + actor.getName());
    }

    public int getHP() {
        return this.hp;
    }

    public int getResource() {
        return this.resource;
    }
}
