package com.codebattle.model;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Input;
import com.codebattle.gui.GameDialog;

public class GameDialogQueue {
    final public GameStage stage;
    private Queue<GameDialog> queue;
    private GameDialog currentDialog = null;

    public GameDialogQueue(GameStage stage) {
        this.stage = stage;
        this.queue = new LinkedList<GameDialog>();
    }

    public void add(GameDialog dlg) {
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

    public void resize(int width, int height) {
        for (GameDialog dlg : this.queue)
            dlg.resize(width, height);
    }

    public boolean keyDown(int keycode) {
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
