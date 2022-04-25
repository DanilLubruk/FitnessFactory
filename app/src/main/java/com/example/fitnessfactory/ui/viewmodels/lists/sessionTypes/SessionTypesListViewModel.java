package com.example.fitnessfactory.ui.viewmodels.lists.sessionTypes;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.SessionTypesListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionTypesDataManager;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.common.internal.ResourceUtils;

import javax.inject.Inject;

public class SessionTypesListViewModel extends SearchViewModel<SessionType, SearchFieldState<SessionType>> {

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
            handleItemDeletingNullError();
            return;
        }
        subscribeInIOThread(sessionTypesDataManager.deleteSessionTypeCompletable(item));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_session_type_null));
    }

    @Override
    protected SearchFieldState<SessionType> getDefaultSearchField() {
        return new SessionTypeNameSearchField();
    }

    @Override
    protected SearchFieldState<SessionType> getRestoredSearchField(int index) {
        return SearchFieldState.getSessionTypeSearchField(index);
    }
}
