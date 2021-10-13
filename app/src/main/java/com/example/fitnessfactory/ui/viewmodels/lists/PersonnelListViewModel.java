package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.DataListener;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.system.SafeReference;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.RxUtils;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;

import java.util.List;

import io.reactivex.Single;

public abstract class PersonnelListViewModel extends BaseViewModel implements DataListListener<AppUser>  {

    private PersonnelAccessManager accessManager;

    private PersonnelDataManager dataManager;

    private DataListener dataListener;

    private final MutableLiveData<List<AppUser>> personnel = new MutableLiveData<>();

    public PersonnelListViewModel(PersonnelAccessManager accessManager,
                                  PersonnelDataManager dataManager,
                                  DataListener dataListener) {
        this.accessManager = accessManager;
        this.dataManager = dataManager;
        this.dataListener = dataListener;
    }

    protected PersonnelAccessManager getAccessManager() {
        return accessManager;
    }

    protected PersonnelDataManager getDataManager() {
        return dataManager;
    }

    protected DataListener getDataListener() {
        return dataListener;
    }

    public SingleLiveEvent<String> registerPersonnel(Single<String> emailDialog,
                                                     Single<Boolean> sendInvitationDialog,
                                                     boolean askForSendingInvite) {
        SingleLiveEvent<String> observer = new SingleLiveEvent<>();
        SafeReference<String> personnelEmail = new SafeReference<>();

        subscribe(
                emailDialog
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(email -> {
                            email = email.toLowerCase();
                            personnelEmail.set(email);
                            return Single.just(email);
                        })
                        .subscribeOn(getIOScheduler())
                        .observeOn(getIOScheduler())
                        .flatMap(email ->
                                getAccessManager()
                                        .createPersonnel(
                                                AppPrefs.gymOwnerId().getValue(),
                                                email))
                        .flatMap(isCreated ->
                                isCreated ?
                                        Single.just(askForSendingInvite) :
                                        Single.just(false))
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(doAsk -> doAsk ? sendInvitationDialog : Single.just(false))
                        .flatMap(doSendInvitation ->
                                doSendInvitation ?
                                        Single.just(personnelEmail.getValue()) :
                                        Single.error(new DialogCancelledException()))
                        .subscribeOn(getIOScheduler()),
                new SingleData<>(
                        observer::setValue,
                        getErrorHandler()::handleError));

        return observer;
    }

    public MutableLiveData<List<AppUser>> getPersonnel() {
        return personnel;
    }

    public void getPersonnelListData() {
        subscribeInIOThread(getDataManager().getPersonnelListAsync(),
                new SingleData<>(personnel::setValue, getErrorHandler()::handleError));
    }

    @Override
    public void startDataListener() {
        getDataListener().startDataListener();
    }

    @Override
    public void stopDataListener() {
        getDataListener().stopDataListener();
    }

    public void deleteItem(AppUser personnel) {
        if (personnel == null) {
            return;
        }

        subscribeInIOThread(
                getAccessManager()
                        .deletePersonnelCompletable(
                                AppPrefs.gymOwnerId().getValue(),
                                personnel.getEmail()),
                getErrorHandler()::handleError);
    }
}
