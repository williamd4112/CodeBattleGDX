package com.codebattle.gui.server.presenters;

public interface PresenterWithView<V> extends Presenter {
    void setView(V view);
}
