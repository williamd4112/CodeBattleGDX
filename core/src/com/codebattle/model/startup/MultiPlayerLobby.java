package com.codebattle.model.startup;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.network.PeerListener;
import com.codebattle.network.client.Client;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;

public class MultiPlayerLobby extends Stage implements PeerListener {

    private Client client;

    private String[] rooms = { "room01", "room02", "room03", "room04", "room05", "room06" };

    final StartupScene parent;
    private Table guiTable;
    private List<Object> roomList;

    private HorizontalGroup buttonPanel;
    private TextButton btn_select;
    private TextButton btn_exit;

    private Label title;
    private Label roomInfo;

    private ButtonHandler handler;

    public MultiPlayerLobby(StartupScene parent) {
        super();
        this.parent = parent;
        this.guiTable = new Table();
        this.addActor(this.guiTable);
        this.handler = new ButtonHandler();

        this.title = new Label("MultiPlayer Lobby", GameConstants.DEFAULT_SKIN);
        this.roomInfo = new Label("Room Info", GameConstants.DEFAULT_SKIN);
        this.roomList = new List<Object>(GameConstants.DEFAULT_SKIN);
        this.btn_select = new TextButton("Select", GameConstants.DEFAULT_SKIN);
        this.btn_select.addListener(handler);
        this.btn_exit = new TextButton("Exit", GameConstants.DEFAULT_SKIN);
        this.btn_exit.addListener(handler);
        this.buttonPanel = new HorizontalGroup();
        this.buttonPanel.addActor(btn_select);
        this.buttonPanel.addActor(btn_exit);
        this.buttonPanel.space(50);
        // this.guiTable.setDebug(true);

        this.guiTable.setFillParent(true);
        this.guiTable.add(title)
                .colspan(2)
                .row();
        this.guiTable.add(roomList)
                .expand()
                .fill()
                .left();
        this.guiTable.add(roomInfo)
                .fill()
                .row();
        this.guiTable.add(buttonPanel)
                .colspan(2)
                .center()
                .expandX();
        this.roomList.setItems(rooms);
        Gdx.input.setInputProcessor(this);

    }

    private class ButtonHandler extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == btn_exit) {
                parent.setStage(new MainMenuStage(parent));
            }
        }

    }

    @Override
    public void onReceivedMessage(String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(Socket socket) {
        // TODO Auto-generated method stub

    }
}
