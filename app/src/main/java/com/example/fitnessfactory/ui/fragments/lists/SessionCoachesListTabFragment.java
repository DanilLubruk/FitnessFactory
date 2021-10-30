package com.example.fitnessfactory.ui.fragments.lists;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_COACH;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
\import com.example.fitnessfactory.ui.viewmodels.factories.SessionCoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SessionCoachesListTabFragment extends ListListenerTabFragment<Personnel, PersonnelListViewHolder, PersonnelListAdapter> {

    private SessionCoachesListTabViewModel viewModel;

    @Override
    public void closeProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    protected SessionCoachesListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new SessionCoachesListTabViewModelFactory()).get(SessionCoachesListTabViewModel.class);
    }

    @Override
    protected PersonnelListAdapter createNewAdapter(List<Personnel> listData) {
        return new PersonnelListAdapter(listData, R.layout.one_bg_button_list_item_view);
    }

    @Override
    protected void onListRowClicked(Personnel personnel) {

    }

    @Override
    protected void showEditorActivity(Personnel item) {

    }

    @Override
    protected Personnel getNewItem() {
        return new Personnel();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_remove_coach_from_session);
    }

    @Override
    protected void openSelectionActivity() {
        Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_COACHES_ID);

        startActivityForResult(intent, REQUEST_COACH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_COACH:
                if (resultCode == RESULT_OK) {
                    String coachId = data.getStringExtra(AppConsts.COACH_ID_EXTRA);
                    getViewModel().addCoachToSession(coachId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionIdUpdateEvent(SessionIdUpdateEvent sessionIdUpdateEvent) {
        getViewModel().setSessionId(sessionIdUpdateEvent.getSessionId());
        getViewModel().startDataListener();
    }
}
