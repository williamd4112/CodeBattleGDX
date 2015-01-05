package com.codebattle.scene;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.codebattle.game.CodeBattle;
import com.codebattle.model.Owner;
import com.codebattle.utility.GameUtil;

public class MultiPlayerGameScene extends SinglePlayerGameScene {

    private Owner localPlayer;

    public MultiPlayerGameScene(CodeBattle parent, String sceneName, String team)
            throws Exception {
        super(parent, sceneName);
        this.localPlayer = GameUtil.toOwner(team);
    }

    @Override
    public void onStageComplete(Owner winner) {
        this.stage.addAction(Actions.sequence(Actions.fadeOut(1.5f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        parent.setScene(new StartupScene(parent));

                    }

                })));
    }

    public void switchPlayer() {
        if (this.localPlayer == Owner.RED)
            this.localPlayer = Owner.BLUE;
        else
            this.localPlayer = Owner.RED;
    }
}
