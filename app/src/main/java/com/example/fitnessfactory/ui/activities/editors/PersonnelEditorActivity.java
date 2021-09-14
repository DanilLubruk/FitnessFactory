package com.example.fitnessfactory.ui.activities.editors;

import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM;

import android.content.Intent;
import android.view.Menu;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.databinding.ActivityPersonnelEditorBinding;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class PersonnelEditorActivity extends EditorActivity {

    RecyclerView rvGyms;
    FloatingActionButton fabAddGym;

    private ActivityPersonnelEditorBinding binding;
    private GymsListAdapter adapter;
    private RecyclerTouchListener touchListener;

    @Override
    protected abstract PersonnelEditorViewModel getViewModel();

    protected abstract String getDeleteGymMessage();

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personnel_editor);
        setTitle(R.string.title_edit_item);
        super.initActivity();
        binding.setModel(getViewModel());
        getViewModel().setPersonnelData(getIntent());
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
        getViewModel().getGyms().observe(this, this::setData);
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

    private void deleteGym(Gym gym) {
        getViewModel().deleteItem(gym);
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
                if (resultCode == RESULT_OK) {
                    String gymId = data.getStringExtra(AppConsts.GYM_ID_EXTRA);
                    getViewModel().addGym(gymId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setData(List<Gym> gyms) {
        if (adapter == null) {
            adapter = new GymsListAdapter(gyms, R.layout.one_bg_button_list_item_view);
            rvGyms.setAdapter(adapter);
        } else {
            adapter.setGyms(gyms);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().startDataListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModel().stopDataListener();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_SAVE);

        return isCreated;
    }

    @Override
    protected void bindViews() {
        rvGyms = findViewById(R.id.rvData);
        fabAddGym = findViewById(R.id.fabAddItem);
    }
}
