package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.game.CodeBattle;
import com.codebattle.gui.GameSceneGUI;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.GameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.VirtualSystem;
import com.codebattle.model.event.Events;
import com.codebattle.model.event.GameEvent;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.scriptprocessor.ScriptProcessor;
import com.codebattle.utility.GameObjects;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.SoundUtil;
import com.codebattle.utility.TextureFactory;

abstract public class PlayerGameScene extends GameScene {

    protected GameSceneGUI gui;
    protected ClickListener handler;

    public PlayerGameScene(CodeBattle parent, final String sceneName) throws Exception {
        super(parent, sceneName);
    }

    @Override
    public void setupGUI() {
        this.gui = new GameSceneGUI(this.stage);
        this.stage.addGUI(this.gui);
        this.stage.getVirtualSystems()[this.currentPlayer.index].addSystemListener(this.gui.getControlGroup()
                .getSystemIndicator());
    }

    @Override
    public void setupGameObjects(final XmlReader.Element context) throws Exception {
        for (final XmlReader.Element element : context.getChildrenByNameRecursively("gameobject")) {
            final GameObject obj = GameObjects.create(stage, element);
            if (obj != null)
                this.stage.addGameObject(obj);
        }
    }

    @Override
    public void setupAmbientLight(Element context) throws Exception {
        XmlReader.Element ambientElement = context.getChildByName("ambient");
        Color color = Color.valueOf(ambientElement.getAttribute("color"));
        float intensity = Float.parseFloat(ambientElement.getAttribute("intensity"));
        this.stage.setAmbientLight(color, intensity);

    }

    @Override
    public void setupPointLight(Element context) {
        XmlReader.Element initElement = context.getChildByName("init");
        for (XmlReader.Element lightElement : initElement.getChildrenByName("pointlight")) {
            PointLightMeta light = new PointLightMeta(lightElement);
            System.out.println("Scene init: " + light.x + " , " + light.y);
            this.stage.addPointLight(light);
        }
    }

    @Override
    public void setupInput() {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void setupBGS(final Element context) throws Exception {
        final XmlReader.Element bgsElement = context.getChildByName("bgs");
        final String bgsName = bgsElement.getText();
        if (bgsName == null) {
            return; // Default no bgs
        }
        SoundUtil.playBGS(bgsName);
    }

    @Override
    public void setupBGM(final Element context) throws Exception {
        final XmlReader.Element bgmElement = context.getChildByName("bgm");
        final String bgmName = bgmElement.getText();
        if (bgmName == null) {
            return; // Default no bgs
        }
        SoundUtil.playBGM(bgmName);
    }

    @Override
    public void setupEvents(Element context) throws NoSuchMethodException, SecurityException {
        XmlReader.Element eventsElement = context.getChildByName("events");
        for (XmlReader.Element eventElement : eventsElement.getChildrenByName("event")) {
            GameEvent e = Events.create(this.stage, eventElement);
            System.out.println("setup event: " + e.getName());
            this.stage.getEventManager().addGameEvent(e);
        }

    }

    @Override
    public void resizeGUI(final int width, final int height) {
        this.gui.invalidateHierarchy();
    }

    @Override
    public void onGUIChange() {
        GameObject selectObject = this.stage.getSelectedObject();
        if (selectObject != null) {
            if (selectObject instanceof StateShowable) {
                this.gui.getControlGroup()
                        .getPanel()
                        .setShowable((StateShowable) selectObject);
            } else {
                this.gui.resetShowable();
            }
        } else if (this.stage.getSelectedCell() != null) {
            this.gui.getControlGroup().getPanel().setShowable(this.stage.getSelectedCell());

        } else {
            this.gui.resetShowable();
        }
    }

    @Override
    public boolean onReceiveScript(String script) {
        System.out.println();
        boolean result = new ScriptProcessor(this.stage, PlayerGameScene.this.currentPlayer,
                script).run();
        this.setSuccess(result);
        return result;
    }

    @Override
    public void onRoundComplete() {
        VirtualSystem[] players = this.stage.getVirtualSystems();
        Owner winner = null;
        for (VirtualSystem player : players) {
            if (player.getHP() <= 0) {
                if (players[Owner.RED.index].getHP() > players[Owner.BLUE.index].getHP()) {
                    System.out.println("Red win");
                    winner = Owner.RED;
                } else if (players[Owner.RED.index].getHP() < players[Owner.BLUE.index].getHP()) {
                    System.out.println("Blue win");
                    winner = Owner.BLUE;
                } else {
                    System.out.println("Tie !");
                    winner = Owner.GREEN;
                }
                this.onStageComplete(winner);
                break;
            }
        }
    }

    @Override
    public void onStageComplete(Owner winner) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                PlayerGameScene.this.putWinnerImage();
                PlayerGameScene.this.stage.addAction(Actions.sequence(Actions.delay(1.5f),
                        Actions.fadeOut(3.5f), Actions.run(new Runnable() {

                            @Override
                            public void run() {
                                parent.setScene(new StartupScene(parent));

                            }

                        })));
            }
        });
    }

    private void putWinnerImage() {
        try {
            Image image = new Image(TextureFactory.getInstance().loadDrawable(
                    "Title_Win.png", ResourceType.PICTURE));
            image.setTouchable(Touchable.disabled);
            image.setScaling(Scaling.fit);
            image.setWidth(Gdx.graphics.getWidth());
            image.setHeight(Gdx.graphics.getHeight());
            this.stage.addGUI(image);
            image.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(3.0f)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameSceneGUI getGui() {
        return this.gui;
    }
}
