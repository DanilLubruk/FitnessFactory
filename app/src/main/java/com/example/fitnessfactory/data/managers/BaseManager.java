package com.example.fitnessfactory.data.managers;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseManager {

    private final RxManager rxManager = new RxManager();

    protected Scheduler getMainThreadScheduler() {
        return rxManager.getMainThreadScheduler();
    }

    protected Scheduler getIOScheduler() {
        return rxManager.getIOScheduler();
    }

    public void setMainThreadScheduler(Scheduler mainThreadScheduler) {
        rxManager.setMainThreadScheduler(mainThreadScheduler);
    }

    public void setIOScheduler(Scheduler ioScheduler) {
        rxManager.setIOScheduler(ioScheduler);
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
        } catch (ExecutionException | InterruptedException e) {
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
        } catch (ExecutionException | InterruptedException e) {
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
}
