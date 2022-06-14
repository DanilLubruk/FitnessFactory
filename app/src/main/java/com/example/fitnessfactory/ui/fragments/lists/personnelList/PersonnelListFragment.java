package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerSelectFragment;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.PersonnelListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.utils.IntentUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import java.util.List;
import io.reactivex.Single;

public abstract class PersonnelListFragment
        extends ListListenerSelectFragment<AppUser, PersonnelListViewHolder, PersonnelListAdapter> {

    protected final int MENU_SEARCH = 21;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected abstract PersonnelListViewModel getViewModel();

    protected abstract BooleanPreference getAskToSendInvitationPrefs();

    protected abstract Intent getEditorActivityIntent(AppUser personnel);

    protected abstract Intent getResultIntent(AppUser personnel);

    protected abstract String getSingularPersonnelCaption();

    protected abstract String getPluralPersonnelCaption();

    @Override
    protected void initComponents() {
        super.initComponents();
        getFAB().setOnMenuButtonClickListener(view -> showSendEmailInvitationDialog());
        getViewModel().getItems().observe(getViewLifecycleOwner(), this::setListData);

        getViewModel().showSearch.observe(this, doSearch -> {
            if (doSearch) {
                getLlSearch().setVisibility(View.VISIBLE);
            } else {
                getLlSearch().setVisibility(View.GONE);
                getBaseActivity().tryToHideKeyboard();
            }
        });
        getViewModel().searchText.observe(this, getViewModel()::applySearch);

        getIbSearchOptions().setOnClickListener((view) -> {
            PopupMenu menu = new PopupMenu(getContext(), view);

            List<SearchFieldState<AppUser>> elements = SearchFieldState.getPersonnelSearchFields();
            for (int i = 0; i < elements.size(); i++) {
                menu.getMenu().add(0, i, i, elements.get(i).toString());
            }

            menu.setOnMenuItemClickListener((item) -> {
                getViewModel().changeSearchField(elements.get(item.getItemId()));
                return true;
            });

            menu.show();
        });
    }

    protected abstract AppCompatEditText getEdtSearch();

    protected abstract LinearLayoutCompat getLlSearch();

    protected abstract ImageButton getIbSearchOptions();

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem = menu.add(0, MENU_SEARCH, 0, R.string.caption_search);
        menuItem.setIcon(ResUtils.getDrawable(getContext(), R.drawable.ic_baseline_search_24));
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case MENU_SEARCH:
                getViewModel().switchSearch();
                getEdtSearch().setText("");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void showEditorActivity(AppUser personnel) {
        Intent intent = getEditorActivityIntent(personnel);

        startActivity(intent);
    }

    private void showSendEmailInvitationDialog() {
        getViewModel().registerPersonnel(
                DialogUtils.getAskEmailDialog(getBaseActivity()),
                DialogUtils.getAskUserNameDialog(getBaseActivity()),
                getAskToSendInvitationDialog(),
                getAskToSendInvitationPrefs().getValue())
                .observe(this, this::sendEmailInvitation);
    }

    private Single<Boolean> getAskToSendInvitationDialog() {
        return DialogUtils.showAskNoMoreDialog(
                getBaseActivity(),
                String.format(ResUtils.getString(R.string.message_send_invitation), getSingularPersonnelCaption()),
                getAskToSendInvitationPrefs());
    }

    private void sendEmailInvitation(String email) {
        Intent emailIntent = IntentUtils.getEmailIntent(
                email,
                ResUtils.getString(R.string.caption_job_offer),
                String.format(ResUtils.getString(R.string.text_invitation_to_personnel), getPluralPersonnelCaption()));

        startActivity(Intent.createChooser(emailIntent, ResUtils.getString(R.string.title_invite_personnel)));
    }

    @Override
    protected PersonnelListAdapter createNewAdapter(List<AppUser> listData) {
        return new PersonnelListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    @Override
    protected AppUser getNewItem() {
        return new AppUser();
    }
}
