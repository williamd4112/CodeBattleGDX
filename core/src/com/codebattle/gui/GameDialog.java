package com.codebattle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.codebattle.model.GameStage;
import com.codebattle.model.meta.GameMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class GameDialog extends Table implements Resizeable {
    final public GameStage stage;
    private final LabelPanel content;
    public String context;
    private Image image = null;
    private int index = 0;
    private List<GameMethod> callbacks = null;

    public GameDialog(final GameStage stage, final TextureRegion portrait,
            final String context, final Skin skin) {
        super();
        this.callbacks = new LinkedList<GameMethod>();
        this.stage = stage;
        this.content = new LabelPanel(context, skin);
        this.context = context;
        if (portrait != null) {
            try {
                this.image = new Image(portrait);
            } catch (final Exception e) {
                e.printStackTrace();
                this.image = null;
            }
        }
        this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(final int width, final int height) {
        this.reset();
        this.setFillParent(true);
        this.index = 0;
        this.content.resize(width, height);
        if (this.image != null) {
            this.add(this.image)
                    .expand()
                    .bottom()
                    .left()
                    .padLeft(0.01f * width)
                    .row();
        }
        this.add(this.content)
                .bottom();
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (this.index < this.context.length()) {
            this.content.setText(this.context.substring(0, this.index++));
        }

    }

    public void setCallback(final GameMethod method) {
        this.callbacks.add(method);
        method.bind("Stage", this.stage);
    }

    public void callback() {
        try {
            for (final GameMethod m : this.callbacks) {
                m.execute();
            }
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
