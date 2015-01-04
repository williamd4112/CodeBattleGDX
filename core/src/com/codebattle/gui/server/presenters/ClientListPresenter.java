package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.models.ClientItem;
import com.codebattle.gui.server.models.ClientList;
import com.codebattle.gui.server.views.ClientItemView;
import com.codebattle.gui.server.views.ClientListView;

public class ClientListPresenter extends AbstractPresenter<ClientListView, ClientList> {
    public ClientListPresenter(final ClientListView view) {
        super(view);
    }

    public void addItem(ClientItem item) {
        this.getView().addItem(new ClientItemView(item));
    }

    public void removeItem(int index) {
        this.getView().remove(index);
    }

    public void clearAllItems() {
        this.getView().clearAllItems();
    }

    public void test() {
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.0", "Connecting")));
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.1", "Connected")));
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.2", "No response")));
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.3", "Connected")));
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.4", "No response")));
        this.getView().addItem(new ClientItemView(new ClientItem("10.0.0.5", "Connecting")));
    }
}
