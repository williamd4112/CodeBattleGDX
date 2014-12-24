package com.codebattle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.codebattle.utility.ResourceType;
import com.codebattle.utility.TextureFactory;

public class Panel extends BasePanel {
    private Texture portrait;
    private Image image;
    private final StateTable stateTable;

    private final APIList apiList;

    public Panel(final Skin skin) {
        super(skin);
        try {
            this.portrait = TextureFactory.getInstance()
                    .loadTextureFromFile("lancer_portrait", ResourceType.PORTRAIT);
            final Drawable drawable = new TextureRegionDrawable(new TextureRegion(this.portrait));
            this.image = new Image(drawable);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.stateTable = new StateTable(skin);
        this.apiList = new APIList(skin);
    }

    public void resize(final int width, final int height) {
        this.reset();
        this.setDebug(true);
        this.stateTable.resize(width, height);
        this.apiList.resize(width, height);
        this.add(this.image)
                .prefSize(width * 0.1f)
                .pad(5)
                .left()
                .fill();
        this.add(this.stateTable)
                .padLeft(width * 0.01f)
                .padRight(width * 0.01f)
                .left()
                .center();
        this.add(this.apiList)
                .left();
        this.pad(5);
    }

    public void setImage(final String source) {
        try {
            this.portrait = TextureFactory.getInstance()
                    .loadTextureFromFile(source + "_portrait", ResourceType.PORTRAIT);
            final Drawable drawable = new TextureRegionDrawable(new TextureRegion(this.portrait));
            this.image.setDrawable(drawable);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
