package com.example.fitnessfactory.ui.viewmodels;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.utils.GuiUtils;

import javax.inject.Inject;

public class MainActivityViewModel extends BaseViewModel {

    @Inject
    FirebaseAuthManager authManager;
    @Inject
    OrganisationInfoRepository organisationInfoRepository;

    public MainActivityViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Boolean> signOut() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(authManager.signOutSingle(),
                new SingleData<>(
                        observer::setValue,
                        throwable -> handleError(observer, throwable)));

        return observer;
    }

    public void setOrganisationName(String organisationName) {
        subscribeInIOThread(
                organisationInfoRepository.setOrganisationNameAsync(organisationName),
                this::handleError);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    private void handleError(SingleLiveEvent<Boolean> observer, Throwable throwable) {
        observer.setValue(false);
        handleError(throwable);
    }
}
