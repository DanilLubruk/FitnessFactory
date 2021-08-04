package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;

import javax.inject.Inject;

public class MainActivityViewModel extends BaseViewModel {

    @Inject
    FirebaseAuthManager authManager;

    public MainActivityViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Boolean> signOut() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.signOut(),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleError(observer, throwable)));

        return observer;
    }

    private void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }
}
