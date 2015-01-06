package com.codebattle.utility;

import com.codebattle.model.Owner;
import com.codebattle.model.units.Direction;

public class GameUtil {

    /**
     * Convert string owner to enum owner
     * @param owner
     * @return
     */
    public static Owner toOwner(final String owner) {
        if (owner.equals("Red")) {
            return Owner.RED;
        } else if (owner.equals("Blue")) {
            return Owner.BLUE;
        } else {
            return Owner.GREEN;
        }
    }

    public static String ownerToString(Owner owner) {
        switch (owner) {
        case RED:
            return "Red";
        case BLUE:
            return "Blue";
        case GREEN:
            return "Green";
        default:
            return null;
        }
    }

    public static Direction toDirection(String dir) {
        if (dir.equals("Right") || dir.equals("R")) {
            return Direction.RIGHT;
        } else if (dir.equals("Left") || dir.equals("L")) {
            return Direction.LEFT;
        } else if (dir.equals("Up") || dir.equals("U")) {
            return Direction.UP;
        } else
            return Direction.UP;
    }
}
