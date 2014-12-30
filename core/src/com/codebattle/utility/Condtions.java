package com.codebattle.utility;

import com.codebattle.model.GameObject;
import com.codebattle.model.GameStage;
import com.codebattle.model.Owner;
import com.codebattle.model.meta.Bundle;

public class Condtions {

    public static boolean whenPlayerResourceUnder(Bundle args) {
        GameStage stage = args.extract("Stage", GameStage.class);
        Owner owner = GameUtil.toOwner(args.extract("Player", String.class));
        int threshold = Integer.parseInt(args.extract("Threshold", String.class));
        if (stage.getVirtualSystems()[owner.index].getResource() < threshold) {
            return true;
        }
        return false;
    }

    public static boolean isObjectDestroyed(Bundle args) {
        GameStage stage = args.extract("Stage", GameStage.class);
        String targetName = args.extract("TargetName", String.class);
        Owner owner = GameUtil.toOwner(args.extract("Owner", String.class));

        GameObject target = stage.findGameObjectByNameAndOwner(targetName, owner);
        if (target != null) {
            return (target.isAlive()) ? false : true;
        }
        return true;
    }
}
