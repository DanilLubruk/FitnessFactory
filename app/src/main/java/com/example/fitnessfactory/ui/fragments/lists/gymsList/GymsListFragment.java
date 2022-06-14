package com.example.fitnessfactory.ui.fragments.lists.gymsList;

import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_ID;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_NAME;

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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.databinding.FragmentGymsSearchListBinding;
import com.example.fitnessfactory.databinding.FragmentSessionTypesSearchListBinding;
import com.example.fitnessfactory.ui.activities.editors.gym.GymEditorActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerSelectFragment;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.ui.viewmodels.lists.gym.GymsListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GymsListFragment extends ListListenerSelectFragment<Gym, GymsListViewHolder, GymsListAdapter> {

    protected final int MENU_SEARCH = 21;

    private GymsListViewModel viewModel;

    public FragmentGymsSearchListBinding binding;

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentGymsSearchListBinding.inflate(inflater, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_gyms);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_gyms);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new GymsListViewModelFactory()).get(GymsListViewModel.class);
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

            List<SearchFieldState<Gym>> elements = SearchFieldState.getGymsSearchFields();
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
    protected GymsListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymsListDataListenerEvent(GymsListDataListenerEvent gymsListDataListenerEvent) {
        getViewModel().setItems(gymsListDataListenerEvent.getGyms());
    }

    @Override
    protected Intent getResultIntent(Gym gym) {
        Intent result = new Intent();

        int requestCode = getBaseActivity().getIntent().getIntExtra(AppConsts.REQUEST_CODE, REQUEST_GYM_ID);
        switch (requestCode) {
            case REQUEST_GYM_ID:
                result.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
                break;
            case REQUEST_GYM_NAME:
                result.putExtra(AppConsts.GYM_NAME_EXTRA, gym.getName());
                break;
        }

        return result;
    }

    @Override
    protected void showEditorActivity(Gym gym) {
        Intent intent = new Intent(getBaseActivity(), GymEditorActivity.class);
        intent.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
        startActivity(intent);
    }

    @Override
    protected Gym getNewItem() {
        return new Gym();
    }

    @Override
    protected GymsListAdapter createNewAdapter(List<Gym> listData) {
        return new GymsListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }
}
