package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.repositories.StaffAccessRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AdminListViewModel extends BaseViewModel {

    @Inject
    StaffAccessRepository accessRepository;
    @Inject
    UserRepository userRepository;

    public AdminListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void registerAccess(String email) {
        addSubscription(accessRepository.isAccessRegistered(email)
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMapCompletable(isRegistered ->
                        isRegistered ?
                                Completable.complete() :
                                accessRepository.registerAccess(email, AppPrefs.gymOwnerId().getValue()))
                .subscribe(() -> {
                }, throwable -> {
                    throwable.printStackTrace();
                    GuiUtils.showMessage(throwable.getLocalizedMessage());
                }));
    }

    public void addAdminsListListener() {
        addSubscription(accessRepository.getAdminsEmailsByOwnerId(AppPrefs.gymOwnerId().getValue())
                .observeOn(getMainThreadScheduler())
                .subscribeOn(getIOScheduler())
                .flatMapCompletable(adminsEmails -> userRepository.addAdminsListListener(adminsEmails))
                .subscribe(() -> {
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));
    }

    public void removeAdminsListListener() {
        subscribeInIOThread(userRepository.removeAdminsListListener(),
                throwable -> {
                    throwable.printStackTrace();
                    GuiUtils.showMessage(throwable.getLocalizedMessage());
                });
    }
}
