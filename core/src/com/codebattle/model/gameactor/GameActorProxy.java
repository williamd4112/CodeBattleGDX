package com.codebattle.model.gameactor;

import com.codebattle.utility.GameUtil;

public class GameActorProxy {
    final private GameActor actor;
    private String alias;

    public GameActorProxy(GameActor actor) {
        this.actor = actor;
        this.alias = actor.getAlias();
    }

    public void moveLeft(int step) {
        this.actor.moveLeft(step);
    }

    public void moveRight(int step) {
        this.actor.moveRight(step);
    }

    public void moveUp(int step) {
        this.actor.moveUp(step);
    }

    public void moveDown(int step) {
        this.actor.moveDown(step);
    }

    public void attack(int x, int y) {
        actor.attack(x, y);
    }

    public void attackUp(int step) {
        actor.attack(actor.getVX(), actor.getVY() + step);
    }

    public void attackDown(int step) {
        actor.attack(actor.getVX(), actor.getVY() - step);
    }

    public void attackLeft(int step) {
        actor.attack(actor.getVX() - step, actor.getVY());
    }

    public void attackRight(int step) {
        actor.attack(actor.getVX() + step, actor.getVY());
    }

    public void writeCell(int x, int y, String type, String script) {
        actor.writeCell(x, y, type, script);
    }

    public void writeObject(int x, int y, String type, String script) {
        actor.writeObject(x, y, type, script);
    }

    public void skill(int x, int y) {
        actor.skill(x, y);
    }

    public void interact(int x, int y) {
        actor.interact(x, y);
    }

    public boolean isPassiable(int x, int y) {
        return actor.isPassiable(x, y);
    }

    public boolean isPassable(String dir, int step) {
        return actor.isPassiable(GameUtil.toDirection(dir), step);
    }

    public String getAlias() {
        return this.alias;
    }

    public void rename(String alias) {
        this.actor.rename(alias);
    }
}
