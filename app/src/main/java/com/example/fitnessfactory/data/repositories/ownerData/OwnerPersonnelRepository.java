package com.example.fitnessfactory.data.repositories.ownerData;

import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import io.reactivex.Single;

public interface OwnerPersonnelRepository {

    Single<Boolean> isPersonnelWithThisEmailAdded(String email);

    Single<WriteBatch> getAddPersonnelBatch(WriteBatch writeBatch, String email);

    Single<WriteBatch> getDeletePersonnelBatch(WriteBatch writeBatch, String email);

    Single<List<String>> getPersonnelEmails();

    Single<List<String>> getPersonnelEmailsByGymId(String gymId);
}
