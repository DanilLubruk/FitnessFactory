package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.CoachesDataProvider;

public class CoachesAccessManagerMocker {

    public static CoachesAccessManager createMock(CoachesAccessRepository accessRepository,
                                                  OwnerCoachesRepository ownersRepository) {
        CoachesAccessManager coachesAccessManager =
                new CoachesAccessManager(accessRepository, ownersRepository);

        PersonnelAccessManagerMocker.setupMock(
                new CoachesDataProvider(),
                accessRepository,
                ownersRepository);

        return coachesAccessManager;
    }
}
