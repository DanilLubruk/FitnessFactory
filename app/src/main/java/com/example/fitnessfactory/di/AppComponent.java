package com.example.fitnessfactory.di;

import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.OrganisationInfoViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AuthViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@AppScope
@Singleton
public interface AppComponent {

    void inject(AdminsListViewModelFactory adminsListViewModelFactory);
    void inject(MainActivityViewModel mainActivityViewModel);
    void inject(CoachesListViewModelFactory coachesListViewModelFactory);
    void inject(OrganisationInfoViewModel organisationInfoViewModel);
    void inject(CoachEditorViewModelFactory coachEditorViewModelFactory);
    void inject(AdminEditorViewModelFactory adminEditorViewModelFactory);
    void inject(AdminsListTabViewModelFactory adminsListTabViewModelFactory);
    void inject(CoachesListTabViewModelFactory coachesListTabViewModelFactory);
    void inject(GymsListViewModelFactory gymsListViewModelFactory);
    void inject(GymEditorViewModelFactory gymEditorViewModelFactory);
    void inject(AuthViewModelFactory authViewModelFactory);
    void inject(ClientsListViewModelFactory clientsListViewModelFactory);
    void inject(ClientEditorViewModelFactory clientEditorViewModelFactory);
}
