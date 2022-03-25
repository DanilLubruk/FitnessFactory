package com.example.fitnessfactory.di;

import com.example.fitnessfactory.ui.activities.editors.SessionEditorActivity;
import com.example.fitnessfactory.ui.fragments.lists.GymsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.SessionTypesListFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.ClientsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.CoachesListFragment;
import com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList.SessionClientsListTabFragment;
import com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList.SessionCoachesListTabFragment;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.OrganisationInfoViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.AuthViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachSessionsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionCoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypeEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionsListViewModelFactory;

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
    void inject(SessionTypesListViewModelFactory sessionTypesListViewModelFactory);
    void inject(SessionTypeEditorViewModelFactory sessionTypeEditorViewModelFactory);
    void inject(SessionsListViewModelFactory sessionsListViewModelFactory);
    void inject(SessionEditorViewModelFactory sessionEditorViewModelFactory);
    void inject(ClientsListTabViewModelFactory clientsListTabViewModelFactory);
    void inject(SessionCoachesListTabViewModelFactory sessionCoachesListTabViewModelFactory);
    void inject(CoachGymsListTabViewModelFactory coachGymsListTabViewModelFactory);
    void inject(AdminGymsListTabViewModelFactory adminGymsListTabViewModelFactory);
    void inject(CoachSessionsListTabViewModelFactory coachSessionsListTabViewModelFactory);
    void inject(SessionEditorActivity sessionEditorActivity);
    void inject(SessionClientsListTabFragment sessionClientsListTabFragment);
    void inject(SessionCoachesListTabFragment sessionCoachesListTabFragment);
    void inject(ClientsListFragment clientsListFragment);
    void inject(CoachesListFragment coachesListFragment);
    void inject(GymsListFragment gymsListFragment);
    void inject(SessionTypesListFragment sessionTypesListFragment);
}
