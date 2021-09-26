package com.example.fitnessfactory.mockHelpers;

import com.example.fitnessfactory.data.managers.access.AdminsAccessManager;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.models.Admin;
import com.example.fitnessfactory.data.models.AdminAccessEntry;
import com.example.fitnessfactory.data.models.Coach;
import com.example.fitnessfactory.data.models.CoachAccessEntry;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class CoachesAccessManagerMocker {

    private static List<CoachAccessEntry> coachesEntries;
    private static List<Coach> coaches;

    static {
        coachesEntries = new ArrayList<CoachAccessEntry>() {{
            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail2")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail3")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail4")
                    .setOwnerId("ownerId1")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail1")
                    .setOwnerId("ownerId2")
                    .build());

            add(CoachAccessEntry
                    .builder()
                    .setUserEmail("userEmail5")
                    .setOwnerId("ownerId2")
                    .build());
        }};

        coaches = new ArrayList<Coach>() {{
            add(Coach
                    .builder()
                    .setUserEmail("userEmail1")
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail2")
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail3")
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail4")
                    .build());

            add(Coach
                    .builder()
                    .setUserEmail("userEmail5")
                    .build());
        }};
    }

    public static PersonnelAccessManager createMock(PersonnelAccessRepository accessRepository,
                                                    OwnerPersonnelRepository ownersRepository) {
        AdminsAccessManager adminsAccessManager =
                new AdminsAccessManager(accessRepository, ownersRepository);

        Mockito.when(
                accessRepository
                        .isPersonnelWithThisEmailRegistered(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    String email = invocation.getArgument(1);

                    for (CoachAccessEntry coachEntry : coachesEntries) {
                        if (id.equals(coachEntry.getOwnerId()) &&
                                email.equals(coachEntry.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        Mockito.when(accessRepository
                .getRegisterPersonnelAccessEntryBatch(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository
                .getAddPersonnelBatch(Mockito.any(), Mockito.anyString()))
                .thenReturn(Single.just(FirebaseFirestore.getInstance().batch()));

        Mockito.when(ownersRepository.isPersonnelWithThisEmailAdded(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0);

                    for (Coach coach : coaches) {
                        if (email.equals(coach.getUserEmail())) {
                            return Single.just(true);
                        }
                    }

                    return Single.just(false);
                });

        return adminsAccessManager;
    }
}
