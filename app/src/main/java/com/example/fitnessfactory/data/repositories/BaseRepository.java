package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.AppPrefs;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseRepository {

    private CollectionReference colReference;

    protected String getRoot() {
        return AppPrefs.gymOwnerId().getValue();
    }

    protected CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    private void initCollection() {
        colReference = FirebaseFirestore.getInstance().collection(getRoot());
    }

}
