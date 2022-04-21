package com.example.fitnessfactory.ui.activities.editors.admin;

import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;

public class AdminEditorViewModelFactoryProvider {
    private static AdminEditorViewModelFactory factory;

    public static AdminEditorViewModelFactory getFactory() {
        if (factory == null) {
            init();
        }
        return factory;
    }

    private static void init() {
        factory = new AdminEditorViewModelFactory();
    }

    public static void clear() {
        factory = null;
    }
}
