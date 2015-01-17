package com.codebattle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.codebattle.model.gameactor.GameActor;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class Panel extends BasePanel {
    private Texture portrait;
    private final Image image;
    private final StateTable stateTable;
    private final APIList apiList;
    private final Label nameInfo;
    private final Label positionInfo;

    private StateShowable showable;

    public Panel(final Skin skin) {
        super(skin);
        this.image = new Image();
        this.image.setScaling(Scaling.fit);
        this.setImage("default");
        this.stateTable = new StateTable(skin);
        this.apiList = new APIList(skin);
        this.nameInfo = new Label("", skin);
        this.positionInfo = new Label("", skin);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);
        this.stateTable.resize(width, height);
        this.apiList.resize(width, height);
        this.add(this.nameInfo);
        this.add(this.positionInfo)
                .row();
        this.add(this.image)
                .prefSize(width * 0.1f)
                .pad(5)
                .left()
                .fill();
        this.add(this.stateTable)
                .padLeft(width * 0.01f)
                .padRight(width * 0.01f)
                .left();
        this.add(this.apiList)
                .left();
        this.pad(5);
    }

    public void setImage(final String source) {
        try {
            this.portrait = TextureFactory.getInstance()
                    .loadTextureFromFile(source + "_portrait", ResourceType.PORTRAIT);
            final Drawable drawable =
                    new TextureRegionDrawable(new TextureRegion(this.portrait));
            this.image.setDrawable(drawable);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void setAPI(final Class type) {
        if (type == GameActor.class) {
            this.apiList.setAPIList(GameConstants.API_GAMEACTOR);
        } else {
            this.apiList.resetAPIList();
        }
    }

    public void resetAPI() {
        this.apiList.resetAPIList();
    }

    public void setShowable(final StateShowable showable) {
        this.showable = showable;
        final Drawable d = this.showable.getPortrait();
        if (d != null) {
            this.image.setDrawable(d);
        } else {
            this.setImage("default");
        }
        this.stateTable.setKeys(showable.getKeys());
        this.stateTable.setValues(showable.getValues());
        this.setAPI(showable.getClass());
        this.nameInfo.setText(showable.getNameInfo());
        this.positionInfo.setText(showable.getPositionInfo());
    }

    public void resetShowable() {
        this.stateTable.resetShowable();
        this.setImage("default");
        this.nameInfo.setText("");
        this.positionInfo.setText("");
    }
}
