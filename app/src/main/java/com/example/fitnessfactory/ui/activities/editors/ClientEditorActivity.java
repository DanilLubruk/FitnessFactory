package com.example.fitnessfactory.ui.activities.editors;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.adapters.AdminGymsPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.ClientEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;

public class ClientEditorActivity extends PersonnelEditorActivity {

    private ClientEditorViewModel viewModel;

    @Override
    protected ClientEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected FragmentStateAdapter getPageAdapter() {
        return new AdminGymsPageAdapter(getSupportFragmentManager(), getLifecycle());
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }

    @Override
    public void initActivity() {
        viewModel = new ViewModelProvider(this, new ClientEditorViewModelFactory()).get(ClientEditorViewModel.class);
        super.initActivity();
        binding.container.tlPersonnelData.setVisibility(View.GONE);
        binding.container.vpPersonnelData.setVisibility(View.GONE);
    }
}
