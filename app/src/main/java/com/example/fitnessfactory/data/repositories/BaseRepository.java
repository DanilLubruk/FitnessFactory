package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;
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

    protected int getEntitiesAmount(Query query) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> documents = Tasks.await(query.get()).getDocuments();

        return documents.size();
    }

    protected String getEntitySavingNullMessage() {
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
