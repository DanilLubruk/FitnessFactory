package com.example.fitnessfactory;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;

import org.junit.Before;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

public class BaseTests {

    protected TestScheduler testScheduler;

    @Before
    public void setup() {
        testScheduler = new TestScheduler();
    }

    protected <T> TestObserver<T> subscribe(Single<T> observable) {
        TestObserver<T> subscriber = new TestObserver<>();
        observable
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .subscribe(subscriber);
        testScheduler.triggerActions();

        return subscriber;
    }
}
