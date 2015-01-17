package com.codebattle.model;

import com.badlogic.gdx.Input;
import com.codebattle.gui.GameDialog;

import java.util.LinkedList;
import java.util.Queue;

public class GameDialogQueue {
    final public GameStage stage;
    private final Queue<GameDialog> queue;
    private GameDialog currentDialog = null;

    public GameDialogQueue(final GameStage stage) {
        this.stage = stage;
        this.queue = new LinkedList<GameDialog>();
    }

    public void add(final GameDialog dlg) {
        this.queue.add(dlg);
        if (this.currentDialog == null) {
            this.stage.fadeOutLayer(this.stage.getGUILayer());
            this.nextDialog();
        }
    }

    public void nextDialog() {
        if (this.currentDialog != null) {
            this.currentDialog.callback();
            this.currentDialog.remove();
        }

        if (!this.queue.isEmpty()) {
            this.currentDialog = this.queue.poll();
            this.stage.addDialog(this.currentDialog);
        } else {
            this.currentDialog = null;
            this.stage.fadeInLayer(this.stage.getGUILayer());
        }
    }

    public void resize(final int width, final int height) {
        for (final GameDialog dlg : this.queue) {
            dlg.resize(width, height);
        }
    }

    public boolean keyDown(final int keycode) {
        switch (keycode) {
        case Input.Keys.ENTER:
            this.nextDialog();
            break;
        default:
            break;
        }

        return true;
    }
}
