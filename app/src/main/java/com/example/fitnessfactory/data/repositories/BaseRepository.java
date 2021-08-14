package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.Write;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public abstract class BaseRepository extends CollectionOperator {

    @Override
    protected String getRoot() {
        return BaseCollection.getRoot();
    }

    protected void checkEmailUniqueness(List<DocumentSnapshot> documents) throws Exception {
        checkUniqueness(documents, ResUtils.getString(R.string.message_error_email_isnt_unique));
    }

    protected void checkUniqueness(List<DocumentSnapshot> documents, String errorMessage) throws Exception {
        boolean isFieldNotUnique = documents.size() > 1;
        if (isFieldNotUnique) {
            throw new Exception(errorMessage);
        }
    }

    protected <T> void reportError(SingleEmitter<T> emitter, Exception error) {
        if (!emitter.isDisposed()) {
            emitter.onError(error);
        }
    }

    protected void reportError(CompletableEmitter emitter, Exception error) {
        if (!emitter.isDisposed()) {
            emitter.onError(error);
        }
    }
}
