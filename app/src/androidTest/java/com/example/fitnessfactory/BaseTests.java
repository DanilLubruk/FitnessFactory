package com.example.fitnessfactory;

import static org.junit.Assert.fail;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

public class BaseTests {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    protected TestScheduler testScheduler;

    @Before
    public void setup() {
        testScheduler = new TestScheduler();
    }

    protected <T> TestObserver<T> subscribeInTestThread(Single<T> observable) {
        TestObserver<T> subscriber = observable
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .test();
        testScheduler.triggerActions();

        return subscriber;
    }

    protected <T> TestObserver<T> subscribe(Single<T> observable) {
        TestObserver<T> subscriber = observable.test();
        testScheduler.triggerActions();

        return subscriber;
    }

    protected TestObserver<Void> subscribe(Completable completable) {
        TestObserver<Void> subscriber = completable.test();
        testScheduler.triggerActions();

        return subscriber;
    }

    protected <T> void checkLiveDataNotSet(LiveData<T> liveData) {
        try {
            getOrAwaitValue(liveData);
            //no value should be emitted, 'cause the viewmodel is blank
            fail();
        } catch (RuntimeException exception) {
            //if the value not emitted, an exception is thrown
        }
    }

    protected <T> T getOrAwaitValue(final LiveData<T> liveData) {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        // Don't wait indefinitely if the LiveData is not set.
        try {
            if (!latch.await(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("LiveData value was never set.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        //noinspection unchecked
        return (T) data[0];
    }
}
