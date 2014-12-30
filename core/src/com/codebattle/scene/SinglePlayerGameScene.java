package com.codebattle.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.codebattle.gui.GameDialog;
import com.codebattle.gui.GameSceneGUI;
import com.codebattle.gui.StateShowable;
import com.codebattle.model.GameObject;
import com.codebattle.model.meta.PointLightMeta;
import com.codebattle.model.scriptprocessor.ScriptProcessor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.GameObjects;
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
        this.stage.getVirtualSystems()[this.currentPlayer.index].addSystemListener(this.gui.getControlGroup()
                .getSystemIndicator());
        this.stage.putDialog(new GameDialog("Hello World1 !!", GameConstants.DEFAULT_SKIN));
        this.stage.putDialog(new GameDialog("Hello World2 !!", GameConstants.DEFAULT_SKIN));
        this.stage.putDialog(new GameDialog("Hello World3 !!", GameConstants.DEFAULT_SKIN));

    }

    @Override
    public void setupGameObjects(final XmlReader.Element context) throws Exception {
        for (final XmlReader.Element element : context.getChildrenByNameRecursively("gameobject")) {
            final GameObject obj = GameObjects.create(stage, element);
            this.stage.addGameObject(obj);
        }
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
        } else {
            this.gui.resetShowable();
        }
    }

}
