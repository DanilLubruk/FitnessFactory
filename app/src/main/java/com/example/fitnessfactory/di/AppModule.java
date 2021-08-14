package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.AdminsDataManager;
import com.example.fitnessfactory.data.managers.GymsAccessManager;
import com.example.fitnessfactory.data.repositories.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.AdminsRepository;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @AppScope
    public UserRepository proviedFirestoreUserRepository() {
        return new UserRepository();
    }

    @Provides
    @Singleton
    public FirebaseAuthManager provideFirebaseAuthManager() {
        return new FirebaseAuthManager();
    }

    @Provides
    @AppScope
    public GymRepository provideGymRepository() {
        return new GymRepository();
    }

    @Provides
    @AppScope
    public AdminsAccessRepository provideStaffAccessRepository() {
        return new AdminsAccessRepository();
    }

    @Provides
    @AppScope
    public AdminsRepository provideAdminsRepository() {
        return new AdminsRepository();
    }

    @Provides
    @AppScope
    public AdminsAccessManager provideAdminsAccessManager() {
        return new AdminsAccessManager();
    }

    @Provides
    @AppScope
    public AdminsDataManager provideAdminsDataManager() {
        return new AdminsDataManager();
    }

    @Provides
    @AppScope
    public AdminsListDataListener provideAdminsListDataListener() {
        return new AdminsListDataListener();
    }

    @Provides
    @AppScope
    public OrganisationInfoRepository provideOrganisationInfoRepository() {
        return new OrganisationInfoRepository();
    }

    @Provides
    @AppScope
    public GymsAccessManager provideGymsAccessManager() {
        return new GymsAccessManager();
    }

    @Provides
    @AppScope
    public GymsListDataListener provideGymsListDataListener() {
        return new GymsListDataListener();
    }
}
