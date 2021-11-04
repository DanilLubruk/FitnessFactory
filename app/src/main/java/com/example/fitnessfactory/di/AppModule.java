package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.dataListeners.AdminGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.AdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.dataListeners.CoachGymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.DaysSessionsListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymAdminsListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymCoachesListDataListener;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionClientsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionTypesListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCalendarDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCoachesListDataListener;
import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.data.AdminsDataManager;
import com.example.fitnessfactory.data.managers.AuthManager;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.ClientsDataManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.managers.data.SessionTypesDataManager;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.ClientSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.ClientsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionTypeRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.system.FirebaseAuthManager;

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
    public GymsAccessManager provideGymsAccessManager(SessionsRepository sessionsRepository,
                                                      OwnerGymRepository ownerGymRepository,
                                                      OwnerAdminsRepository ownerAdminsRepository,
                                                      OwnerCoachesRepository ownerCoachesRepository) {
        return new GymsAccessManager(sessionsRepository, ownerGymRepository, ownerAdminsRepository, ownerCoachesRepository);
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
    public CoachesAccessManager provideCoachesAccessManager(CoachSessionsRepository coachSessionsRepository,
                                                            CoachesAccessRepository coachesAccessRepository,
                                                            OwnerCoachesRepository ownerCoachesRepository) {
        return new CoachesAccessManager(coachSessionsRepository, coachesAccessRepository, ownerCoachesRepository);
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

    @Provides
    @AppScope
    public SessionsCalendarDataListener provideSessionsCalendarDataListener() {
        return new SessionsCalendarDataListener();
    }

    @Provides
    @AppScope
    public SessionsRepository provideSessionsRepository() {
        return new SessionsRepository();
    }

    @Provides
    @AppScope
    public SessionClientsListDataListener provideSessionClientsListDataListener() {
        return new SessionClientsListDataListener();
    }

    @Provides
    @AppScope
    public DaysSessionsListDataListener provideDaysSessionsListDataListener() {
        return new DaysSessionsListDataListener();
    }

    @Provides
    @AppScope
    public SessionsDataManager provideSessionsDataManager(SessionsRepository sessionsRepository,
                                                          SessionTypeRepository sessionTypeRepository,
                                                          ClientSessionsRepository clientSessionsRepository,
                                                          CoachSessionsRepository coachSessionsRepository,
                                                          OwnerCoachesRepository ownerCoachesRepository) {
        return new SessionsDataManager(
                sessionsRepository,
                sessionTypeRepository,
                clientSessionsRepository,
                coachSessionsRepository,
                ownerCoachesRepository);
    }

    @Provides
    @AppScope
    public SessionsCoachesListDataListener provideSessionsCoachesListDataListener() {
        return new SessionsCoachesListDataListener();
    }

    @Provides
    @AppScope
    public ClientSessionsRepository provideClientSessionsRepository() {
        return new ClientSessionsRepository();
    }

    @Provides
    @AppScope
    public CoachSessionsRepository provideCoachSessionsRepository() {
        return new CoachSessionsRepository();
    }

    @Provides
    @AppScope
    public SessionTypesDataManager provideSessionTypesDataManager(SessionsRepository sessionsRepository,
                                                                  SessionTypeRepository sessionTypeRepository) {
        return new SessionTypesDataManager(sessionsRepository, sessionTypeRepository);
    }

    @Provides
    @AppScope
    public ClientsDataManager provideClientsDataManager(ClientsRepository clientsRepository,
                                                        ClientSessionsRepository clientSessionsRepository) {
        return new ClientsDataManager(clientsRepository, clientSessionsRepository);
    }
}
