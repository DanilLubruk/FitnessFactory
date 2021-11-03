package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.data.SessionTypesDataManager;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class SessionTypeEditorViewModel extends EditorViewModel {

    private final SessionTypesDataManager sessionTypesDataManager;
    private final SessionTypeRepository sessionTypeRepository;

    public ObservableField<SessionType> sessionType = new ObservableField<>();
    private SessionType dbSessionType;

    private final String DB_ID_KEY = "DB_ID_KEY";
    private final String DB_NAME_KEY = "DB_NAME_KEY";
    private final String DB_PEOPLE_AMOUNT_KEY = "DB_PEOPLE_AMOUNT_KEY";
    private final String DB_PRICE_KEY = "DB_PRICE_KEY";

    @Inject
    public SessionTypeEditorViewModel(SessionTypesDataManager sessionTypesDataManager,
                                      SessionTypeRepository sessionTypeRepository) {
        this.sessionTypesDataManager = sessionTypesDataManager;
        this.sessionTypeRepository = sessionTypeRepository;
    }

    public void getSessionType(String typeId) {
        subscribeInIOThread(
                sessionTypeRepository.getSessionTypeAsync(typeId),
                new SingleData<>(
                        this::setSessionType,
                        getErrorHandler()::handleError));
    }

    private void setSessionType(SessionType sessionType) {
        if (sessionType == null) {
            sessionType = new SessionType();
        }
        if (dbSessionType == null) {
            dbSessionType = new SessionType();
            dbSessionType.copy(sessionType);
        }
        if (hasHandle()) {
            setHandleState(sessionType);
            setDbHandleState();
        }

        this.sessionType.set(sessionType);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();

        SessionType sessionType = this.sessionType.get();
        if (SessionType.isNotNull(sessionType)) {
            isModified.setValue(!sessionType.equals(dbSessionType));
        } else {
            isModified.setValue(false);
        }

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        SessionType sessionType = this.sessionType.get();
        if (sessionType == null) {
            return handleItemSavingNullError(observer);
        }

        subscribeInIOThread(
                sessionTypeRepository.saveAsync(sessionType),
                new SingleData<>(
                        isSaved -> {
                            dbSessionType.copy(sessionType);
                            observer.setValue(isSaved);
                        },
                        throwable -> getErrorHandler().handleError(observer, throwable)));

        return observer;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        SessionType sessionType = this.sessionType.get();
        if (sessionType == null) {
            return handleItemDeletingNullError(observer);
        }

        subscribeInIOThread(
                sessionTypesDataManager.deleteSessionTypeSingle(sessionType),
                new SingleData<>(
                        observer::setValue,
                        throwable -> getErrorHandler().handleError(observer, throwable)));

        return observer;
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        saveSessionTypeState();
        saveDbSessionTypeState();
    }

    private void saveSessionTypeState() {
        SessionType sessionType = this.sessionType.get();
        if (sessionType == null) {
            return;
        }
        getHandle().put(SessionType.ID_FIELD, sessionType.getId());
        getHandle().put(SessionType.NAME_FIELD, sessionType.getName());
        getHandle().put(SessionType.PEOPLE_AMOUNT_FIELD, sessionType.getPeopleAmount());
        getHandle().put(SessionType.PRICE_FIELD, sessionType.getPrice());
    }

    private void saveDbSessionTypeState() {
        if (dbSessionType == null) {
            return;
        }
        getHandle().put(DB_ID_KEY, dbSessionType.getId());
        getHandle().put(DB_NAME_KEY, dbSessionType.getName());
        getHandle().put(DB_PEOPLE_AMOUNT_KEY, dbSessionType.getPeopleAmount());
        getHandle().put(DB_PRICE_KEY, dbSessionType.getPrice());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
    }

    private void setHandleState(SessionType sessionType) {
        if (sessionType == null) {
            sessionType = new SessionType();
        }

        final SessionType entity = new SessionType();
        entity.setId((String) getHandle().get(SessionType.ID_FIELD));
        entity.setName((String) getHandle().get(SessionType.NAME_FIELD));
        handleNullPointerException(
                () -> entity.setPeopleAmount((Integer) getHandle().get(SessionType.PEOPLE_AMOUNT_FIELD)),
                getPeopleAmountErrorMessage());
        handleNullPointerException(
                () -> entity.setPrice((Float) getHandle().get(SessionType.PRICE_FIELD)),
                getPriceErrorMessage());

        sessionType.copy(entity);
    }

    private void setDbHandleState() {
        if (dbSessionType == null) {
            dbSessionType = new SessionType();
        }

        final SessionType entity = new SessionType();
        entity.setId((String) getHandle().get(DB_ID_KEY));
        entity.setName((String) getHandle().get(DB_NAME_KEY));
        handleNullPointerException(
                () -> entity.setPeopleAmount((Integer) getHandle().get(DB_PEOPLE_AMOUNT_KEY)),
                getPeopleAmountErrorMessage());
        handleNullPointerException(
                () -> entity.setPrice((Float) getHandle().get(DB_PRICE_KEY)),
                getPriceErrorMessage());

        dbSessionType.copy(entity);
    }

    private String getPeopleAmountErrorMessage() {
        return ResUtils.getString(R.string.message_error_restoring_session_type)
                .concat(getErrorMessageBreak())
                .concat(ResUtils.getString(R.string.message_error_people_amount_null));
    }

    private String getPriceErrorMessage() {
        return ResUtils.getString(R.string.message_error_restoring_session_type)
                .concat(getErrorMessageBreak())
                .concat(ResUtils.getString(R.string.message_error_price_null));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_session_type_null));
    }
}
