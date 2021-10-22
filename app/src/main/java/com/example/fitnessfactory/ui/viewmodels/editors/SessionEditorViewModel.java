package com.example.fitnessfactory.ui.viewmodels.editors;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class SessionEditorViewModel extends EditorViewModel {

    private SessionsRepository sessionsRepository;

    public ObservableField<Session> session = new ObservableField<>();

    @Inject
    public SessionEditorViewModel(SessionsRepository sessionsRepository) {
        this.sessionsRepository = sessionsRepository;
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

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_session_null));
    }
}
