package com.codebattle.scene;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.game.CodeBattle;

public class SinglePlayerGameScene extends PlayerGameScene {

    public SinglePlayerGameScene(final CodeBattle parent, final String sceneName)
            throws Exception {
        super(parent, sceneName);
    }

    @Override
    public void setupGUI() {
        super.setupGUI();
        this.handler = new Handler();
        this.gui.getEditor().addHandler(this.handler);
    }

    /* Handling script interpretation */
    private class Handler extends ClickListener {
        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            System.out.println("click");
            final String script = SinglePlayerGameScene.this.gui.getEditor().getText();
            SinglePlayerGameScene.this.onReceiveScript(script);
        }
    }

}
