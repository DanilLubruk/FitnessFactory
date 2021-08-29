package com.example.fitnessfactory.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.MainActivity;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseAppFragment<A extends Activity> extends Fragment implements MainFragment {

    private final CompositeDisposable disposables = new CompositeDisposable();

    public abstract void closeProgress();

    public abstract void showProgress();

    protected int getContentViewId() {
        return 0;
    }

    private void unsubscribe() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    protected void addSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableAutofill();
    }

    private void disableAutofill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getBaseActivity().getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    protected <T> void subscribeInMainThread(Observable<T> subscriber, SingleData<T> observer) {
        addSubscription(subscriber
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer.getObserver()::onSuccess, observer.getObserver()::onError));
    }

    protected <T> void subscribeInMainThread(Single<T> subscriber, SingleData<T> observer) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public abstract A getBaseActivity();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean goToMainPage() {
        if (getBaseActivity() instanceof MainActivity) {
            ((MainActivity) getBaseActivity()).openMainPage();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }
}