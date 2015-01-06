package com.codebattle.scene;

import java.net.Socket;

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

public class MultiPlayerGameScene extends SinglePlayerGameScene implements PeerListener {

    final public Client client;
    final public MultiPlayerRoom room;

    private Owner localPlayer;

    private MultiPlayerExtend extendView;
    private MultiPlayerEditorHandler multiPlayerHandler;

    public MultiPlayerGameScene(CodeBattle parent, MultiPlayerRoom room, String sceneName,
            String team) throws Exception {
        super(parent, sceneName);
        this.room = room;
        this.client = NetworkManager.getInstance().getClient();
        this.client.addPeerListener(this);
        this.localPlayer = GameUtil.toOwner(team);

        this.extendView = new MultiPlayerExtend(GameConstants.DEFAULT_SKIN);
        this.multiPlayerHandler = new MultiPlayerEditorHandler();
        this.gui.getEditor().addHandler(multiPlayerHandler);

        this.stage.addGUI(extendView);

        this.gui.getEditor().setDisable(!this.isLocalTurn());
        this.extendView.setTeamLabel(team, GameUtil.ownerToString(currentPlayer));
    }

    @Override
    public void onStageComplete(Owner winner) {

    }

    @Override
    public void onRoundComplete() {
        this.switchPlayer();
        if (this.isLocalTurn())
            this.gui.getEditor().setDisable(false);
    }

    public void switchPlayer() {
        if (this.currentPlayer == Owner.RED)
            this.currentPlayer = Owner.BLUE;
        else
            this.currentPlayer = Owner.RED;
    }

    public boolean isLocalTurn() {
        return (this.localPlayer == this.currentPlayer);
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        // create("Game", "Close", data);
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                try {
                    Message msg = new Message(rawMessage);
                    if (msg.type.equals("Game")) {
                        if (msg.opt.equals("Script")) {
                            String script = (String) msg.data;
                            onReceiveScript(script);
                        } else if (msg.opt.equals("Close")) {
                            backtoRoom();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onConnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    public void backtoRoom() {
        StartupScene startup = new StartupScene(parent);
        client.popPeerListener();
        room.getPlayerState();
        Gdx.input.setInputProcessor(room);
        startup.setStage(room);
        parent.setScene(startup);
        dispose();
    }

    private class MultiPlayerEditorHandler extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            gui.getEditor().setDisable(true);
            String script = gui.getEditor().getText();

            if (script != null)
                client.send(DataHandler.script(script).toString());
        }

    }

    private class MultiPlayerExtend extends Table implements Resizeable {
        private Label team_label;

        public MultiPlayerExtend(Skin skin) {
            super();
            this.team_label = new Label(GameUtil.ownerToString(localPlayer), skin);
            this.setFillParent(true);
            this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void resize(int width, int height) {
            this.reset();
            this.setFillParent(true);
            this.add(team_label).expand().top();
        }

        public void setTeamLabel(String team, String current) {
            this.team_label.setText(team + " / " + current);
        }
    }
}
