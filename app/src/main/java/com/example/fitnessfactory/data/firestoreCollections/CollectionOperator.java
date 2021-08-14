package com.example.fitnessfactory.data.firestoreCollections;

import com.google.firebase.firestore.CollectionReference;

public interface CollectionOperator {

    String getRoot();

    CollectionReference getCollection();
}
