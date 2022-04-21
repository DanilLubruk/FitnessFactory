package com.example.fitnessfactory.ui.activities.editors.gym;

import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;

public class GymEditorViewModelFactoryProvider {
    private static GymEditorViewModelFactory factory;

    public static GymEditorViewModelFactory getFactory() {
        if (factory == null) {
            init();
        }
        return factory;
    }

    private static void init() {
        factory = new GymEditorViewModelFactory();
    }

    public static void clear() {
        factory = null;
    }
}
