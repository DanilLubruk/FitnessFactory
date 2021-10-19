package com.example.fitnessfactory.data.repositories;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.exceptions.NoDataException;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public abstract class BaseRepository extends CollectionOperator {

    @Override
    protected String getRoot() {
        return BaseCollection.getRoot();
    }

    protected <T> Single<T> SingleCreate(SingleSubscriber<T> singleSubscriber) {
        return Single.create(getSingleOnSubscribe(singleSubscriber));
    }

    private <T> SingleOnSubscribe<T> getSingleOnSubscribe(SingleSubscriber<T> singleSubscriber) {
        return (emitter -> {
            try {
                singleSubscriber.onSubscribe(emitter);
            } catch (InterruptedException exception) {
                reportError(emitter, exception);
            }
            catch (Exception exception) {
                reportError(emitter, exception);
            }
        });
    }

    protected interface SingleSubscriber<T> {
        void onSubscribe(SingleEmitter<T> emitter) throws Exception;
    }

    protected Completable CompletableCreate(CompletableSubscriber completableSubscriber) {
        return Completable.create(getCompletableOnSubscribe(completableSubscriber));
    }

    private CompletableOnSubscribe getCompletableOnSubscribe(CompletableSubscriber completableSubscriber) {
        return (emitter -> {
            try {
                completableSubscriber.onSubscribe(emitter);
            } catch (InterruptedException exception) {
                reportError(emitter, exception);
            } catch (Exception exception) {
                reportError(emitter, exception);
            }
        });
    }

    protected interface CompletableSubscriber {
        void onSubscribe(CompletableEmitter emitter) throws Exception;
    }

    protected <T> void checkDataEmpty(List<T> entities) throws Exception {
        if (entities.isEmpty()) {
            Log.d(AppConsts.DEBUG_TAG, "Base repository.checkDataEmpty(): Empty data obtaining attempt");
            throw new NoDataException(ResUtils.getString(R.string.message_error_no_data));
        }
    }

    protected <T> void checkEmailUniqueness(List<T> entities) throws Exception {
        checkUniqueness(entities, getUserEmailNotUniqueMessage());
    }

    protected <T> void checkUniqueness(List<T> entities, String errorMessage) throws Exception {
        boolean isFieldNotUnique = entities.size() > 1;
        if (isFieldNotUnique) {
            Log.d(AppConsts.DEBUG_TAG, "Base repository.checkUniqueness(): Entity uniqueness check failed");
            throw new Exception(errorMessage);
        }
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

    protected int getEntitiesAmount(Query query) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> documents = Tasks.await(query.get()).getDocuments();

        return documents.size();
    }

    private String getUserEmailNotUniqueMessage() {
        return ResUtils.getString(R.string.message_error_email_isnt_unique);
    }

    protected String getEntityNullMessage() {
        return ResUtils.getString(R.string.message_error_data_save)
                .concat(" - ");
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
