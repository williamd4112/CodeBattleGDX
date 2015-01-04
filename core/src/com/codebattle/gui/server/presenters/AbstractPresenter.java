package com.codebattle.gui.server.presenters;

public abstract class AbstractPresenter<V, M> implements Presenter<V> {
    private M model;
    private V view;

    public AbstractPresenter(final V view) {
        this.view = view;
    }

    @Override
    public final void setView(final V view) {
        if (view == null) {
            throw new NullPointerException("View cannot be null.");
        }

        if (this.view != null) {
            throw new IllegalStateException("View has already been set.");
        }

        this.view = view;
    }

    /**
     * Set associated model.
     *
     * @param model     Associated model
     */
    public final void setModel(final M model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null.");
        }

        this.model = model;
    }

    public final M getModel() {
        return this.model;
    }

    protected final V getView() {
        return this.view;
    }
}
