package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.OrganisationData;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class OrganisationInfoViewModel extends EditorViewModel {

    @Inject
    OrganisationInfoRepository organisationInfoRepository;

    private OrganisationData dbOrganisationData;
    public ObservableField<OrganisationData> organisation = new ObservableField<>();

    private final String NAME_KEY = "NAME_KEY";
    private final String ADDRESS_KEY = "ADDRESS_KEY";
    private final String EMAIL_KEY = "EMAIL_KEY";
    private final String PHONE_KEY = "PHONE_KEY";
    private final String TAX_ID_KEY = "TAX_ID_KEY";
    private final String BANK_DETAILS_KEY = "BANK_DETAILS_KEY";

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
            organisationData = new OrganisationData();
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
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();

        OrganisationData organisationData = organisation.get();
        if (OrganisationData.isNotNull(organisationData)) {
            isModified.setValue(!organisationData.equals(dbOrganisationData));
        } else {
            isModified.setValue(false);
        }

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        OrganisationData organisationData = organisation.get();
        if (organisationData == null) {
            return handleItemSavingNullError(observer);
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
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_org_data_null));
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
        getHandle().put(ADDRESS_KEY, organisationData.getAddress());
        getHandle().put(EMAIL_KEY, organisationData.getEmail());
        getHandle().put(PHONE_KEY, organisationData.getPhone());
        getHandle().put(TAX_ID_KEY, organisationData.getTaxId());
        getHandle().put(BANK_DETAILS_KEY, organisationData.getBankDetails());
    }

    private void saveInfoDbState() {
        if (dbOrganisationData == null) {
            return;
        }
        getHandle().put(OrganisationData.NAME_FIELD, dbOrganisationData.getName());
        getHandle().put(OrganisationData.ADDRESS_FIELD, dbOrganisationData.getAddress());
        getHandle().put(OrganisationData.EMAIL_FIELD, dbOrganisationData.getEmail());
        getHandle().put(OrganisationData.PHONE_FIELD, dbOrganisationData.getPhone());
        getHandle().put(OrganisationData.TAX_ID_FIELD, dbOrganisationData.getTaxId());
        getHandle().put(OrganisationData.BANK_DETAILS_FIELD, dbOrganisationData.getBankDetails());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
    }

    private void setHandleState(OrganisationData organisationData) {
        if (organisationData == null) {
            handleItemObtainingNullError();
            return;
        }
        organisationData.setName((String) getHandle().get(NAME_KEY));
        organisationData.setAddress((String) getHandle().get(ADDRESS_KEY));
        organisationData.setEmail((String) getHandle().get(EMAIL_KEY));
        organisationData.setPhone((String) getHandle().get(PHONE_KEY));
        organisationData.setTaxId((String) getHandle().get(TAX_ID_KEY));
        organisationData.setBankDetails((String) getHandle().get(BANK_DETAILS_KEY));
    }

    private void setInfoDbState() {
        if (dbOrganisationData == null) {
            return;
        }
        dbOrganisationData.setName((String) getHandle().get(OrganisationData.NAME_FIELD));
        dbOrganisationData.setAddress((String) getHandle().get(OrganisationData.ADDRESS_FIELD));
        dbOrganisationData.setEmail((String) getHandle().get(OrganisationData.EMAIL_FIELD));
        dbOrganisationData.setPhone((String) getHandle().get(OrganisationData.PHONE_FIELD));
        dbOrganisationData.setTaxId((String) getHandle().get(OrganisationData.TAX_ID_FIELD));
        dbOrganisationData.setBankDetails((String) getHandle().get(OrganisationData.BANK_DETAILS_FIELD));
    }
}
