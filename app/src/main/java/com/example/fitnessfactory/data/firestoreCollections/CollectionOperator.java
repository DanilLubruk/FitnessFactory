package com.example.fitnessfactory.data.firestoreCollections;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class CollectionOperator {

    private CollectionReference colReference;

    protected abstract String getRoot();

    protected CollectionReference getCollection() {
        initCollection();
        return colReference;
    }

    protected FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }

    private void initCollection() {
        colReference = FirebaseFirestore.getInstance().collection(getRoot());
    }
}
