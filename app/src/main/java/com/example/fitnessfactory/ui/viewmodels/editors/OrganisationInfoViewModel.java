package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.OrganisationData;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;

import javax.inject.Inject;

public class OrganisationInfoViewModel extends EditorViewModel {

    @Inject
    OrganisationInfoRepository organisationInfoRepository;

    private OrganisationData dbOrganisationData;
    public ObservableField<OrganisationData> organisation = new ObservableField<>();

    private final String NAME_KEY = "NAME_KEY";

    public OrganisationInfoViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<OrganisationData> getData() {
        SingleLiveEvent<OrganisationData> observer = new SingleLiveEvent<>();

        subscribeInIOThread(
                organisationInfoRepository.getOrganisationDataAsync(),
                new SingleData<>(
                        observer::setValue,
                        getErrorHandler()::handleError));

        return observer;
    }

    public void setData(OrganisationData organisationData) {
        if (organisationData == null) {
            return;
        }
        if (dbOrganisationData == null) {
            dbOrganisationData = new OrganisationData();
            dbOrganisationData.copy(organisationData);
        }
        if (hasHandle()) {
            setHandleState(organisationData);
            setInfoDbState();
        }

        organisation.set(organisationData);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();
        observer.setValue(false);

        OrganisationData organisationData = organisation.get();
        if (organisationData != null && organisationData.getName() != null) {
            boolean isModified = !organisationData.equals(dbOrganisationData);
            observer.setValue(isModified);
        }

        return observer;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        OrganisationData organisationData = organisation.get();
        if (organisationData == null) {
            observer.setValue(false);
            return observer;
        }

        subscribeInIOThread(
                organisationInfoRepository.saveOrganisationInfoAsync(organisationData),
                new SingleData<>(
                        observer::setValue,
                        throwable -> getErrorHandler().handleError(observer, throwable)));

        return observer;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();
        observer.setValue(true);

        return observer;
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        saveInfoState();
        saveInfoDbState();
    }

    private void saveInfoState() {
        OrganisationData organisationData = this.organisation.get();
        if (organisationData == null) {
            return;
        }
        getHandle().put(NAME_KEY, organisationData.getName());
    }

    private void saveInfoDbState() {
        if (dbOrganisationData == null) {
            return;
        }
        getHandle().put(OrganisationData.NAME_FIELD, dbOrganisationData.getName());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
    }

    private void setHandleState(OrganisationData organisationData) {
        if (organisationData == null) {
            return;
        }
        organisationData.setName((String) getHandle().get(NAME_KEY));
    }

    private void setInfoDbState() {
        if (dbOrganisationData == null) {
            return;
        }
        dbOrganisationData.setName((String) getHandle().get(OrganisationData.NAME_FIELD));
    }
}
