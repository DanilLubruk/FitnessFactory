package com.example.fitnessfactory.ui.viewmodels.editors;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.TimeUtils;

import java.util.Date;

import javax.inject.Inject;

public class SessionEditorViewModel extends EditorViewModel {

    private final SessionsRepository sessionsRepository;

    public ObservableField<Session> session = new ObservableField<>();
    private Session dbSession;

    @Inject
    public SessionEditorViewModel(SessionsRepository sessionsRepository) {
        this.sessionsRepository = sessionsRepository;
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
        isObtained.setValue(true);
    }

    public void setSessionDefaultTime(Date defaultDate) {
        setSessionDate(defaultDate);
        setSessionStartTime(defaultDate);
        setSessionDefaultTime(defaultDate);
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

        session.setDate(TimeUtils.getStartOfDayDate(date));
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
                            isSaved.setValue(isSavedResult);
                        },
                        throwable -> getErrorHandler().handleError(isSaved, throwable)));


        return isSaved;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        return null;
    }

    private void handleDateNullError() {
        handleDateNullError(new SingleLiveEvent<>());
    }

    private SingleLiveEvent<Date> handleDateNullError(SingleLiveEvent<Date> observer) {
        observer.setValue(null);
        GuiUtils.showMessage(getDateNullMessage());

        return observer;
    }

    private String getDateNullMessage() {
        return getErrorOperationMessage()
                .concat(getErrorMessageBreak())
                .concat(ResUtils.getString(R.string.message_error_date_null));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_session_null));
    }
}
