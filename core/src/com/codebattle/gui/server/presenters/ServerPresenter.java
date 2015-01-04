package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.models.Server;
import com.codebattle.gui.server.views.ServerView;

public class ServerPresenter extends AbstractPresenter<ServerView, Server> {
    private boolean isServerRunning = false;

    public ServerPresenter(ServerView view) {
        super(view);
    }

    public boolean isServerReady() {
        return true;
    }

    public boolean isServerRunning() {
        return isServerRunning;
    }

    public void startServer() {
        isServerRunning = true;
    }

    public void closeServer() {
        isServerRunning = false;
    }

    public void dispose() {

    }
}
