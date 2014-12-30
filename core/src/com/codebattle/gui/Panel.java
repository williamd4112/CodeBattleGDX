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
    private Image image;
    private StateTable stateTable;
    private APIList apiList;
    private Label nameInfo;

    private StateShowable showable;

    public Panel(Skin skin) {
        super(skin);
        this.image = new Image();
        this.image.setScaling(Scaling.fit);
        this.setImage("default");
        this.stateTable = new StateTable(skin);
        this.apiList = new APIList(skin);
        this.nameInfo = new Label("Saber", skin);
    }

    public void resize(int width, int height) {
        this.reset();
        this.setDebug(true);
        this.stateTable.resize(width, height);
        this.apiList.resize(width, height);
        this.add(nameInfo)
                .row();
        this.add(image)
                .prefSize(width * 0.1f)
                .pad(5)
                .left()
                .fill();
        this.add(stateTable)
                .padLeft(width * 0.01f)
                .padRight(width * 0.01f)
                .left();
        this.add(apiList)
                .left();
        this.pad(5);
    }

    public void setImage(String source) {
        try {
            this.portrait = TextureFactory.getInstance()
                    .loadTextureFromFile(source + "_portrait", ResourceType.PORTRAIT);
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(this.portrait));
            this.image.setDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAPI(Class type) {
        if (type == GameActor.class) {
            this.apiList.setAPIList(GameConstants.API_GAMEACTOR);
        }
    }

    public void resetAPI() {
        this.apiList.resetAPIList();
    }

    public void setShowable(StateShowable showable) {
        this.showable = showable;
        this.image.setDrawable(this.showable.getPortrait());
        this.stateTable.setKeys(showable.getKeys());
        this.stateTable.setValues(showable.getValues());
        this.setAPI(showable.getClass());
        this.nameInfo.setText(showable.getNameInfo());
    }

    public void resetShowable() {
        this.stateTable.resetShowable();
        this.setImage("default");
        this.nameInfo.setText("");
    }
}
