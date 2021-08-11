package com.example.fitnessfactory.data.repositories.bondingRepositories;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.AccessRepository;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.google.firebase.firestore.ListenerRegistration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class AdminAccessRepository extends BaseRepository {

    @Inject
    AccessRepository accessRepository;
    @Inject
    UserRepository userRepository;

    private ListenerRegistration gymAdminsListListener;

    public AdminAccessRepository() {
        FFApp.get().getAppComponent().inject(this);
    }

    public Completable addGymAdminsListListener(String ownerId, String gymId) {
        return Completable.create(emitter -> {
            gymAdminsListListener = accessRepository.getPersonnelQueryByGymId(ownerId, gymId)
                    .addSnapshotListener(((value, error) -> {
                        if (error != null) {
                            reportError(emitter, error);
                        }

                        EventBus.getDefault().post(new GymAdminsListListenerEvent());
                    }));

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    public Completable removeGymAdminsListListener() {
        return Completable.create(emitter -> {
            if (gymAdminsListListener != null) {
                gymAdminsListListener.remove();
            }

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    public Single<List<AppUser>> getAdminsByGymIdAsync(String ownerId, String gymId) {
        return Single.create(emitter -> {
            List<AppUser> admins = getAdminsByGymId(emitter, ownerId, gymId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(admins);
            }
        });
    }

    private List<AppUser> getAdminsByGymId(SingleEmitter<List<AppUser>> emitter,
                                           String ownerId,
                                           String gymId) {
        List<AppUser> admins = new ArrayList<>();

        try {
            admins = getAdminsByGymId(ownerId, gymId);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return admins;
    }

    private List<AppUser> getAdminsByGymId(String ownerId, String gymId) throws Exception {
        List<String> adminsEmails = accessRepository.getAdminEmailsByGymId(ownerId, gymId);

        return userRepository.getAdminsList(adminsEmails);
    }
}
