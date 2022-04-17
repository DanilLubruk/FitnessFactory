package com.example.fitnessfactory.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleData;

import java.util.HashMap;
import icepick.Icepick;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    protected Bundle savedState = null;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private HashMap<Integer, Bundle> customBundles = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Icepick.restoreInstanceState(this, savedInstanceState);
        savedState = savedInstanceState;
        initActivity();
        initToolbar();
        initComponents();
        disableAutofill();
    }

    public abstract Toolbar getToolbar();

    private void disableAutofill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    public HashMap<Integer, Bundle> getCustomBundles() {
        return customBundles;
    }

    private void unsubscribe() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private void addSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void initToolbar() {
        setSupportActionBar(getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
        outState.putSerializable("customBundles", customBundles);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        customBundles = (HashMap<Integer, Bundle>) savedInstanceState.getSerializable("customBundles");
    }

    protected void initComponents() {

    }

    protected void initActivity() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // кнопка домой
        if (id == android.R.id.home) {
            finish();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void close() {
        finish();
    }

    @Override
    protected void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }

    protected <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected <T> void subscribeInMainThread(Completable subscriber, Consumer<Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, onError));
    }

    protected <T> void subscribeInMainThread(Completable subscriber, Action onComplete, Consumer<Throwable> onError) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError));
    }

    public <T> void subscribeInMainThread(Observable<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected <T> void subscribeInIOThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

}
