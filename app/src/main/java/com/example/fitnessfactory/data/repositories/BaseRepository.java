package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.CompletableEmitter;
import io.reactivex.SingleEmitter;

public class BaseRepository {

    private CollectionReference colReference;

    protected String getRoot() {
        return AppPrefs.gymOwnerId().getValue();
    }

    protected CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    private void initCollection() {
        colReference = FirebaseFirestore.getInstance().collection(getRoot());
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

    protected  <T> void reportError(SingleEmitter<T> emitter, Exception error) {
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
