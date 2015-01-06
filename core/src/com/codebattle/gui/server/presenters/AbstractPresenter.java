package com.codebattle.gui.server.presenters;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPresenter<V, M> implements PresenterWithView<V> {
    private PresenterFactory presenterFactory;
    private final Map<Object, V> views = new HashMap<Object, V>();
    private M model;

    public AbstractPresenter(final V view) {
        this.setView(view);
    }

    @Override
    public void setPresenterFactory(final PresenterFactory presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    @Override
    public void setView(final V view) {
        if (view == null) {
            throw new NullPointerException("View cannot be null.");
        }

        if (this.getView() != null) {
            throw new IllegalStateException("View has already been set.");
        }

        this.views.put(null, view);
    }

    public void setView(final Object role, final V view) {
        this.views.put(role, view);
    }

    /**
     * Set associated model.
     *
     * @param model     Associated model
     */
    public void setModel(final M model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null.");
        }

        this.model = model;
    }

    public final M getModel() {
        return this.model;
    }

    @Override
    public PresenterFactory getPresenterFactory() {
        return this.presenterFactory;
    }

    protected V getView() {
        return this.views.get(null);
    }

    protected V getView(final Object role) {
        return this.views.get(role);
    }
}
