package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.models.ClientItem;
import com.codebattle.gui.server.models.ClientList;
import com.codebattle.gui.server.views.ClientItemView;
import com.codebattle.gui.server.views.ClientListView;
import com.codebattle.gui.server.views.View;

public class ClientListPresenter extends AbstractPresenter<ClientListView, ClientList> {
    public ClientListPresenter(final View view) {
        super((ClientListView) view);
    }

    public void addItem(final String role, final ClientItem item) {
        this.getView(role).addItem(new ClientItemView(item));
    }

    public void removeItem(final String role, final int index) {
        this.getView(role).remove(index);
    }

    public void clearAllItems(final String role) {
        this.getView(role).clearAllItems();
    }
}
