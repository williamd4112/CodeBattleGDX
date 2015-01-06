package com.codebattle.model.startup;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.network.PeerListener;
import com.codebattle.network.client.Client;
import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.NetworkManager;

public class MultiPlayerLobby extends SelectionStage implements PeerListener {

    private Client client;

    private Map<Object, Object> rooms;

    private TextButton btn_refresh;
    private TextButton btn_create;
    private TextButton btn_join;

    private MultiPlayerRoom room = null;

    public MultiPlayerLobby(StartupScene parent) {
        super(parent);
        this.client = NetworkManager.getInstance().getClient();
        this.btn_refresh = new TextButton("Refresh", GameConstants.DEFAULT_SKIN);
        this.btn_refresh.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                refresh();
            }

        });

        this.btn_create = new TextButton("Create", GameConstants.DEFAULT_SKIN);
        this.btn_create.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                popCreateRoomDialog();
            }

        });

        this.btn_join = new TextButton("Join", GameConstants.DEFAULT_SKIN);
        this.btn_join.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                joinRoom(list.getSelected().toString());
            }

        });

        this.topbar.addActor(btn_refresh);
        this.topbar.addActor(btn_create);
        this.topbar.addActor(btn_join);

        this.rooms = new HashMap<Object, Object>();
        this.connect();
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = new Message(rawMessage);
                    if (msg.type.equals("Server")) {
                        if (msg.opt.equals("List")) {
                            rooms = DataHandler.JsonToMap((JSONObject) msg.data);
                            setRoomListItems(rooms);
                        } else if (msg.opt.equals("Success")) {
                            if (msg.data.equals("CreateRoom") || msg.data.equals("Join")) {
                                room = new MultiPlayerRoom(MultiPlayerLobby.this);
                                room.getTeam();
                                room.getPlayerState();
                                parent.setStage(room);
                            }
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

    public void connect() {
        // Create Client
        try {
            client.addPeerListener(this);
            client.send(DataHandler.loginServer("Williamd").toString());
            client.send(DataHandler.requestRoomList().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        try {
            client.send(DataHandler.requestRoomList().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRoom(String name) {
        try {
            client.send(DataHandler.createRoom(name).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(String key) {
        try {
            client.send(DataHandler.joinRoom(key).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popCreateRoomDialog() {
        CreateRoomDialog dlg = new CreateRoomDialog("Create Room", GameConstants.DEFAULT_SKIN);
        dlg.show(this);
    }

    public void setRoomListItems(Map<Object, Object> map) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object key : map.keySet())
            list.add(key);
        this.list.setItems(list.toArray());
    }

    public Client getClient() {
        return this.client;
    }

    private class CreateRoomDialog extends Dialog {

        private TextField txt_RoomName;
        private TextButton btn_confirm;
        private TextButton btn_cancel;

        private DialogButtonHandler handler;

        public CreateRoomDialog(String title, Skin skin) {
            super(title, skin);
            this.txt_RoomName = new TextField("Room01", skin);
            this.btn_confirm = new TextButton("Confirm", skin);
            this.btn_cancel = new TextButton("Cancel", skin);
            this.handler = new DialogButtonHandler();
            this.setupLayout();
            this.setupListener();
        }

        public void setupListener() {
            this.btn_confirm.addListener(handler);
            this.btn_cancel.addListener(handler);
        }

        public void setupLayout() {
            this.pad(20);
            this.row();
            this.add(this.txt_RoomName).center().colspan(2).row();
            this.add(btn_confirm);
            this.add(btn_cancel);
        }

        private class DialogButtonHandler extends ClickListener {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (event.getListenerActor() == btn_confirm) {
                    if (txt_RoomName != null) {
                        MultiPlayerLobby.this.createRoom(txt_RoomName.getText());
                        CreateRoomDialog.this.addAction(Actions.sequence(
                                Actions.fadeOut(0.2f), Actions.removeActor()));
                    }
                } else if (event.getListenerActor() == btn_cancel) {
                    CreateRoomDialog.this.addAction(Actions.sequence(Actions.fadeOut(0.2f),
                            Actions.removeActor()));
                }
            }

        }
    }
}
