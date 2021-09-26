package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.OrganisationInfoViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListTabViewModel;
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
    void inject(AdminsListViewModelFactory adminsListViewModelFactory);
    void inject(MainActivityViewModel mainActivityViewModel);
    void inject(AdminsListTabViewModel adminsListTabViewModel);
    void inject(GymsAccessManager gymsAccessManager);
    void inject(AuthManager authManager);
    void inject(CoachesListViewModelFactory coachesListViewModelFactory);
    void inject(CoachesListTabViewModel coachesListTabViewModel);
    void inject(OrganisationInfoViewModel organisationInfoViewModel);
    void inject(CoachEditorViewModelFactory coachEditorViewModelFactory);
    void inject(AdminEditorViewModelFactory adminEditorViewModelFactory);
}
