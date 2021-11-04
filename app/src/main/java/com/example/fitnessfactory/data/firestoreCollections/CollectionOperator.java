package com.example.fitnessfactory.data.firestoreCollections;
import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.exceptions.NoDataException;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public abstract class CollectionOperator {

    private CollectionReference colReference;

    protected abstract String getRoot();

    protected CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    protected FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    private void initCollection() {
        colReference = FirebaseFirestore.getInstance().collection(getRoot());
    }

    protected <T> T getUniqueUserEntity(Query query, Class<T> clazz) throws Exception {
        return getUniqueEntity(query, clazz, getUserEmailNotUniqueMessage());
    }

    protected DocumentReference getUniqueUserEntityReference(Query query) throws Exception {
        return getUniqueEntityReference(query, getUserEmailNotUniqueMessage());
    }

    protected DocumentSnapshot getUniqueUserEntitySnapshot(Query query) throws Exception {
        return getUniqueEntitySnapshot(query, getUserEmailNotUniqueMessage());
    }

    protected <T> T getUniqueEntity(Query query, Class<T> clazz, String notUniqueMessage) throws Exception {
        return getUniqueEntitySnapshot(query, notUniqueMessage).toObject(clazz);
    }
    protected DocumentReference getUniqueEntityReference(Query query, String notUniqueMessage) throws Exception {
        return getUniqueEntitySnapshot(query, notUniqueMessage).getReference();
    }

    protected DocumentSnapshot getUniqueEntitySnapshot(Query query, String notUniqueMessage) throws Exception {
        List<DocumentSnapshot> documentSnapshots = Tasks.await(query.get()).getDocuments();

        checkDataEmpty(documentSnapshots);
        checkUniqueness(documentSnapshots, notUniqueMessage);

        return documentSnapshots.get(0);
    }

    protected <T> void checkDataEmpty(List<T> entities) throws Exception {
        if (entities.isEmpty()) {
            Log.d(AppConsts.DEBUG_TAG, "CollectionOperator.checkDataEmpty(): Empty data obtaining attempt");
            throw new NoDataException(ResUtils.getString(R.string.message_error_no_data));
        }
    }

    protected <T> void checkEmailUniqueness(List<T> entities) throws Exception {
        checkUniqueness(entities, getUserEmailNotUniqueMessage());
    }

    protected <T> void checkUniqueness(List<T> entities, String errorMessage) throws Exception {
        boolean isFieldNotUnique = entities.size() > 1;
        if (isFieldNotUnique) {
            Log.d(AppConsts.DEBUG_TAG, "CollectionOperator.checkUniqueness(): Entity uniqueness check failed");
            throw new Exception(errorMessage);
        }
    }

    private String getUserEmailNotUniqueMessage() {
        return ResUtils.getString(R.string.message_error_email_isnt_unique);
    }
}
