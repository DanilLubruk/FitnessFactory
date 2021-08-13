package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.Admin;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class AdminsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return super.getRoot() +
                "/" +
                FirestoreCollections.ADMINS +
                "/" +
                FirestoreCollections.ADMINS;
    }

    public Single<WriteBatch> deleteAdminAsync(WriteBatch writeBatch, String adminEmail) {
        return Single.create(emitter -> {
            deleteAdmin(emitter, writeBatch, adminEmail);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(writeBatch);
            }
        });
    }

    private void deleteAdmin(SingleEmitter<WriteBatch> emitter,
                                   WriteBatch writeBatch,
                                   String adminEmail) {
        try {
            deleteAdmin(writeBatch, adminEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }
    }

    private void deleteAdmin(WriteBatch writeBatch, String adminEmail) throws Exception {
        writeBatch.delete(getAdminDocument(adminEmail));
    }

    private DocumentReference getAdminDocument(String adminEmail) throws Exception {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, adminEmail).get()).getDocuments();

        checkEmailUniqueness(documentSnapshots);

        return documentSnapshots.get(0).getReference();
    }

    public Single<List<String>> getAdminsEmailsAsync() {
        return Single.create(emitter -> {
            List<String> adminsEmails = getAdminsEmails(emitter);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(adminsEmails);
            }
        });
    }

    private List<String> getAdminsEmails(SingleEmitter<List<String>> emitter) {
        List<String> adminsEmails = new ArrayList<>();

        try {
            adminsEmails = getAdminsEmails();
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return adminsEmails;
    }

    private List<String> getAdminsEmails() throws ExecutionException, InterruptedException {
        List<Admin> admins = Tasks.await(getCollection().get()).toObjects(Admin.class);

        List<String> adminsEmails = new ArrayList<>();
        for (Admin admin : admins) {
            adminsEmails.add(admin.getUserEmail());
        }

        return adminsEmails;
    }

    public Single<WriteBatch> addAdminAsync(String userEmail, WriteBatch writeBatch) {
        return Single.create(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(addAdmin(userEmail, writeBatch));
           }
        });
    }

    private WriteBatch addAdmin(String userEmail, WriteBatch writeBatch) {
        DocumentReference documentReference = getCollection().document();
        Admin admin = new Admin();
        admin.setUserEmail(userEmail);

        return writeBatch.set(documentReference, admin);
    }

    public Single<Boolean> isAdminAddedAsync(String userEmail) {
        return Single.create(emitter -> {
           boolean isAdminAdded = isAdminAdded(emitter, userEmail);

           if (!emitter.isDisposed()) {
               emitter.onSuccess(isAdminAdded);
           }
        });
    }

    private boolean isAdminAdded(SingleEmitter<Boolean> emitter, String userEmail) {
        boolean isAdminAdded = false;
        try {
            isAdminAdded = isAdminAdded(userEmail);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        } catch (Exception e) {
            reportError(emitter, e);
        }

        return isAdminAdded;
    }

    private boolean isAdminAdded(String userEmail) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> admins =
                Tasks.await(getCollection().whereEqualTo(Admin.USER_EMAIL_FIELD, userEmail).get()).getDocuments();
        boolean isAdminWithThisEmailAdded = admins.size() > 0;

        return isAdminWithThisEmailAdded;
    }
}
