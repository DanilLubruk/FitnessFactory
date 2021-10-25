package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.CoachAccessCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.tasks.Tasks;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class CoachesAccessRepository extends PersonnelAccessRepository {

    @Override
    protected String getRoot() {
        return CoachAccessCollection.getRoot();
    }

    @Override
    protected CoachesAccessRepository.QueryBuilder newQuery() {
        return new CoachesAccessRepository().new QueryBuilder();
    }
}
