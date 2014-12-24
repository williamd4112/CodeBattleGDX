package com.codebattle.utility;

import com.codebattle.model.Owner;

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
}
