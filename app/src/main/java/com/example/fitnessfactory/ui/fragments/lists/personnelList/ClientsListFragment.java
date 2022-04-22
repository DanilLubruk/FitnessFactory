package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.ClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.databinding.FragmentClientSearchListBinding;
import com.example.fitnessfactory.databinding.FragmentListBinding;
import com.example.fitnessfactory.ui.activities.editors.ClientEditorActivity;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client.ClientSearchFieldState;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client.ClientsListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionClientsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ClientsListFragment extends PersonnelListFragment {

    protected final int MENU_SEARCH = 21;

    private ClientsListViewModel viewModel;

    private SessionClientsListTabViewModel tabViewModel;

    private SessionEditorViewModel editorViewModel;

    public FragmentClientSearchListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new ClientsListViewModelFactory()).get(ClientsListViewModel.class);
        tabViewModel = new ViewModelProvider(this, new ClientsListTabViewModelFactory()).get(SessionClientsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                SessionEditorViewModelFactoryProvider.getFactory())
                .get(SessionEditorViewModel.class);
        binding.setModel(viewModel);

        binding.ibSearchOptions.setOnClickListener((view) -> {
            PopupMenu menu = new PopupMenu(getContext(), view);

            List<ClientSearchFieldState> elements = ClientSearchFieldState.getSearchFields();
            for (int i = 0; i < elements.size(); i++) {
                menu.getMenu().add(0, i, i, elements.get(i).toString());
            }

            menu.setOnMenuItemClickListener((item) -> {
                viewModel.changeSearchField(elements.get(item.getItemId()));
                return true;
            });

            menu.show();
        });
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        viewModel.showSearch.observe(this, doSearch -> {
            if (doSearch) {
                binding.llSearch.setVisibility(View.VISIBLE);
            } else {
                binding.llSearch.setVisibility(View.GONE);
            }
        });
        viewModel.searchText.observe(this, viewModel::applySearch);
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentClientSearchListBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return binding.getRoot();
    }

    protected RecyclerView getRecyclerView() {
        return binding.rvData;
    }

    protected FloatingActionMenu getFAB() {
        return binding.fabAddItem;
    }

    protected ProgressBar getProgressBar() {
        return binding.pkProgress;
    }

    protected TextView getEmptyDataLabel() {
        return binding.tvEmptyData;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem = menu.add(0, MENU_SEARCH, 0, R.string.caption_search);
        menuItem.setIcon(ResUtils.getDrawable(R.drawable.ic_baseline_search_24));
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case MENU_SEARCH:
                viewModel.switchSearch();
                binding.edtSearch.setText("");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_client);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_clients);
    }

    @Override
    protected ClientsListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected BooleanPreference getAskToSendInvitationPrefs() {
        return AppPrefs.askToSendClientEmailInvite();
    }

    @Override
    protected Intent getEditorActivityIntent(AppUser personnel) {
        Intent intent = new Intent(getBaseActivity(), ClientEditorActivity.class);

        intent.putExtra(AppConsts.CLIENT_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.CLIENT_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.CLIENT_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected void sendSelectResult(AppUser personnel) {
        editorViewModel.sessionId.observe(this, sessionId -> {
            tabViewModel.addParticipantToSession(sessionId, personnel.getEmail()).observe(this,
                    isSaved -> closeFragment());
        });
    }

    @Override
    protected Intent getResultIntent(AppUser personnel) {
        Intent intent = new Intent();
        intent.putExtra(AppConsts.CLIENT_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_client);
    }

    @Override
    protected String getPluralPersonnelCaption() {
        return ResUtils.getString(R.string.caption_clients);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientsListDataListenerEvent(ClientsListDataListenerEvent clientsListDataListenerEvent) {
        viewModel.getPersonnelListData();
    }
}
