package com.codebattle.gui.server.presenters;

import com.codebattle.gui.server.views.View;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PresenterFactory {
    private final Map<Class<?>, Presenter> presenters = new HashMap<Class<?>, Presenter>();

    /**
     * Get existed presenter, if not existed, create a new one and return.
     *
     * @param presenterClazz    Presenter class
     * @param view              View instance
     * @return Existed or new presenter.
     */
    public Presenter getPresenter(final Class<?> presenterClazz, final View view) {
        final Presenter presenter = this.getExistedPresenter(presenterClazz);

        if (presenter != null) {
            return presenter;
        } else {
            return this.getNewPresenter(presenterClazz, view);
        }
    }

    public Presenter getExistedPresenter(final Class<?> presenterClazz) {
        return this.presenters.get(presenterClazz);
    }

    private Presenter getNewPresenter(final Class<?> presenterClazz, final View view) {
        Presenter presenter;

        try {
            presenter =
                    (Presenter) presenterClazz.getDeclaredConstructor(View.class)
                            .newInstance(view);
        } catch (final InstantiationException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        } catch (final SecurityException e) {
            throw new RuntimeException("Error while creating presenter instance.", e);
        }

        this.presenters.put(presenterClazz, presenter);

        return presenter;
    }
}
