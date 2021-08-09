package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.models.OrganisationData;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class OrganisationInfoRepository extends BaseRepository {

    public Single<String> getOrganisationNameAsync() {
        return Single.create(emitter -> {
            String organisationName = "";
            try {
                organisationName = getOrganisationName();
            } catch (InterruptedException e) {
                emitter.onError(e);
            } catch (Exception e) {
                emitter.onError(e);
            }

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
        return Completable.create(emitter -> {
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
        return Completable.create(emitter -> {
            try {
                setOrganisationName(organisationName);
            } catch (InterruptedException e) {
                emitter.onError(e);
            } catch (Exception e) {
                emitter.onError(e);
            }

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
