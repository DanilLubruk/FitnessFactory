package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
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

public class BaseRepository {

    private CollectionReference colReference;

    protected String getRoot() {
        return AppPrefs.gymOwnerId().getValue();
    }

    protected CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    protected FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    public Completable commitBatchCompletable(WriteBatch writeBatch) {
        return Completable.create(emitter -> {
            commitBatch(emitter, writeBatch);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private void commitBatch(CompletableEmitter emitter, WriteBatch writeBatch) {
        try {
            Tasks.await(writeBatch.commit());
        } catch (ExecutionException e) {
            reportError(emitter, e);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        }
    }

    public Single<Boolean> commitBatchSingle(WriteBatch writeBatch) {
        return Single.create(emitter -> {
            boolean isCommitted = commitBatch(emitter, writeBatch);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isCommitted);
            }
        });
    }

    private boolean commitBatch(SingleEmitter<Boolean> emitter, WriteBatch writeBatch) {
        boolean isCommitted = false;
        try {
            Tasks.await(writeBatch.commit());
            isCommitted = true;
        } catch (ExecutionException e) {
            reportError(emitter, e);
        } catch (InterruptedException e) {
            reportError(emitter, e);
        }

        return isCommitted;
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
