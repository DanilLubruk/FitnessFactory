package com.example.fitnessfactory.ui.activities.editors.session;

import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;

public class SessionEditorViewModelFactoryProvider {
    private static SessionEditorViewModelFactory factory;

    public static SessionEditorViewModelFactory getFactory() {
        if (factory == null) {
            init();
        }

        return factory;
    }

    private static void init() {
        factory = new SessionEditorViewModelFactory();
    }

    public static void clear() {
        factory = null;
    }
}
