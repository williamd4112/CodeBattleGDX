package com.codebattle.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameMethod;

public class GameDialog extends Table implements Resizeable {
    final public GameStage stage;
    private LabelPanel content;
    public String context;
    private Image image = null;
    private int index = 0;
    private List<GameMethod> callbacks = null;

    public GameDialog(GameStage stage, TextureRegion portrait, String context, Skin skin) {
        super();
        this.callbacks = new LinkedList<GameMethod>();
        this.stage = stage;
        this.content = new LabelPanel(context, skin);
        this.context = context;
        if (portrait != null) {
            try {
                this.image = new Image(portrait);
            } catch (Exception e) {
                e.printStackTrace();
                this.image = null;
            }
        }
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        this.reset();
        this.setFillParent(true);
        this.index = 0;
        this.content.resize(width, height);
        if (this.image != null) {
            this.add(image)
                    .expand()
                    .bottom()
                    .left()
                    .padLeft(0.01f * width)
                    .row();
        }
        this.add(content)
                .bottom();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.index < this.context.length())
            this.content.setText(context.substring(0, this.index++));

    }

    public void setCallback(GameMethod method) {
        this.callbacks.add(method);
        method.bind("Stage", this.stage);
    }

    public void callback() {
        try {
            for (GameMethod m : this.callbacks)
                m.execute();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
