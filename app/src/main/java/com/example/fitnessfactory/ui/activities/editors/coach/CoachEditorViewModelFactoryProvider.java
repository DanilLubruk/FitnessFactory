package com.example.fitnessfactory.ui.activities.editors.coach;

import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;

public class CoachEditorViewModelFactoryProvider {
    private static CoachEditorViewModelFactory factory;

    public static CoachEditorViewModelFactory getFactory() {
        if (factory == null) {
            init();
        }
        return factory;
    }

    private static void init() {
        factory = new CoachEditorViewModelFactory();
    }

    public static void clear() {
        factory = null;
    }
}
