package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

public class AdminsDataManager extends BaseManager {

    @Inject
    AdminsRepository adminsRepository;
    @Inject
    AdminsAccessRepository adminsAccessRepository;
    @Inject
    UserRepository userRepository;

    public AdminsDataManager() {
        FFApp.get().getAppComponent().inject(this);
    }

    public ListenerRegistration getAdminsListListener() {
        AtomicReference<ListenerRegistration> adminsListListener = new AtomicReference<>();

        addSubscription(adminsRepository.getAdminsEmailsAsync()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(adminsEmails -> adminsAccessRepository.getAdminsListListener(adminsEmails))
                .subscribe(
                        adminsListListener::set,
                        this::handleError));

        return adminsListListener.get();
    }

    public List<AppUser> getAdminsListData() {
        AtomicReference<List<AppUser>> users = new AtomicReference<>();

        addSubscription(adminsRepository.getAdminsEmailsAsync()
                .subscribeOn(getIOScheduler())
                .observeOn(getIOScheduler())
                .flatMap(adminsEmails -> userRepository.getUsersByEmailsAsync(adminsEmails))
                .subscribe(
                        users::set,
                        this::handleError));

        return users.get();
    }
}
