package com.codebattle.gui.server.presenters;

public interface Presenter {
    void setPresenterFactory(PresenterFactory presenterFactory);

    PresenterFactory getPresenterFactory();
}
