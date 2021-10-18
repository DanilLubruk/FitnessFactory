package com.example.fitnessfactory.data.repositories.access;

import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Single;

public abstract class PersonnelAccessRepository {

    public abstract Single<Boolean> isPersonnelWithThisEmailRegistered(String ownerId, String userEmail);

    public abstract Single<WriteBatch> getRegisterPersonnelAccessEntryBatch(String ownerId, String userEmail);

    public abstract Single<WriteBatch> getDeletePersonnelAccessEntryBatch(String ownerId, String userEmail);
}
