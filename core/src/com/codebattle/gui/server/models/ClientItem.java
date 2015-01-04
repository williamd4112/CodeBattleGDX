package com.codebattle.gui.server.models;

public class ClientItem {
    private String title;
    private String status;

    public ClientItem(final String title, final String status) {
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
}
