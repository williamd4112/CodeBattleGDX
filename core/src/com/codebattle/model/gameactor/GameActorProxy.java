package com.codebattle.model.gameactor;

import com.codebattle.utility.GameUtil;

public class GameActorProxy {
    final private GameActor actor;
    private final String alias;

    public GameActorProxy(final GameActor actor) {
        this.actor = actor;
        this.alias = actor.getAlias();
    }

    public void moveLeft(final int step) {
        this.actor.moveLeft(step);
    }

    public void moveRight(final int step) {
        this.actor.moveRight(step);
    }

    public void moveUp(final int step) {
        this.actor.moveUp(step);
    }

    public void moveDown(final int step) {
        this.actor.moveDown(step);
    }

    public void attack(final int x, final int y) {
        this.actor.attack(x, y);
    }

    public void attackUp(final int step) {
        this.actor.attack(this.actor.getVX(), this.actor.getVY() + step);
    }

    public void attackDown(final int step) {
        this.actor.attack(this.actor.getVX(), this.actor.getVY() - step);
    }

    public void attackLeft(final int step) {
        this.actor.attack(this.actor.getVX() - step, this.actor.getVY());
    }

    public void attackRight(final int step) {
        this.actor.attack(this.actor.getVX() + step, this.actor.getVY());
    }

    public void writeCell(final int x, final int y, final String type, final String script) {
        this.actor.writeCell(x, y, type, script);
    }

    public void writeObject(final int x, final int y, final String type, final String script) {
        this.actor.writeObject(x, y, type, script);
    }

    public void skill(final int x, final int y) {
        this.actor.skill(x, y);
    }

    public void heal(final int x, final int y, final int diff) {
        this.actor.heal(x, y, diff);
    }

    public void interact(final int x, final int y) {
        this.actor.interact(x, y);
    }

    public boolean isPassiable(final int x, final int y) {
        return this.actor.isPassiable(x, y);
    }

    public boolean isPassable(final String dir, final int step) {
        return this.actor.isPassiable(GameUtil.toDirection(dir), step);
    }

    public boolean confront(final int x, final int y) {
        return this.actor.stage.getVirtualMap().getCell(x, y).getObject() == null ? false
                : true;
    }

    public boolean isOverstep() {
        return this.actor.getSteps() >= this.actor.getProp().maxsteps;
    }

    public String getAlias() {
        return this.alias;
    }

    public void rename(final String alias) {
        this.actor.rename(alias);
    }
}
