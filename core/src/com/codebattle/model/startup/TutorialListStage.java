package com.codebattle.model.startup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.scene.SinglePlayerGameScene;
import com.codebattle.scene.StartupScene;

public class TutorialListStage extends SelectionStage {

    private final Object[] options = { "tutorial1", "tutorial2" };
    private final ButtonHandler handler;

    public TutorialListStage(final StartupScene parent) {
        super(parent);
        this.list.setItems(this.options);
        this.handler = new ButtonHandler();
        this.btn_select.addListener(this.handler);

    }

    private class ButtonHandler extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == TutorialListStage.this.btn_select) {
                try {
                    TutorialListStage.this.parent.getParent()
                            .setScene(
                                    new SinglePlayerGameScene(
                                            TutorialListStage.this.parent.getParent(),
                                            TutorialListStage.this.list.getSelected()
                                                    .toString()));
                    TutorialListStage.this.dispose();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
