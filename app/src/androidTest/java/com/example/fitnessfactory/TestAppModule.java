package com.example.fitnessfactory;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.di.AppScope;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule {

    @Provides
    @Singleton
    public AdminsAccessRepository provideStaffAccessRepository() {
        return Mockito.mock(AdminsAccessRepository.class);
    }

    @Provides
    @Singleton
    public OwnerAdminsRepository provideAdminsRepository() {
        return Mockito.mock(OwnerAdminsRepository.class);
    }

    @Provides
    @Singleton
    public AdminsAccessManager provideAdminsAccessManager(AdminsAccessRepository accessRepository,
                                                          OwnerAdminsRepository ownerAdminsRepository) {
        return new AdminsAccessManager(accessRepository, ownerAdminsRepository);
    }
}
