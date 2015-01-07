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

    /**
     * Add an item.
     *
     * @param role  Role
     * @param item  Item to be added.
     */
    public void addItem(final String role, final ClientItem item) {
        ClientListView view = this.getView(role);

        view.addItem(item, new ClientItemView(view, item));
    }

    /**
     * Update item.
     *
     * @param role  Role
     * @param item  Client item
     */
    public void updateItem(final String role, final ClientItem item) {
        final ClientItemView itemView = (ClientItemView) this.getView(role).getItem(item);

        itemView.update();
    }

    public void removeItem(final String role, final ClientItem item) {
        this.getView(role).removeItem(item);
    }

    public void clearAllItems(final String role) {
        this.getView(role).clearAllItems();
    }
}
