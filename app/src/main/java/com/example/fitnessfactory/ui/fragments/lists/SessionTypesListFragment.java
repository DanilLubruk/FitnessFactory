package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionTypesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.databinding.FragmentCoachesSearchListBinding;
import com.example.fitnessfactory.databinding.FragmentSessionTypesSearchListBinding;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.activities.editors.SessionTypeEditorActivity;
import com.example.fitnessfactory.ui.adapters.SessionTypesListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.SessionTypesListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionTypes.SessionTypesListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SessionTypesListFragment extends
        ListListenerSelectFragment<SessionType, SessionTypesListViewHolder, SessionTypesListAdapter> {

    protected final int MENU_SEARCH = 21;

    private SessionTypesListViewModel viewModel;

    private SessionEditorViewModel editorViewModel;

    public FragmentSessionTypesSearchListBinding binding;

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSessionTypesSearchListBinding.inflate(inflater, container, false);
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

    protected AppCompatEditText getEdtSearch() {
        return binding.edtSearch;
    }

    protected LinearLayoutCompat getLlSearch() {
        return binding.llSearch;
    }

    protected ImageButton getIbSearchOptions() {
        return binding.ibSearchOptions;
    }

    @Override
    protected SessionTypesListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_session_type);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_session_types);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new SessionTypesListViewModelFactory()).get(SessionTypesListViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                SessionEditorViewModelFactoryProvider.getFactory())
                .get(SessionEditorViewModel.class);
        binding.setModel(viewModel);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
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

            List<SearchFieldState<SessionType>> elements = SearchFieldState.getSessionTypesSearchFields();
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
    protected SessionTypesListAdapter createNewAdapter(List<SessionType> listData) {
        return new SessionTypesListAdapter(listData, R.layout.session_types_list_item_view);
    }

    @Override
    protected void sendSelectResult(SessionType sessionType) {
        editorViewModel.setSessionType(sessionType.getId());
        closeFragment();
    }

    @Override
    protected Intent getResultIntent(SessionType item) {
        Intent result = new Intent();
        result.putExtra(AppConsts.SESSION_TYPE_ID_EXTRA, item.getId());

        return result;
    }

    @Override
    protected void showEditorActivity(SessionType item) {
        Intent intent = new Intent(getBaseActivity(), SessionTypeEditorActivity.class);
        intent.putExtra(AppConsts.SESSION_TYPE_ID_EXTRA, item.getId());
        startActivity(intent);
    }

    @Override
    protected SessionType getNewItem() {
        return new SessionType();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session_type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionTypesListDataListenerEvent(SessionTypesListDataListenerEvent sessionTypesListDataListenerEvent) {
        getViewModel().setItems(sessionTypesListDataListenerEvent.getSessionTypes());
    }
}
