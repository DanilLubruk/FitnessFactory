package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.ClientsListViewModel;

public class ClientsListFragment extends ListListenerFragment<Client> {

    private ClientsListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ClientsListViewModel.class);
        initComponents();
        getBaseActivity().setTitle(R.string.title_clients);
    }

    private void initComponents() {

    }

    @Override
    public void closeProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    protected void bindView(View itemView) {

    }

    @Override
    protected DataListListener<Client> getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return null;
    }
}
