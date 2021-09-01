package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.CoachesDataManager;
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

public class CoachesListViewModel extends BaseViewModel implements DataListListener<AppUser> {

    @Inject
    CoachesListDataListener dataListener;
    @Inject
    CoachesDataManager dataManager;
    @Inject
    CoachesAccessManager accessManager;

    private MutableLiveData<List<AppUser>> coaches = new MutableLiveData<>();

    public CoachesListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<String> registerCoach(Single<String> emailDialog, Single<Boolean> sendInvitationDialog) {
        SingleLiveEvent<String> observer = new SingleLiveEvent<>();
        SafeReference<String> coachEmail = new SafeReference<>();

        subscribe(
                emailDialog
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(email -> {
                            email = email.toLowerCase();
                            coachEmail.set(email);
                            return Single.just(email);
                        })
                        .subscribeOn(getIOScheduler())
                        .observeOn(getIOScheduler())
                        .flatMap(email -> accessManager.createCoach(AppPrefs.gymOwnerId().getValue(), email))
                        .flatMap(isCreated ->
                                isCreated ?
                                        Single.just(AppPrefs.askForSendingCoachEmailInvite().getValue()) :
                                        Single.just(false))
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(doAsk -> doAsk ? sendInvitationDialog : Single.just(false))
                        .flatMap(doSendInvitation ->
                                doSendInvitation ?
                                        Single.just(coachEmail.getValue()) :
                                        Single.error(new DialogCancelledException()))
                        .subscribeOn(getIOScheduler()),
                new SingleData<>(
                        observer::setValue,
                        RxUtils::handleError));

        return observer;
    }

    public MutableLiveData<List<AppUser>> getCoaches() {
        return coaches;
    }

    public void getCoachesData() {
        subscribeInIOThread(
                dataManager.getCoachesListAsync(),
                new SingleData<>(coaches::setValue, RxUtils::handleError));
    }

    @Override
    public void deleteItem(AppUser appUser) {
        subscribeInIOThread(
                accessManager.deleteCoachCompletable(AppPrefs.gymOwnerId().getValue(), appUser.getEmail()),
                RxUtils::handleError);
    }

    @Override
    public void startDataListener() {
        dataListener.startDataListener();
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }
}
