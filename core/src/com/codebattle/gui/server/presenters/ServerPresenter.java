package com.codebattle.gui.server.presenters;

import java.io.IOException;

import com.codebattle.gui.server.models.ClientItem;
import com.codebattle.gui.server.models.Server;
import com.codebattle.gui.server.views.ServerView;
import com.codebattle.gui.server.views.View;

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
        // Test
        /*final ClientListPresenter clientListPresenter =
                (ClientListPresenter)
                this.getPresenterFactory().getExistedPresenter(ClientListPresenter.class);

        clientListPresenter.addItem("Players", new ClientItem(0, "10.0.0.0", "Connecting"));
        clientListPresenter.addItem("Players", new ClientItem(1, "10.0.0.1", "Connected"));
        clientListPresenter.addItem("Players", new ClientItem(2, "10.0.0.2", "No response"));

        clientListPresenter.addItem("Rooms", new ClientItem(0, "Room 1", "1 people"));
        clientListPresenter.addItem("Rooms", new ClientItem(1, "Room 2", "2 people, ready"));
        clientListPresenter.addItem("Rooms", new ClientItem(2, "Room 3", "2 people, playing"));*/
    	
    	try{
    	server = new com.codebattle.network.server.Server(8000);
    	server.addPresenterFactory(this.getPresenterFactory());
    	server.setClientListPresenter();
    	server.start();

        this.isServerRunning = true;
    	} catch(Exception e){}
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
