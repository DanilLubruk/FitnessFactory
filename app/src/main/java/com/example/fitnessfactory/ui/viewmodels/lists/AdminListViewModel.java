package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.StaffAccessRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AdminListViewModel extends BaseViewModel {

    @Inject
    StaffAccessRepository accessRepository;
    @Inject
    UserRepository userRepository;

    private MutableLiveData<List<AppUser>> admins = new MutableLiveData<>();

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
                },this::handleError));
    }

    public void addAdminsListListener() {
        subscribeInIOThread(
                accessRepository.addAdminsListListener(AppPrefs.gymOwnerId().getValue()),
                this::handleError);
    }

    public MutableLiveData<List<AppUser>> getAdmins() {
        return admins;
    }

    public void getAdminsListData() {
        addSubscription(accessRepository.getAdminsEmailsByOwnerId(AppPrefs.gymOwnerId().getValue())
                .observeOn(getIOScheduler())
                .subscribeOn(getIOScheduler())
                .flatMap(adminsEmails -> userRepository.getAdminsByEmails(adminsEmails))
                .observeOn(getMainThreadScheduler())
                .subscribe(admins::setValue,
                        this::handleError));
    }

    public void removeAdminsListListener() {
        subscribeInIOThread(accessRepository.removeAdminsListListener(),
                this::handleError);
    }

    public void deleteAdmin(String email) {
        subscribeInIOThread(
                accessRepository.deleteAdminCompletable(AppPrefs.gymOwnerId().getValue(), email),
                this::handleError);
    }
}
