package com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList;

import android.os.Bundle;
import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.List;

import io.reactivex.Completable;

public abstract class SessionParticipantListTabViewModel<ItemType> extends ListViewModel<ItemType> {

    protected final SessionsDataManager sessionsDataManager;

    protected abstract ArgDataListener<String> getDataListener();

    protected abstract Completable getAddParticipantAction(String sessionId, String participantId);

    protected abstract Completable getDeleteParticipantAction(String sessionId, String participantId);

    protected abstract List<String> getParticipantsList(Session session);

    protected abstract String getParticipantId(ItemType participant);

    public SessionParticipantListTabViewModel(SessionsDataManager sessionsDataManager) {
        this.sessionsDataManager = sessionsDataManager;
    }

    @Override
    public void startDataListener(String sessionId) {
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

    public SingleLiveEvent<Boolean> addParticipantToSession(String sessionId, String participantId) {
        SingleLiveEvent<Boolean> event = new SingleLiveEvent<>();

        if (StringUtils.isEmpty(sessionId)) {
            handleSessionOperationNullError();
            event.setValue(false);
            return event;
        }
        if (StringUtils.isEmpty(participantId)) {
            handleItemOperationError();
            event.setValue(false);
            return event;
        }

        subscribeInIOThread(getAddParticipantAction(sessionId, participantId), () -> event.setValue(true));

        return event;
    }

    private void handleSessionOperationNullError() {
        GuiUtils.showMessage(getErrorOperationMessage().concat(" - ").concat(getSessionNullMessage()));
    }

    @Override
    public void deleteItem(String sessionId, ItemType item) {
        if (StringUtils.isEmpty(sessionId)) {
            handleSessionDeletingNullError();
            return;
        }
        String participantId = getParticipantId(item);
        if (StringUtils.isEmpty(participantId)) {
            handleItemDeletingNullError();
            return;
        }

        subscribeInIOThread(getDeleteParticipantAction(sessionId, participantId));
    }

    private void handleSessionDeletingNullError() {
        GuiUtils.showMessage(getErrorDeletingMessage().concat(" - ").concat(getSessionNullMessage()));
    }

    private String getSessionNullMessage() {
        return ResUtils.getString(R.string.message_error_session_null);
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
    }
}
