package com.codebattle.model.startup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.scene.SinglePlayerGameScene;
import com.codebattle.scene.StartupScene;

public class TutorialListStage extends SelectionStage {

    private Object[] options = { "tutorial1", "tutorial2" };
    private ButtonHandler handler;

    public TutorialListStage(StartupScene parent) {
        super(parent);
        this.list.setItems(options);
        this.handler = new ButtonHandler();
        this.btn_select.addListener(handler);

    }

    private class ButtonHandler extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == btn_select) {
                try {
                    parent.getParent()
                            .setScene(
                                    new SinglePlayerGameScene(parent.getParent(),
                                            list.getSelected()
                                                    .toString()));
                    TutorialListStage.this.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
