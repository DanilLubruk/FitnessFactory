package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.repositories.bondingRepositories.AdminAccessRepository;
import com.example.fitnessfactory.data.repositories.bondingRepositories.GymAccessRepository;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@AppScope
@Singleton
public interface AppComponent {

    void inject(AuthViewModel authViewModel);
    void inject(GymsListViewModel gymsListViewModel);
    void inject(GymEditorViewModel gymEditorViewModel);
    void inject(AdminListViewModel adminListViewModel);
    void inject(MainActivityViewModel mainActivityViewModel);
    void inject(AdminEditorViewModel adminEditorViewModel);
    void inject(GymAccessRepository gymAccessRepository);
    void inject(AdminsListTabViewModel adminsListTabViewModel);
    void inject(AdminAccessRepository adminAccessRepository);
    void inject(AdminsAccessManager adminsAccessManager);
    void inject(AdminsDataManager adminsDataManager);
    void inject(AdminsListDataListener adminsListDataListener);
    void inject (AdminsGymsDataListener AdminsGymsDataListener);
}
