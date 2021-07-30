package com.example.fitnessfactory.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleData;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected Unbinder unbinder;
    protected Bundle savedState = null;
    private CompositeDisposable disposables = new CompositeDisposable();
    private HashMap<Integer, Bundle> customBundles = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        savedState = savedInstanceState;
        initActivity();
        initToolbar();
        initComponents();
        disableAutofill();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

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

    protected void unBind() {
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void initToolbar() {
        setSupportActionBar(toolbar);
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
    protected void onSaveInstanceState(Bundle outState) {
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
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        unBind();
        unsubscribe();
        super.onDestroy();
    }

    protected <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
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
