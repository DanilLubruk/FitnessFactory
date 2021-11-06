package com.example.fitnessfactory.ui.viewmodels.lists.personnelList;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class CoachesListViewModel extends PersonnelListViewModel {

    @Inject
    public CoachesListViewModel(CoachesAccessManager accessManager,
                                CoachesDataManager dataManager,
                                CoachesListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
    }


    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
