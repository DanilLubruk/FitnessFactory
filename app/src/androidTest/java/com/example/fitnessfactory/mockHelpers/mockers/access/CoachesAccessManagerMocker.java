package com.example.fitnessfactory.mockHelpers.mockers.access;

import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.repositories.access.CoachesAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.participantsData.CoachSessionsRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.personnel.PersonnelDataProvider;

public class CoachesAccessManagerMocker {

    public static CoachesAccessManager createMock(CoachSessionsRepository coachSessionsRepository,
                                                  CoachesAccessRepository accessRepository,
                                                  OwnerCoachesRepository ownersRepository) {
        CoachesAccessManager coachesAccessManager =
                new CoachesAccessManager(coachSessionsRepository, accessRepository, ownersRepository);

        PersonnelAccessManagerMocker.setupMock(
                new PersonnelDataProvider(),
                accessRepository,
                ownersRepository);

        return coachesAccessManager;
    }
}
