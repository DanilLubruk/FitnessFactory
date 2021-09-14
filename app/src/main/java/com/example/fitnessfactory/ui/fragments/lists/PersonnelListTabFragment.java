package com.example.fitnessfactory.ui.fragments.lists;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.activities.editors.EditorActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListTabViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

public abstract class PersonnelListTabFragment extends ListListenerFragment<AppUser>  {

    RecyclerView rvPersonnel;
    FloatingActionButton fabAddPersonnel;

    private PersonnelListAdapter adapter;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        defineViewModel();
        initComponents();
    }

    protected abstract void defineViewModel();

    @Override
    protected abstract PersonnelListTabViewModel getViewModel();

    protected abstract String getSingularPersonnelCaption();

    protected abstract int getSelectionFragmentId();

    protected abstract String getPersonnelEmailExtraKey();

    private void initComponents() {
        getViewModel().refreshGymData(getBaseActivity().getIntent().getStringExtra(AppConsts.GYM_ID_EXTRA));
        fabAddPersonnel.setOnClickListener(view -> tryToShowSelectionActivity());
        GuiUtils.initListView(getBaseActivity(), rvPersonnel, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvPersonnel);
        rvPersonnel.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnRemove);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnRemove:
                    AppUser personnel = adapter.getPersonnel(position);
                    askForDelete(personnel);
                    break;
            }
        });
        getViewModel().getPersonnel().observe(getViewLifecycleOwner(), this::setPersonnelData);
    }

    @Override
    public EditorActivity getBaseActivity() {
        return (EditorActivity) getActivity();
    }

    @Override
    protected String getDeleteMessage() {
        return String.format(
                ResUtils.getString(R.string.message_ask_remove_personnel_from_gym),
                getSingularPersonnelCaption());
    }

    private void tryToShowSelectionActivity() {
        getBaseActivity().save(isSaved -> {
            if (isSaved) {
                getViewModel().refreshGymData(getBaseActivity().getIntent().getStringExtra(AppConsts.GYM_ID_EXTRA));
                showSelectionActivity();
            }
        });
    }

    private void showSelectionActivity() {
        Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, getSelectionFragmentId());

        startActivityForResult(intent, REQUEST_GYM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GYM:
                if (resultCode == RESULT_OK) {
                    String personnelEmail = data.getStringExtra(getPersonnelEmailExtraKey());
                    getViewModel().addPersonnelToGym(personnelEmail);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPersonnelData(List<AppUser> personnel) {
        if (adapter == null) {
            adapter = new PersonnelListAdapter(personnel, R.layout.one_bg_button_list_item_view);
            rvPersonnel.setAdapter(adapter);
        } else {
            adapter.setPersonnel(personnel);
        }
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected void bindView(View itemView) {
        rvPersonnel = itemView.findViewById(R.id.rvData);
        fabAddPersonnel = itemView.findViewById(R.id.fabAddItem);
    }
}
