package com.codebattle.model.startup;

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

import org.json.JSONObject;

import java.net.Socket;
import java.util.Map;

public class MultiPlayerRoom extends Stage implements PeerListener {

    private final Client client;

    private final String[] scenes = { "scene_demo", "multiplay_1", "tutorial2" };

    final public StartupScene parentScene;
    final public MultiPlayerLobby lobbyStage;

    private View view;
    private Controller controller;

    private String sceneName = null;
    private String team = null;
    private Map<Object, Object> playerState = null;

    public MultiPlayerRoom(final MultiPlayerLobby lobbyStage) {
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
            this.view.setupListener(this.controller);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        this.view.setFillParent(true);
        this.addActor(this.view);

        Gdx.input.setInputProcessor(this);

    }

    public void getTeam() {
        this.client.send(DataHandler.getTeam("").toString());
    }

    public void getPlayerState() {
        this.client.send(DataHandler.requestPlayerState().toString());
    }

    public void ready() {
        this.client.send(DataHandler.ready(this.team).toString());
    }

    public void start() {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                try {
                    MultiPlayerRoom.this.parentScene.getParent().setScene(
                            new MultiPlayerGameScene(
                                    MultiPlayerRoom.this.parentScene.getParent(),
                                    MultiPlayerRoom.this, MultiPlayerRoom.this.sceneName,
                                    MultiPlayerRoom.this.team));
                } catch (final Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        try {
            final Message msg = new Message(rawMessage);
            if (msg.type.equals("Room")) {
                // Receive : Player state
                if (msg.opt.equals("PlayerState")) {
                    this.playerState = DataHandler.JsonToMap((JSONObject) msg.data);
                    // Setting portrait
                    for (final Object key : this.playerState.keySet()) {
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

        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(final Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(final Socket socket) {
        // TODO Auto-generated method stub

    }

    private class View extends Table {

        private final Label title;

        private final Image red_image, blue_image;
        private final List<Object> available_scenes;
        private final TextButton btn_changeScene;

        private final VerticalGroup selectArea;
        private final TextButton btn_start;
        private final TextButton btn_exit;

        public View(final Skin skin) throws Exception {
            super();
            this.title = new Label("Room", skin);

            this.red_image = new Image(TextureFactory.getInstance().loadTextureFromFile(
                    "default_portrait", ResourceType.PORTRAIT));
            this.blue_image = new Image(TextureFactory.getInstance().loadTextureFromFile(
                    "default_portrait", ResourceType.PORTRAIT));
            this.selectArea = new VerticalGroup();
            this.available_scenes = new List<Object>(skin);
            this.available_scenes.setItems(MultiPlayerRoom.this.scenes);
            this.btn_changeScene = new TextButton("Assign", skin);
            this.selectArea.addActor(this.available_scenes);
            this.selectArea.addActor(this.btn_changeScene);

            this.btn_start = new TextButton("Start", skin);
            this.btn_exit = new TextButton("Exit", skin);
        }

        public void setupListener(final Controller controller) {
            this.btn_start.addListener(controller);
            this.btn_exit.addListener(controller);
        }

        public void setupLayout() {
            this.add(this.title).top().colspan(3).row();
            this.add(this.red_image);
            this.add(this.blue_image);
            this.add(this.selectArea).expand().fill().top().right();
            this.row();
            this.add(this.btn_start).fillX();
            this.add(this.btn_exit).fillX();
        }

        public void setTitle(final String team) {
            this.title.setText(team);
        }

        public void setPlayerPortrait(final String key) {
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run() {
                    if (key.equals("Red")) {
                        View.this.setRed("Lancer");
                    } else if (key.equals("Blue")) {
                        View.this.setBlue("Saber");
                    }
                }

            });
        }

        public void resetPlayerPortrait(final String key) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (key.equals("Red")) {
                        View.this.setRed("default");
                    } else if (key.equals("Blue")) {
                        View.this.setBlue("default");
                    }
                }

            });
        }

        private void setRed(final String source) {
            try {
                this.red_image.setDrawable(TextureFactory.getInstance().loadDrawable(
                        source + "_portrait", ResourceType.PORTRAIT));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        private void setBlue(final String source) {
            try {
                this.blue_image.setDrawable(TextureFactory.getInstance().loadDrawable(
                        source + "_portrait", ResourceType.PORTRAIT));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class Controller extends ClickListener {

        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            if (event.getListenerActor() == MultiPlayerRoom.this.view.btn_start) {
                MultiPlayerRoom.this.ready();
            }
        }

    }
}
