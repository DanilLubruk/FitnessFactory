package com.example.fitnessfactory.di;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.ui.viewmodels.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListViewModel;
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
    void inject(AdminsListTabViewModel adminsListTabViewModel);
    void inject(AdminsAccessManager adminsAccessManager);
    void inject(AdminsDataManager adminsDataManager);
    void inject(GymsAccessManager gymsAccessManager);
    void inject(AuthManager authManager);
    void inject(CoachesDataManager coachesDataManager);
    void inject(CoachesListViewModel coachesListViewModel);
    void inject(CoachesAccessManager coachesAccessManager);
    void inject(CoachEditorViewModel coachEditorViewModel);
    void inject(CoachesListTabViewModel coachesListTabViewModel);
}
