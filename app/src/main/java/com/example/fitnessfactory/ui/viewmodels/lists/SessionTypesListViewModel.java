package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.SessionTypesListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionTypesDataManager;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.common.internal.ResourceUtils;

import javax.inject.Inject;

public class SessionTypesListViewModel extends BaseViewModel implements DataListListener<SessionType> {

    private final SessionTypesDataManager sessionTypesDataManager;
    private final SessionTypesListDataListener dataListener;

    @Inject
    public SessionTypesListViewModel(SessionTypesDataManager sessionTypesDataManager,
                                     SessionTypesListDataListener dataListener) {
        this.sessionTypesDataManager = sessionTypesDataManager;
        this.dataListener = dataListener;
    }

    @Override
    public void startDataListener() {
        dataListener.startDataListener();
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    @Override
    public void deleteItem(SessionType item) {
        if (item == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_session_type_null));
            return;
        }
        subscribeInIOThread(sessionTypesDataManager.deleteSessionTypeCompletable(item));
    }
}
