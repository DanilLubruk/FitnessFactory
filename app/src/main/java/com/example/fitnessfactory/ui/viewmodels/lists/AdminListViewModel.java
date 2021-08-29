package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.RxUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AdminListViewModel extends BaseViewModel {

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

        subscribe(
                adminsAccessManager.registerAdmin(emailDialog, sendInvitationDialog),
                new SingleData<>(
                        observer::setValue,
                        RxUtils::handleError));

        return observer;
    }

    public void addAdminsListListener() {
        adminsListListener.setAdminsListListener();
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void getAdminsListData() {
        subscribeInIOThread(adminsDataManager.getAdminsListAsync(),
                new SingleData<>(admins::setValue, RxUtils::handleError));
    }

    public void removeAdminsListListener() {
        adminsListListener.removeDataListener();
    }

    public void deleteAdmin(String email) {
        subscribeInIOThread(
                adminsAccessManager.deleteAdminCompletable(AppPrefs.gymOwnerId().getValue(), email),
                RxUtils::handleError);
    }
}
