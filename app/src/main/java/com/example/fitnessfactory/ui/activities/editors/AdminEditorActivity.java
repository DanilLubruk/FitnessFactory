package com.example.fitnessfactory.ui.activities.editors;

import android.content.Intent;
import android.view.Menu;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.databinding.ActivityAdminEditorBinding;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class AdminEditorActivity extends EditorActivity {

    @BindView(R.id.rvGyms)
    RecyclerView rvGyms;
    @BindView(R.id.fabAddGym)
    FloatingActionButton fabAddGym;

    private AdminEditorViewModel viewModel;
    private ActivityAdminEditorBinding binding;
    private GymsListAdapter adapter;
    private RecyclerTouchListener touchListener;

    public static final int REQUEST_GYM = 20;

    @Override
    protected AdminEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_editor);
        viewModel = new ViewModelProvider(this).get(AdminEditorViewModel.class);
        setTitle(R.string.title_edit_item);
        super.initActivity();
        binding.setModel(viewModel);
        viewModel.setAdminData(getIntent());
    }

    @Override
    public void initComponents() {
        fabAddGym.setOnClickListener(view -> showSelectionActivity());
        GuiUtils.initListView(this, rvGyms, true);
        touchListener = new RecyclerTouchListener(this, rvGyms);
        rvGyms.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnRemove);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnRemove:
                    Gym gym = adapter.getGym(position);
                    askForDeleteGym(gym);
                    break;
            }
        });
        viewModel.getGyms().observe(this, this::setData);
    }

    private void askForDeleteGym(Gym gym) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        this,
                        getDeleteGymMessage(),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                deleteGym(gym);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private String getDeleteGymMessage() {
        return ResUtils.getString(R.string.message_ask_remove_admin_from_gym);
    }

    private void deleteGym(Gym gym) {
        viewModel.removeGym(gym.getId());
    }

    private void showSelectionActivity() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_GYMS_ID);

        startActivityForResult(intent, REQUEST_GYM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GYM:
                String gymId = data.getStringExtra(AppConsts.GYM_ID_EXTRA);
                viewModel.addGym(gymId);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminEditorGymsListenerEvent(AdminGymsListListenerEvent adminGymsListListenerEvent) {
        viewModel.getGymsData();
    }

    private void setData(List<Gym> gyms) {
        if (adapter == null) {
            adapter = new GymsListAdapter(gyms, R.layout.admins_gyms_list_item_view);
            rvGyms.setAdapter(adapter);
        } else {
            adapter.setGyms(gyms);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.addAdminEditorGymsListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.removeAdminEditorGymsListener();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_SAVE);

        return isCreated;
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }
}
