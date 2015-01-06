package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.models.ClientItem;
import com.codebattle.gui.server.models.Server;
import com.codebattle.gui.server.views.ServerView;
import com.codebattle.gui.server.views.View;

public class ServerPresenter extends AbstractPresenter<ServerView, Server> {
    private boolean isServerRunning = false;

    public ServerPresenter(final View view) {
        super((ServerView) view);
    }

    public boolean isServerReady() {
        return true;
    }

    public boolean isServerRunning() {
        return this.isServerRunning;
    }

    /**
     * Start server.
     */
    public void startServer() {
        // Test
        final ClientListPresenter clientListPresenter =
                (ClientListPresenter)
                this.getPresenterFactory().getExistedPresenter(ClientListPresenter.class);

        clientListPresenter.addItem("Players", new ClientItem("10.0.0.0", "Connecting"));
        clientListPresenter.addItem("Players", new ClientItem("10.0.0.1", "Connected"));
        clientListPresenter.addItem("Players", new ClientItem("10.0.0.2", "No response"));

        clientListPresenter.addItem("Rooms", new ClientItem("Room 1", "1 people"));
        clientListPresenter.addItem("Rooms", new ClientItem("Room 2", "2 people, ready"));
        clientListPresenter.addItem("Rooms", new ClientItem("Room 3", "2 people, playing"));

        this.isServerRunning = true;
    }

    /**
     * Close server.
     */
    public void closeServer() {
        // Test
        final ClientListPresenter clientListPresenter =
                (ClientListPresenter)
                this.getPresenterFactory().getExistedPresenter(ClientListPresenter.class);

        clientListPresenter.clearAllItems("Players");
        clientListPresenter.clearAllItems("Rooms");

        this.isServerRunning = false;
    }

    public void dispose() {

    }
}
