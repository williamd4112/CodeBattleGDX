package com.codebattle.gui.server.models;

public class ClientItem {
    private int id;
    private String title;
    private String status;

    /**
     * Create a client item with id and other info.
     *
     * @param id        Unique id
     * @param title     Title
     * @param status    Status
     */
    public ClientItem(final int id, final String title, final String status) {
        this.setId(id);
        this.setTitle(title);
        this.setStatus(status);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ClientItem)) {
            return false;
        }
        if (other == this) {
            return true;
        }

        final ClientItem otherItem = (ClientItem) other;

        return otherItem.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
