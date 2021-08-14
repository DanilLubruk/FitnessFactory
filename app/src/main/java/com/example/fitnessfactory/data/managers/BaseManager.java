package com.example.fitnessfactory.data.managers;

import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.GuiUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseManager {

    private Disposable disposable;

    private void unsubscribe() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    protected void addSubscription(Disposable disposable) {
        this.disposable = disposable;
    }

    protected Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    protected Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    protected Completable commitBatchCompletable(WriteBatch writeBatch) {
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

    protected Single<Boolean> commitBatchSingle(WriteBatch writeBatch) {
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

    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    protected void handleError(AtomicReference<Boolean> observer, Throwable throwable) {
        observer.set(false);
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }
}
