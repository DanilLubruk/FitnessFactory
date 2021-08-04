package com.example.fitnessfactory.di;

import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.data.repositories.StaffAccessRepository;
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
    public StaffAccessRepository provideStaffAccessRepository() {
        return new StaffAccessRepository();
    }
}
