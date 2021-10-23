package com.example.fitnessfactory.ui.viewmodels.editors;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Date;

import javax.inject.Inject;

public class SessionEditorViewModel extends EditorViewModel {

    private SessionsRepository sessionsRepository;

    public ObservableField<Session> session = new ObservableField<>();
    private Session dbSession;

    @Inject
    public SessionEditorViewModel(SessionsRepository sessionsRepository) {
        this.sessionsRepository = sessionsRepository;
    }

    public SingleLiveEvent<Date> getSessionDate() {
        SingleLiveEvent<Date> dateObserver = new SingleLiveEvent<>();

        Session session = this.session.get();
        if (session != null) {
            dateObserver.setValue(session.getDate());
        } else {
            dateObserver.setValue(null);
            return handleDateNullError(dateObserver);
        }

        return dateObserver;
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        return null;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        return null;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        return null;
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
