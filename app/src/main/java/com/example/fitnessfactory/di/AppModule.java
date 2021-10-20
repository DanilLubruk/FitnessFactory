package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymCoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionTypesListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
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
    @AppScope
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
    public AdminsAccessManager provideAdminsAccessManager(AdminsAccessRepository accessRepository,
                                                          OwnerAdminsRepository ownerAdminsRepository) {
        return new AdminsAccessManager(accessRepository, ownerAdminsRepository);
    }

    @Provides
    @AppScope
    public AdminsDataManager provideAdminsDataManager(OwnerAdminsRepository ownerRepository,
                                                      UserRepository userRepository,
                                                      OwnerGymRepository gymRepository) {
        return new AdminsDataManager(ownerRepository, userRepository, gymRepository);
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
    public GymsAccessManager provideGymsAccessManager(OwnerGymRepository ownerGymRepository,
                                                      OwnerAdminsRepository ownerAdminsRepository,
                                                      OwnerCoachesRepository ownerCoachesRepository) {
        return new GymsAccessManager(ownerGymRepository, ownerAdminsRepository, ownerCoachesRepository);
    }

    @Provides
    @AppScope
    public GymsListDataListener provideGymsListDataListener() {
        return new GymsListDataListener();
    }

    @Provides
    @AppScope
    public AuthManager provideAuthManager(UserRepository userRepository,
                                          AdminsAccessRepository adminsAccessRepository,
                                          FirebaseAuthManager authManager,
                                          OrganisationInfoRepository organisationInfoRepository) {
        return new AuthManager(userRepository, adminsAccessRepository, authManager, organisationInfoRepository);
    }

    @Provides
    @AppScope
    public AdminGymsListDataListener provideAdminsGymsDataListener() {
        return new AdminGymsListDataListener();
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
    public CoachesAccessManager provideCoachesAccessManager(CoachesAccessRepository coachesAccessRepository,
                                                            OwnerCoachesRepository ownerCoachesRepository) {
        return new CoachesAccessManager(coachesAccessRepository, ownerCoachesRepository);
    }

    @Provides
    @AppScope
    public CoachesDataManager provideCoachesDataManager(OwnerCoachesRepository ownerRepository,
                                                        UserRepository userRepository,
                                                        OwnerGymRepository gymRepository) {
        return new CoachesDataManager(ownerRepository, userRepository, gymRepository);
    }

    @Provides
    @AppScope
    public CoachGymsListDataListener provideCoachGymsListDataListener() {
        return new CoachGymsListDataListener();
    }

    @Provides
    @AppScope
    public GymCoachesListDataListener provideGymCoachesListDataListener() {
        return new GymCoachesListDataListener();
    }

    @Provides
    @AppScope
    public ClientsListDataListener provideClientsListDataListener() {
        return new ClientsListDataListener();
    }

    @Provides
    @AppScope
    public ClientsRepository provideClientsRepository() {
        return new ClientsRepository();
    }

    @Provides
    @AppScope
    public SessionTypesListDataListener provideSessionTypesListDataListener() {
        return new SessionTypesListDataListener();
    }

    @Provides
    @AppScope
    public SessionTypeRepository provideSessionTypeRepository() {
        return new SessionTypeRepository();
    }
}
