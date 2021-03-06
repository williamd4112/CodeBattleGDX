package com.codebattle.model.startup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.codebattle.scene.StartupScene;
import com.codebattle.utility.GameConstants;

abstract public class SelectionStage extends Stage {
    protected StartupScene parent;
    protected List<Object> list;

    protected HorizontalGroup topbar;
    protected Label title;
    protected Label info;

    protected TextButton btn_select;
    protected TextButton btn_exit;
    protected Table table;

    public SelectionStage(final StartupScene parent) {
        this.parent = parent;
        this.list = new List<Object>(GameConstants.DEFAULT_SKIN);
        this.topbar = new HorizontalGroup();
        this.title = new Label("Tutorials", GameConstants.DEFAULT_SKIN);
        this.info = new Label("Info", GameConstants.DEFAULT_SKIN);

        this.btn_select = new TextButton("Select", GameConstants.DEFAULT_SKIN);
        this.btn_exit = new TextButton("Exit", GameConstants.DEFAULT_SKIN);
        this.btn_exit.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                parent.setStage(new MainMenuStage(parent));
                SelectionStage.this.dispose();
            }

        });

        this.table = new Table();
        this.addActor(this.table);
        this.table.setFillParent(true);
        this.layout();

        Gdx.input.setInputProcessor(this);
    }

    public void layout() {
        this.table.add(this.title).colspan(2).row();
        this.table.add(this.topbar).right().colspan(2).row();
        this.table.add(new ScrollPane(this.list)).expand().fill().left().top();
        this.table.add(this.info).prefWidth(0.3f * Gdx.graphics.getWidth()).row();
        this.table.add(this.btn_select).center().fill();
        this.table.add(this.btn_exit).center().fill();
    }
}
