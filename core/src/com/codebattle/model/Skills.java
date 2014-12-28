package com.codebattle.model;

import com.codebattle.model.gameactor.GameActor;

public class Skills {
    public static void increaseHP(GameActor actor, int diff) {
        actor.increaseHP(diff);
    }

    public static void decreaseHP(GameActor actor, int diff) {
        actor.decreaseHP(diff);
    }

    public static void poison(GameActor actor) {
        actor.setState(GameObjectState.POISIONED);
    }

    public static void areaAttack(GameActor attacker, int range) {
        int leftTopX = attacker.getVX() - range, leftTopY = attacker.getVY() + range;
        int rightBottomX = attacker.getVX() + range, rightBottomY = attacker.getVY() - range;
        for (int y = leftTopY; y >= rightBottomY; y--) {
            for (int x = leftTopX; x <= rightBottomX; x++) {
                attacker.attack(x, y);
            }
        }
    }
}
