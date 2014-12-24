package com.codebattle.demo;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.model.gameactor.GameActorDescription;
import com.codebattle.utility.XMLUtil;

import java.io.IOException;

public class DemoFileIO implements Screen {

    public DemoFileIO() {
        try {
            final XmlReader.Element root = XMLUtil.readXMLFromFile("actors/Saber.xml");
            final GameActorDescription desc = new GameActorDescription(root);
            System.out.println(desc);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void render(final float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(final int width, final int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
