package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.Date;

import javax.inject.Inject;

public class SessionEditorViewModel extends EditorViewModel {

    private final SessionsDataManager sessionsDataManager;
    private final SessionsRepository sessionsRepository;

    public ObservableField<Session> session = new ObservableField<>();
    public MutableLiveData<String> sessionId = new MutableLiveData<>();
    private Session dbSession;

    @Inject
    public SessionEditorViewModel(SessionsDataManager sessionsDataManager,
                                  SessionsRepository sessionsRepository) {
        this.sessionsDataManager = sessionsDataManager;
        this.sessionsRepository = sessionsRepository;
    }

    public LiveData<String> getSessionId() {
        return sessionId;
    }

    public SingleLiveEvent<Boolean> getSession(String sessionId) {
        SingleLiveEvent<Boolean> isObtained = new SingleLiveEvent<>();

        subscribeInIOThread(
                sessionsRepository.getSessionAsync(sessionId),
                new SingleData<>(
                        obtainedSession -> setSession(obtainedSession, isObtained),
                        getErrorHandler()::handleError));

        return isObtained;
    }

    private void setSession(Session session, SingleLiveEvent<Boolean> isObtained) {
        if (session == null) {
            session = new Session();
        }
        if (dbSession == null) {
            dbSession = new Session();
            dbSession.copy(session);
        }
        if (hasHandle()) {
            //restoreHandleState
        }

        this.session.set(session);
        this.sessionId.setValue(session.getId());
        isObtained.setValue(true);
    }

    public void setSessionDefaultTime(Date defaultDate) {
        setSessionDate(defaultDate);
        setSessionStartTime(defaultDate);
        setSessionEndTime(defaultDate);
    }

    public void changeSessionDate(SingleDialogEvent<Date, Date> dialogEvent) {
        Session session = this.session.get();

        if (session == null) {
            handleItemOperationError();
            return;
        }

        subscribeInMainThread(
                dialogEvent.showDialog(session.getDate()),
                new SingleData<>(this::setSessionDate, getErrorHandler()::handleError));
    }

    private void setSessionDate(Date date) {
        Session session = this.session.get();
        if (session == null) {
            handleItemOperationError();
            return;
        }

        session.setDate(date);
        this.session.notifyChange();
    }

    public void changeSessionStartTime(SingleDialogEvent<Date, Date> dialogEvent) {
        Session session = this.session.get();

        if (session == null) {
            handleItemOperationError();
            return;
        }

        addSubscription(
                dialogEvent.showDialog(session.getStartTime())
                .subscribeOn(getMainThreadScheduler())
                .observeOn(getMainThreadScheduler())
                .flatMap(startTime ->
                        sessionsRepository.checkSessionStartTimeCorrectAsync(
                                startTime,
                                session.getEndTime()))
                .subscribeOn(getIOScheduler())
                .observeOn(getMainThreadScheduler())
                .subscribe(this::setSessionStartTime, getErrorHandler()::handleError));
    }

    private void setSessionStartTime(Date date) {
        Session session = this.session.get();
        if (session == null) {
            handleItemOperationError();
            return;
        }

        session.setStartTime(date);
        this.session.notifyChange();
    }

    public void changeSessionEndTime(SingleDialogEvent<Date, Date> dialogEvent) {
        Session session = this.session.get();

        if (session == null) {
            handleItemOperationError();
            return;
        }

        addSubscription(
                dialogEvent.showDialog(session.getStartTime())
                        .subscribeOn(getMainThreadScheduler())
                        .observeOn(getMainThreadScheduler())
                        .flatMap(endTime ->
                                sessionsRepository.checkSessionEndTimeCorrectAsync(
                                        session.getStartTime(),
                                        endTime))
                        .subscribeOn(getIOScheduler())
                        .observeOn(getMainThreadScheduler())
                        .subscribe(this::setSessionEndTime, getErrorHandler()::handleError));
    }

    private void setSessionEndTime(Date date) {
        Session session = this.session.get();
        if (session == null) {
            handleItemOperationError();
            return;
        }

        session.setEndTime(date);
        this.session.notifyChange();
    }

    public void setGym(String gymName) {
        if (StringUtils.isEmpty(gymName)) {
            handleGymNullError();
            return;
        }
        Session session = this.session.get();
        if (session == null) {
            handleItemOperationError();
            return;
        }

        session.setGymName(gymName);
        this.session.notifyChange();
    }

    private void handleGymNullError() {
        GuiUtils.showMessage(ResUtils.getString(R.string.message_error_gym_null));
    }

    public void setSessionType(String sessionTypeName) {
        if (StringUtils.isEmpty(sessionTypeName)) {
            handleSessionTypeNullError();
            return;
        }
        Session session = this.session.get();
        if (session == null) {
            handleItemOperationError();
            return;
        }

        session.setSessionTypeName(sessionTypeName);
        this.session.notifyChange();
    }

    private void handleSessionTypeNullError() {
        GuiUtils.showMessage(ResUtils.getString(R.string.message_session_type_null));
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();

        Session session = this.session.get();
        if (Session.isNotNull(session)) {
            isModified.setValue(!session.equals(dbSession));
        } else {
            isModified.setValue(false);
        }

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> isSaved = new SingleLiveEvent<>();

        Session session = this.session.get();
        if (session == null) {
            return handleItemSavingNullError(isSaved);
        }

        subscribeInIOThread(
                sessionsRepository.saveAsync(session),
                new SingleData<>(
                        isSavedResult -> {
                            dbSession.copy(session);
                            if (!session.getId().equals(sessionId.getValue())) {
                                sessionId.setValue(session.getId());
                            }
                            isSaved.setValue(isSavedResult);
                        },
                        throwable -> getErrorHandler().handleError(isSaved, throwable)));


        return isSaved;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> isDeleted = new SingleLiveEvent<>();

        Session session = this.session.get();
        if (session == null) {
            return handleItemDeletingNullError(isDeleted);
        }

        subscribeInIOThread(
                sessionsDataManager.deleteSessionSingle(session),
                new SingleData<>(
                        isDeleted::setValue,
                        throwable -> getErrorHandler().handleError(isDeleted, throwable)));

        return isDeleted;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_session_null));
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
    }
}
