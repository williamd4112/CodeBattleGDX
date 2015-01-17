package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.models.Server;
import com.codebattle.gui.server.views.ServerView;
import com.codebattle.gui.server.views.View;

import java.io.IOException;

public class ServerPresenter extends AbstractPresenter<ServerView, Server> {
    private boolean isServerRunning = false;
    private com.codebattle.network.server.Server server;

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
     * @throws IOException
     */
    public void startServer() {
        try {
            this.server = new com.codebattle.network.server.Server(8000);
            this.server.addPresenterFactory(this.getPresenterFactory());
            this.server.setClientListPresenter();
            this.server.start();

            this.isServerRunning = true;
        } catch (final Exception e) {
            throw new RuntimeException("Failed to start server.", e);
        }
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
