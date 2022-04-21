package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.models.OrganisationData;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.EmailValueChecker;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class OrganisationInfoRepository extends BaseRepository {

    public Single<Boolean> saveOrganisationInfoAsync(OrganisationData organisationData) {
        return SingleCreate(emitter -> {
            boolean isDeleted = saveOrganisationInfo(organisationData);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isDeleted);
            }
        });
    }

    private boolean saveOrganisationInfo(OrganisationData organisationData) throws Exception {
        organisationData.trimStrings();
        Tasks.await(
                getCollection().document(
                        FirestoreCollections.ORGANISATION_DATA)
                        .set(organisationData));

        return true;
    }

    public Single<OrganisationData> getOrganisationDataAsync() {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getOrganisationData());
            }
        });
    }

    private OrganisationData getOrganisationData() throws ExecutionException, InterruptedException {
        return Tasks.await(
                getCollection().document(
                        FirestoreCollections.ORGANISATION_DATA).get())
                .toObject(OrganisationData.class);
    }

    public Single<String> getOrganisationNameAsync() {
        return SingleCreate(emitter -> {
            String organisationName = getOrganisationName();

            if (!emitter.isDisposed()) {
                emitter.onSuccess(organisationName);
            }
        });
    }

    private String getOrganisationName() throws ExecutionException, InterruptedException {
        DocumentReference document = getCollection().document(FirestoreCollections.ORGANISATION_DATA);
        DocumentSnapshot snapshot = Tasks.await(document.get());
        OrganisationData organisationData = snapshot.toObject(OrganisationData.class);

        if (organisationData == null) {
            document.set(new OrganisationData());
            return "";
        } else {
            return organisationData.getName();
        }
    }

    public Completable checkOrganisationNameAsync(String organisationName) {
        return CompletableCreate(emitter -> {
            checkOrganisationName(organisationName);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void checkOrganisationName(String organisationName) {
        if (StringUtils.isEmpty(organisationName)) {
            AppPrefs.askForOrganisationName().setValue(true);
        } else {
            AppPrefs.organisationName().setValue(organisationName);
        }
    }

    public Completable setOrganisationNameAsync(String organisationName) {
        return CompletableCreate(emitter -> {
            setOrganisationName(organisationName);

           if (!emitter.isDisposed()) {
               emitter.onComplete();
           }
        });
    }

    private void setOrganisationName(String organisationName) throws ExecutionException, InterruptedException {
        DocumentReference document = Tasks.await(getCollection().document(FirestoreCollections.ORGANISATION_DATA).get()).getReference();
        document.update(OrganisationData.NAME_FIELD, organisationName);
        AppPrefs.organisationName().setValue(organisationName);
    }
}
