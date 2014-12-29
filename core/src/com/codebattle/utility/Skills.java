package com.codebattle.utility;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameObjectState;
import com.codebattle.model.GameStage;
import com.codebattle.model.animation.OnAttackAnimation;
import com.codebattle.model.animation.SkillAnimation;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.model.meta.Bundle;
import com.codebattle.model.meta.Skill;

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

    public static void areaAttack(Bundle args) {
        Skill skill = args.extract("Skill", Skill.class);
        GameStage stage = args.extract("Stage", GameStage.class);
        GameObject attacker = args.extract("Emitter", GameObject.class);
        int range = skill.getRange();
        String atk = args.extract("atk", String.class);

        int leftTopX = attacker.getVX() - range, leftTopY = attacker.getVY() + range;
        int rightBottomX = attacker.getVX() + range, rightBottomY = attacker.getVY() - range;
        for (int y = leftTopY; y >= rightBottomY; y--) {
            for (int x = leftTopX; x <= rightBottomX; x++) {
                if (x == attacker.getVX() && y == attacker.getVY())
                    continue;
                Bundle bundle = new Bundle();
                bundle.bind("Stage", stage);
                bundle.bind("Skill", skill);
                bundle.bind("x", x);
                bundle.bind("y", y);
                bundle.bind("atk", atk);
                skillAttack(bundle);
            }
        }
    }

    public static void skillAttack(Bundle args) {
        try {
            Skill skill = args.extract("Skill", Skill.class);
            GameObject emitter = args.extract("Emitter", GameObject.class);
            GameStage stage = args.extract("Stage", GameStage.class);
            int atk = Integer.parseInt(args.extract("atk", String.class));
            int vx = args.extract("x", int.class);
            int vy = args.extract("y", int.class);

            GameObject target = stage.getVirtualMap()
                    .getCell(vx, vy)
                    .getObject();
            if (target != null) {
                target.onSkillAttacked(atk);
                stage.addAnimation(new SkillAnimation(stage, skill, emitter, target));
                stage.addAnimation(new OnAttackAnimation(target));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
