package com.example.fitnessfactory.ui.fragments.lists.personnelGymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachGymsListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.coach.CoachEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.CoachGymsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.PersonnelGymsListTabViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class CoachGymsListTabFragment extends PersonnelGymsListTabFragment {

    private CoachGymsListTabViewModel viewModel;

    private CoachEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new CoachGymsListTabViewModelFactory()).get(CoachGymsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                CoachEditorViewModelFactoryProvider.getFactory())
                .get(CoachEditorViewModel.class);
    }

    @Override
    protected PersonnelGymsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachEditorGymsListenerEvent(CoachGymsListListenerEvent coachGymsListListenerEvent) {
        editorViewModel.personnelEmail.observe(this, email -> getViewModel().getGymsData(email));
    }

    @Override
    protected void openSelectionActivity() {
        getBaseActivity().getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragment(getBaseActivity(), AppConsts.FRAGMENT_COACH_GYMS_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getViewModel() != null) {
            editorViewModel.personnelEmail.observe(this, email -> getViewModel().startDataListener(email));
        } else {
            closeProgress();
        }
    }

    protected void deleteItem(Gym gym) {
        editorViewModel.personnelEmail.observe(this, email -> getViewModel().deleteItem(email, gym));
    }
}
