package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.repositories.GymAccessRepository;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
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
}
