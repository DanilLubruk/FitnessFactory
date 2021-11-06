package com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class CoachGymsListTabViewModel extends PersonnelGymsListTabViewModel {

    @Inject
    public CoachGymsListTabViewModel(OwnerCoachesRepository ownerRepository,
                                     CoachGymsListDataListener dataListener,
                                     CoachesDataManager coachesDataManager) {
        super(ownerRepository, dataListener, coachesDataManager);
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
