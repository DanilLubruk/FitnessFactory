package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;
import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.List;

import io.reactivex.Completable;

public abstract class SessionParticipantListTabViewModel<ItemType> extends ListViewModel<ItemType> {

    protected final SessionsDataManager sessionsDataManager;
    private String sessionId;

    protected abstract ArgDataListener<String> getDataListener();

    protected abstract Completable getAddParticipantAction(String sessionId, String participantId);

    protected abstract Completable getDeleteParticipantAction(String sessionId, String participantId);

    protected abstract List<String> getParticipantsList(Session session);

    protected abstract String getParticipantId(ItemType participant);

    protected abstract String getParticipantNullMessage();

    public SessionParticipantListTabViewModel(SessionsDataManager sessionsDataManager) {
        this.sessionsDataManager = sessionsDataManager;
    }

    public void resetSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void startDataListener() {
        if (StringUtils.isEmpty(sessionId)) {
            doInterruptProgress.setValue(true);
            return;
        }
        Log.d(AppConsts.DEBUG_TAG, "sessionId: " + sessionId);
        getDataListener().startDataListener(sessionId);
    }

    @Override
    public void stopDataListener() {
        getDataListener().stopDataListener();
    }

    public void addParticipantToSession(String participantId) {
        if (StringUtils.isEmpty(sessionId)) {
            GuiUtils.showMessage(getSessionNullMessage());
            return;
        }
        if (StringUtils.isEmpty(participantId)) {
            GuiUtils.showMessage(getParticipantNullMessage());
            return;
        }

        subscribeInIOThread(getAddParticipantAction(sessionId, participantId));
    }

    @Override
    public void deleteItem(ItemType item) {
        if (StringUtils.isEmpty(sessionId)) {
            GuiUtils.showMessage(getSessionNullMessage());
            return;
        }
        String participantId = getParticipantId(item);
        if (StringUtils.isEmpty(participantId)) {
            GuiUtils.showMessage(getParticipantNullMessage());
            return;
        }

        subscribeInIOThread(getDeleteParticipantAction(sessionId, participantId));
    }

    private String getSessionNullMessage() {
        return ResUtils.getString(R.string.message_error_session_null);
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        getHandle().put(AppConsts.SESSION_ID_EXTRA, sessionId);
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
        sessionId = (String) getHandle().get(AppConsts.SESSION_ID_EXTRA);
    }
}
