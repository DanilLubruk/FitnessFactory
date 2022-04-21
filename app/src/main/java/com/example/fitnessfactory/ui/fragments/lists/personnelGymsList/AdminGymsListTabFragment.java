package com.example.fitnessfactory.ui.fragments.lists.personnelGymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.admin.AdminEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.AdminGymsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.PersonnelGymsListTabViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class AdminGymsListTabFragment extends PersonnelGymsListTabFragment {

    private AdminGymsListTabViewModel viewModel;

    private AdminEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new AdminGymsListTabViewModelFactory()).get(AdminGymsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                AdminEditorViewModelFactoryProvider.getFactory())
                .get(AdminEditorViewModel.class);
    }

    @Override
    protected PersonnelGymsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminEditorGymsListenerEvent(AdminGymsListListenerEvent adminGymsListListenerEvent) {
        editorViewModel.personnelEmail.observe(this, email -> getViewModel().getGymsData(email));
    }

    @Override
    protected void openSelectionActivity() {
        getBaseActivity().getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragment(getBaseActivity(), AppConsts.FRAGMENT_ADMIN_GYMS_ID);
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
