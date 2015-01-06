package com.codebattle.model.startup;

import java.net.Socket;
import java.util.Map;

import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.network.PeerListener;
import com.codebattle.network.client.Client;
import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;
import com.codebattle.scene.MultiPlayerGameScene;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class MultiPlayerRoom extends Stage implements PeerListener {

    private Client client;

    private String[] scenes = { "scene_demo", "multiplay_1", "tutorial2" };

    final public StartupScene parentScene;
    final public MultiPlayerLobby lobbyStage;

    private View view;
    private Controller controller;

    private String sceneName = null;
    private String team = null;
    private Map<Object, Object> playerState = null;

    public MultiPlayerRoom(MultiPlayerLobby lobbyStage) {
        super();
        // Init network
        this.client = lobbyStage.getClient();
        this.client.addPeerListener(this);

        this.lobbyStage = lobbyStage;
        this.parentScene = lobbyStage.parent;
        try {
            this.view = new View(GameConstants.DEFAULT_SKIN);
            this.controller = new Controller();
            this.view.setupLayout();
            this.view.setupListener(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.view.setFillParent(true);
        this.addActor(view);

        Gdx.input.setInputProcessor(this);

    }

    public void getTeam() {
        this.client.send(DataHandler.getTeam("").toString());
    }

    public void getPlayerState() {
        this.client.send(DataHandler.requestPlayerState().toString());
    }

    public void ready() {
        this.client.send(DataHandler.ready(team).toString());
    }

    public void start() {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                try {
                    parentScene.getParent().setScene(
                            new MultiPlayerGameScene(parentScene.getParent(),
                                    MultiPlayerRoom.this, sceneName, team));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onReceivedMessage(String rawMessage) {
        try {
            Message msg = new Message(rawMessage);
            if (msg.type.equals("Room")) {
                // Receive : Player state
                if (msg.opt.equals("PlayerState")) {
                    this.playerState = DataHandler.JsonToMap((JSONObject) msg.data);
                    // Setting portrait
                    for (Object key : this.playerState.keySet()) {
                        if (this.playerState.get(key).equals("Online")) {
                            this.view.setPlayerPortrait((String) key);
                        } else {
                            this.view.resetPlayerPortrait((String) key);
                        }
                    }
                }
            } else if (msg.type.equals("Game")) {
                // Receive : Team
                if (msg.opt.equals("Team")) {
                    this.team = (String) msg.data;
                    this.view.setTitle(this.team);
                } else if (msg.opt.equals("Start")) {
                    this.sceneName = msg.data.toString();
                    this.start();
                    System.out.println("!!!!!!!!!!!Start!!!!!!!!!!!!!!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    private class View extends Table {

        private Label title;

        private Image red_image, blue_image;
        private List<Object> available_scenes;
        private TextButton btn_changeScene;

        private VerticalGroup selectArea;
        private TextButton btn_start;
        private TextButton btn_exit;

        public View(Skin skin) throws Exception {
            super();
            this.title = new Label("Room", skin);

            this.red_image = new Image(TextureFactory.getInstance().loadTextureFromFile(
                    "default_portrait", ResourceType.PORTRAIT));
            this.blue_image = new Image(TextureFactory.getInstance().loadTextureFromFile(
                    "default_portrait", ResourceType.PORTRAIT));
            this.selectArea = new VerticalGroup();
            this.available_scenes = new List<Object>(skin);
            this.available_scenes.setItems(scenes);
            this.btn_changeScene = new TextButton("Assign", skin);
            this.selectArea.addActor(available_scenes);
            this.selectArea.addActor(btn_changeScene);

            this.btn_start = new TextButton("Start", skin);
            this.btn_exit = new TextButton("Exit", skin);
        }

        public void setupListener(Controller controller) {
            this.btn_start.addListener(controller);
            this.btn_exit.addListener(controller);
        }

        public void setupLayout() {
            this.add(title).top().colspan(3).row();
            this.add(red_image);
            this.add(blue_image);
            this.add(selectArea).expand().fill().top().right();
            this.row();
            this.add(btn_start).fillX();
            this.add(btn_exit).fillX();
        }

        public void setTitle(String team) {
            this.title.setText(team);
        }

        public void setPlayerPortrait(final String key) {
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run() {
                    if (key.equals("Red"))
                        setRed("Lancer");
                    else if (key.equals("Blue"))
                        setBlue("Saber");
                }

            });
        }

        public void resetPlayerPortrait(final String key) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (key.equals("Red"))
                        setRed("default");
                    else if (key.equals("Blue"))
                        setBlue("default");
                }

            });
        }

        private void setRed(String source) {
            try {
                this.red_image.setDrawable(TextureFactory.getInstance().loadDrawable(
                        source + "_portrait", ResourceType.PORTRAIT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setBlue(String source) {
            try {
                this.blue_image.setDrawable(TextureFactory.getInstance().loadDrawable(
                        source + "_portrait", ResourceType.PORTRAIT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class Controller extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == view.btn_start) {
                ready();
            }
        }

    }
}
