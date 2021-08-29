package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.BaseCollection;
import com.example.fitnessfactory.data.firestoreCollections.CollectionOperator;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

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
            } catch (Exception exception) {
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
            throw new Exception(ResUtils.getString(R.string.message_error_no_data));
        }
    }

    protected <T> void checkEmailUniqueness(List<T> entities) throws Exception {
        checkUniqueness(entities, ResUtils.getString(R.string.message_error_email_isnt_unique));
    }

    protected <T> void checkUniqueness(List<T> entities, String errorMessage) throws Exception {
        boolean isFieldNotUnique = entities.size() > 1;
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
