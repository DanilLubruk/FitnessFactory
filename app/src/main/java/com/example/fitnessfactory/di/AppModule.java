package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.managers.data.GymsDataManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
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
    public OwnerGymRepository provideGymRepository() {
        return new OwnerGymRepository();
    }

    @Provides
    @AppScope
    public AdminsAccessRepository provideStaffAccessRepository() {
        return new AdminsAccessRepository();
    }

    @Provides
    @AppScope
    public OwnerAdminsRepository provideAdminsRepository() {
        return new OwnerAdminsRepository();
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

    @Provides
    @AppScope
    public AuthManager provideAuthManager() {
        return new AuthManager();
    }

    @Provides
    @AppScope
    public AdminGymsListDataListener provideAdminsGymsDataListener() {
        return new AdminGymsListDataListener();
    }

    @Provides
    @AppScope
    public GymsDataManager provideGymsDataManager() {
        return new GymsDataManager();
    }

    @Provides
    @AppScope
    public GymAdminsListDataListener provideGymAdminsDataListener() {
        return new GymAdminsListDataListener();
    }

    @Provides
    @AppScope
    public CoachesListDataListener provideCoachesListDataListener() {
        return new CoachesListDataListener();
    }

    @Provides
    @AppScope
    public OwnerCoachesRepository provideOwnerCoachesRepository() {
        return new OwnerCoachesRepository();
    }

    @Provides
    @AppScope
    public CoachesAccessRepository provideCoachesAccessRepository() {
        return new CoachesAccessRepository();
    }

    @Provides
    @AppScope
    public CoachesAccessManager provideCoachesAccessManager() {
        return new CoachesAccessManager();
    }

    @Provides
    @AppScope
    public CoachesDataManager provideCoachesDataManager() {
        return new CoachesDataManager();
    }
}
