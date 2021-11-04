package com.example.fitnessfactory;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.di.AppScope;
import com.example.fitnessfactory.mockHelpers.mockers.access.AdminsAccessManagerMocker;
import com.example.fitnessfactory.mockHelpers.mockers.access.CoachesAccessManagerMocker;

import org.checkerframework.checker.nullness.compatqual.PolyNullDecl;
import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule {

    private final AdminsAccessRepository adminsAccessRepository = Mockito.mock(AdminsAccessRepository.class);
    private final OwnerAdminsRepository ownerAdminsRepository = Mockito.mock(OwnerAdminsRepository.class);

    private final CoachSessionsRepository coachSessionsRepository = Mockito.mock(CoachSessionsRepository.class);
    private final CoachesAccessRepository coachesAccessRepository = Mockito.mock(CoachesAccessRepository.class);
    private final OwnerCoachesRepository ownerCoachesRepository = Mockito.mock(OwnerCoachesRepository.class);

    @Provides
    @Singleton
    public AdminsAccessRepository provideStaffAccessRepository() {
        return adminsAccessRepository;
    }

    @Provides
    @Singleton
    public OwnerAdminsRepository provideAdminsRepository() {
        return ownerAdminsRepository;
    }

    @Provides
    @Singleton
    public AdminsAccessManager provideAdminsAccessManager(AdminsAccessRepository accessRepository,
                                                          OwnerAdminsRepository ownerAdminsRepository) {
        return AdminsAccessManagerMocker.createMock(accessRepository, ownerAdminsRepository);
    }

    @Provides
    @Singleton
    public CoachesAccessRepository provideCoachesAccessRepository() {
        return coachesAccessRepository;
    }

    @Provides
    @Singleton
    public OwnerCoachesRepository provideOwnerCoachesRepository() {
        return ownerCoachesRepository;
    }

    @Provides
    @Singleton
    public CoachSessionsRepository provideCoachSessionsRepository() {
        return coachSessionsRepository;
    }

    @Provides
    @Singleton
    public CoachesAccessManager provideCachesAccessManager(CoachSessionsRepository coachSessionsRepository,
                                                           CoachesAccessRepository coachesAccessRepository,
                                                           OwnerCoachesRepository ownerCoachesRepository) {
        return CoachesAccessManagerMocker.createMock(coachSessionsRepository, coachesAccessRepository, ownerCoachesRepository);
    }
}
