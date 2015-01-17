package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.game.CodeBattle;
import com.codebattle.gui.Resizeable;
import com.codebattle.model.Owner;
import com.codebattle.model.startup.MultiPlayerRoom;
import com.codebattle.network.PeerListener;
import com.codebattle.network.client.Client;
import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameUtil;
import com.codebattle.utility.NetworkManager;

import java.net.Socket;

public class MultiPlayerGameScene extends PlayerGameScene implements PeerListener {

    final public Client client;
    final public MultiPlayerRoom room;

    private final Owner localPlayer;

    private final MultiPlayerExtend extendView;
    private MultiPlayerEditorHandler multiPlayerHandler;

    public MultiPlayerGameScene(final CodeBattle parent, final MultiPlayerRoom room,
            final String sceneName,
            final String team) throws Exception {
        super(parent, sceneName);
        this.room = room;
        this.client = NetworkManager.getInstance().getClient();
        this.client.addPeerListener(this);
        this.localPlayer = GameUtil.toOwner(team);

        this.extendView = new MultiPlayerExtend(GameConstants.DEFAULT_SKIN);

        this.stage.addGUI(this.extendView);

        this.gui.getEditor().setDisable(!this.isLocalTurn());
        this.extendView.setTeamLabel(team, GameUtil.ownerToString(this.currentPlayer));
    }

    @Override
    public void setupGUI() {
        super.setupGUI();
        this.multiPlayerHandler = new MultiPlayerEditorHandler();
        this.gui.getEditor().addHandler(this.multiPlayerHandler);
    }

    @Override
    public void onStageComplete(final Owner winner) {

    }

    @Override
    public void onRoundComplete() {
        // System.out.println("Result:" + this.isSuccess);
        if (!this.isSuccess) {
            return;
        }
        this.setSuccess(false);
        this.switchPlayer();
        if (this.isLocalTurn()) {
            this.gui.getEditor().setDisable(false);
        } else {
            this.gui.getEditor().setDisable(true);
        }
    }

    public void switchPlayer() {
        if (this.currentPlayer == Owner.RED) {
            this.currentPlayer = Owner.BLUE;
        } else {
            this.currentPlayer = Owner.RED;
        }
        this.extendView.setTeamLabel(GameUtil.ownerToString(this.localPlayer),
                GameUtil.ownerToString(this.currentPlayer));
    }

    public boolean isLocalTurn() {
        return this.localPlayer == this.currentPlayer;
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        // create("Game", "Close", data);
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                try {
                    final Message msg = new Message(rawMessage);
                    if (msg.type.equals("Game")) {
                        if (msg.opt.equals("Script")) {
                            final String script = (String) msg.data;
                            MultiPlayerGameScene.this.onReceiveScript(script);
                        } else if (msg.opt.equals("Close")) {
                            MultiPlayerGameScene.this.backtoRoom();
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onConnected(final Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(final Socket socket) {
        // TODO Auto-generated method stub

    }

    public void backtoRoom() {
        final StartupScene startup = new StartupScene(this.parent);
        this.client.popPeerListener();
        this.room.getPlayerState();
        Gdx.input.setInputProcessor(this.room);
        startup.setStage(this.room);
        this.parent.setScene(startup);
        this.dispose();
    }

    private class MultiPlayerEditorHandler extends ClickListener {
        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            System.out.println("submit");
            final String script = MultiPlayerGameScene.this.gui.getEditor().getText();
            final boolean result = MultiPlayerGameScene.this.onReceiveScript(script);
            MultiPlayerGameScene.this.setSuccess(result);
            if (result) {
                MultiPlayerGameScene.this.client.send(DataHandler.script(script).toString());
            }

        }

    }

    private class MultiPlayerExtend extends Table implements Resizeable {
        private final Label team_label;

        public MultiPlayerExtend(final Skin skin) {
            super();
            this.team_label =
                    new Label(GameUtil.ownerToString(MultiPlayerGameScene.this.localPlayer),
                            skin);
            this.setFillParent(true);
            this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void resize(final int width, final int height) {
            this.reset();
            this.setFillParent(true);
            this.add(this.team_label).expand().top();
        }

        public void setTeamLabel(final String team, final String current) {
            this.team_label.setText("Team: " + team + "  "
                    + (current.equals(team) ? "Your turn" : "Wait for opponent..."));
        }
    }
}
