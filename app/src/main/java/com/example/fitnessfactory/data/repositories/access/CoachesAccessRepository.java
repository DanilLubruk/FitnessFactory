package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.CoachAccessCollection;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class CoachesAccessRepository extends PersonnelAccessRepository {

    @Override
    protected String getRoot() {
        return CoachAccessCollection.getRoot();
    }

    @Override
    protected CoachesAccessRepository.QueryBuilder newQuery() {
        return new CoachesAccessRepository(). new QueryBuilder();
    }
}
