package com.codebattle.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DemoUiGroup extends Table {
    private final Skin skin;
    private final TextArea edtTxt;
    private final Label edtLabel;
    private final TextButton edtBtn;

    public DemoUiGroup() {
        this.skin = new Skin(Gdx.files.internal("skin/demo/uiskin.json"));
        this.edtLabel = new Label("Script Editor", this.skin);
        this.edtTxt = new TextArea("Enter your script here...", this.skin);
        this.edtBtn = new TextButton("submit", this.skin);
        this.edtBtn.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                System.out.println(DemoUiGroup.this.edtTxt.getText());
            }
        });
        this.set();
    }

    public void resize(final int width, final int height) {
        this.setSize(width, height);
        this.reset();
        this.set();
    }

    public void set() {
        this.add(this.edtLabel)
                .expandX()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
        this.row();
        this.add(this.edtTxt)
                .expand()
                .fillY()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
        this.row();
        this.add(this.edtBtn)
                .expandX()
                .align(Align.right)
                .width(this.getWidth() * 0.3f);
    }

    public String getEdtText() {
        return this.edtTxt.getText();
    }
}
