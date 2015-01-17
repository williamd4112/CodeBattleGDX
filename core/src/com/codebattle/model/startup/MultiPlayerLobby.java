package com.codebattle.model.startup;

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

import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiPlayerLobby extends SelectionStage implements PeerListener {

    private final Client client;

    private Map<Object, Object> rooms;

    private final TextButton btn_refresh;
    private final TextButton btn_create;
    private final TextButton btn_join;

    private MultiPlayerRoom room = null;

    public MultiPlayerLobby(final StartupScene parent) {
        super(parent);
        this.client = NetworkManager.getInstance().getClient();
        this.btn_refresh = new TextButton("Refresh", GameConstants.DEFAULT_SKIN);
        this.btn_refresh.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                MultiPlayerLobby.this.refresh();
            }

        });

        this.btn_create = new TextButton("Create", GameConstants.DEFAULT_SKIN);
        this.btn_create.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                MultiPlayerLobby.this.popCreateRoomDialog();
            }

        });

        this.btn_join = new TextButton("Join", GameConstants.DEFAULT_SKIN);
        this.btn_join.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                MultiPlayerLobby.this.joinRoom(MultiPlayerLobby.this.list.getSelected()
                        .toString());
            }

        });

        this.topbar.addActor(this.btn_refresh);
        this.topbar.addActor(this.btn_create);
        this.topbar.addActor(this.btn_join);

        this.rooms = new HashMap<Object, Object>();
        this.connect();
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    final Message msg = new Message(rawMessage);
                    if (msg.type.equals("Server")) {
                        if (msg.opt.equals("List")) {
                            MultiPlayerLobby.this.rooms =
                                    DataHandler.JsonToMap((JSONObject) msg.data);
                            MultiPlayerLobby.this.setRoomListItems(MultiPlayerLobby.this.rooms);
                        } else if (msg.opt.equals("Success")) {
                            if (msg.data.equals("CreateRoom") || msg.data.equals("Join")) {
                                MultiPlayerLobby.this.room =
                                        new MultiPlayerRoom(MultiPlayerLobby.this);
                                MultiPlayerLobby.this.room.getTeam();
                                MultiPlayerLobby.this.room.getPlayerState();
                                MultiPlayerLobby.this.parent.setStage(MultiPlayerLobby.this.room);
                            }
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

    public void connect() {
        // Create Client
        try {
            this.client.addPeerListener(this);
            this.client.send(DataHandler.loginServer("Williamd").toString());
            this.client.send(DataHandler.requestRoomList().toString());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        try {
            this.client.send(DataHandler.requestRoomList().toString());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void createRoom(final String name) {
        try {
            this.client.send(DataHandler.createRoom(name).toString());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(final String key) {
        try {
            this.client.send(DataHandler.joinRoom(key).toString());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void popCreateRoomDialog() {
        final CreateRoomDialog dlg =
                new CreateRoomDialog("Create Room", GameConstants.DEFAULT_SKIN);
        dlg.show(this);
    }

    public void setRoomListItems(final Map<Object, Object> map) {
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final Object key : map.keySet()) {
            list.add(key);
        }
        this.list.setItems(list.toArray());
    }

    public Client getClient() {
        return this.client;
    }

    private class CreateRoomDialog extends Dialog {

        private final TextField txt_RoomName;
        private final TextButton btn_confirm;
        private final TextButton btn_cancel;

        private final DialogButtonHandler handler;

        public CreateRoomDialog(final String title, final Skin skin) {
            super(title, skin);
            this.txt_RoomName = new TextField("Room01", skin);
            this.btn_confirm = new TextButton("Confirm", skin);
            this.btn_cancel = new TextButton("Cancel", skin);
            this.handler = new DialogButtonHandler();
            this.setupLayout();
            this.setupListener();
        }

        public void setupListener() {
            this.btn_confirm.addListener(this.handler);
            this.btn_cancel.addListener(this.handler);
        }

        public void setupLayout() {
            this.pad(20);
            this.row();
            this.add(this.txt_RoomName).center().colspan(2).row();
            this.add(this.btn_confirm);
            this.add(this.btn_cancel);
        }

        private class DialogButtonHandler extends ClickListener {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (event.getListenerActor() == CreateRoomDialog.this.btn_confirm) {
                    if (CreateRoomDialog.this.txt_RoomName != null) {
                        MultiPlayerLobby.this.createRoom(CreateRoomDialog.this.txt_RoomName.getText());
                        CreateRoomDialog.this.addAction(Actions.sequence(
                                Actions.fadeOut(0.2f), Actions.removeActor()));
                    }
                } else if (event.getListenerActor() == CreateRoomDialog.this.btn_cancel) {
                    CreateRoomDialog.this.addAction(Actions.sequence(Actions.fadeOut(0.2f),
                            Actions.removeActor()));
                }
            }

        }
    }
}
