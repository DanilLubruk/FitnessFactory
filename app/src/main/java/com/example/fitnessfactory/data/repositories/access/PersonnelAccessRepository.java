package com.example.fitnessfactory.data.repositories.access;

import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Single;

public interface PersonnelAccessRepository {

    Single<Boolean> isPersonnelWithThisEmailRegistered(String ownerId, String userEmail);

    Single<WriteBatch> getRegisterPersonnelAccessEntryBatch(String ownerId, String userEmail);

    Single<WriteBatch> getDeletePersonnelAccessEntryBatch(String ownerId, String userEmail);
}
