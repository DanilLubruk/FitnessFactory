package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.RxUtils;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminListViewModel extends BaseViewModel implements DataListListener {

    @Inject
    AdminsAccessManager adminsAccessManager;
    @Inject
    AdminsDataManager adminsDataManager;
    @Inject
    AdminsListDataListener adminsListListener;

    private final MutableLiveData<List<AppUser>> admins = new MutableLiveData<>();

    public AdminListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<String> registerAdmin(Single<String> emailDialog, Single<Boolean> sendInvitationDialog) {
        SingleLiveEvent<String> observer = new SingleLiveEvent<>();
        SafeReference<String> adminEmail = new SafeReference<>();

        subscribe(
                emailDialog
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(email -> {
                            email = email.toLowerCase();
                            adminEmail.set(email);
                            return Single.just(email);
                        })
                        .subscribeOn(getIOScheduler())
                        .observeOn(getIOScheduler())
                        .flatMap(email -> adminsAccessManager.createAdmin(AppPrefs.gymOwnerId().getValue(), email))
                        .flatMap(isCreated ->
                                isCreated ?
                                        Single.just(AppPrefs.askForSendingAdminEmailInvite().getValue()) :
                                        Single.just(false))
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(doAsk -> doAsk ? sendInvitationDialog : Single.just(false))
                        .flatMap(doSendInvitation ->
                                doSendInvitation ?
                                        Single.just(adminEmail.getValue()) :
                                        Single.error(new DialogCancelledException()))
                        .subscribeOn(getIOScheduler()),
                new SingleData<>(
                        observer::setValue,
                        RxUtils::handleError));

        return observer;
    }

    public void startDataListener() {
        adminsListListener.startAdminsListListener();
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void getAdminsListData() {
        subscribeInIOThread(adminsDataManager.getAdminsListAsync(),
                new SingleData<>(admins::setValue, RxUtils::handleError));
    }

    public void stopDataListener() {
        adminsListListener.stopDataListener();
    }

    public void deleteAdmin(String email) {
        subscribeInIOThread(
                adminsAccessManager.deleteAdminCompletable(AppPrefs.gymOwnerId().getValue(), email),
                RxUtils::handleError);
    }
}
