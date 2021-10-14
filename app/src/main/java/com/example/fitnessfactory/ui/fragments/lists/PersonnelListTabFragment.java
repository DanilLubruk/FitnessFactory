package com.example.fitnessfactory.ui.fragments.lists;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.activities.editors.EditorActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

public abstract class PersonnelListTabFragment
        extends ListListenerFragment<AppUser, PersonnelListViewHolder, PersonnelListAdapter>  {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected abstract PersonnelListTabViewModel getViewModel();

    protected abstract String getSingularPersonnelCaption();

    protected abstract int getSelectionFragmentId();

    protected abstract String getPersonnelEmailExtraKey();

    protected void initComponents() {
        super.initComponents();
        getViewModel().refreshGymData(getBaseActivity().getIntent().getStringExtra(AppConsts.GYM_ID_EXTRA));
        fabAddItem.setOnClickListener(view -> tryToShowSelectionActivity());
        touchListener.setSwipeOptionViews(R.id.btnRemove);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnRemove:
                    askForDelete(adapter.getItem(position));
                    break;
            }
        });
        getViewModel().getPersonnel().observe(getViewLifecycleOwner(), this::setListData);
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
                onListRowClicked();
            }
        });
    }

    protected void onListRowClicked() {
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

    @Override
    protected PersonnelListAdapter createNewAdapter(List<AppUser> listData) {
        return new PersonnelListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    @Override
    protected AppUser getNewItem() {
        return new AppUser();
    }

    @Override
    protected void onListRowClicked(AppUser appUser) {

    }

    @Override
    protected void showEditorActivity(AppUser item) {

    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected String getTitle() {
        return getBaseActivity().getTitle().toString();
    }

    @Override
    protected void bindView(View itemView) {
        super.bindView(itemView);
    }
}
