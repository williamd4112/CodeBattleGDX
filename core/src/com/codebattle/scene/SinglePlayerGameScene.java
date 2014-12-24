package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.gui.GameSceneGUI;
import com.codebattle.model.GameObject;
import com.codebattle.model.Owner;
import com.codebattle.model.scriptprocessor.ScriptProcessor;
import com.codebattle.utility.GameActorFactory;
import com.codebattle.utility.GameUtil;
import com.codebattle.utility.SoundUtil;

public class SinglePlayerGameScene extends GameScene {

    // private ScriptEditor scriptEditor;
    private GameSceneGUI gui;

    public SinglePlayerGameScene(final String sceneName) throws Exception {
        super(sceneName);
    }

    @Override
    public void setupGUI() {
        this.gui = new GameSceneGUI(new Handler());
        this.stage.addGUI(this.gui);
    }

    @Override
    public void setupGameObjects(final XmlReader.Element context) throws Exception {
        for (final XmlReader.Element element : context.getChildrenByNameRecursively("gameobject")) {
            final String clazz = element.getAttribute("class");
            final Owner owner = GameUtil.toOwner(element.getAttribute("owner"));
            final String name = element.getAttribute("name");
            final String type = element.getAttribute("type");
            final float x = Float.parseFloat(element.getAttribute("x"));
            final float y = Float.parseFloat(element.getAttribute("y"));

            final GameObject obj = this.generateGameObject(clazz, owner, name, type, x, y);
            this.stage.addGameObject(obj);
        }
    }

    public GameObject generateGameObject(final String clazz, final Owner owner, final String name,
            final String type, final float x, final float y) throws Exception {
        if (clazz.equals("GameActor")) {
            return GameActorFactory.getInstance()
                    .createGameActor(this.stage, owner, name, type, x, y);
        } else {
            return null;
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

    /* Handling script interpretation */
    private class Handler extends ClickListener {
        @Override
        public void clicked(final InputEvent event, final float x, final float y) {
            super.clicked(event, x, y);
            final String script = SinglePlayerGameScene.this.gui.getEditor()
                    .getText();
            new ScriptProcessor(SinglePlayerGameScene.this.stage,
                    SinglePlayerGameScene.this.currentPlayer, script).start();

        }
    }

    @Override
    public void resizeGUI(final int width, final int height) {
        this.gui.resize(width, height);
        this.gui.invalidateHierarchy();
    }

}
