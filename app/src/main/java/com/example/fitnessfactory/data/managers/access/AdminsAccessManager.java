package com.example.fitnessfactory.data.managers.access;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.repositories.access.AdminsAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.firebase.firestore.WriteBatch;

import javax.inject.Inject;

import io.reactivex.Single;

public class AdminsAccessManager extends PersonnelAccessManager {

    @Inject
    public AdminsAccessManager(AdminsAccessRepository accessRepository,
                               OwnerAdminsRepository ownerRepository) {
        super(accessRepository, ownerRepository);
    }

    protected Single<WriteBatch> getDeleteBatch(String ownerId, String personnelEmail) {
        return ownerRepository.isPersonnelOccupiedWithGyms(personnelEmail)
                .flatMap(isOccupied -> isOccupied ?
                        Single.error(new Exception(getOccupiedMessage()))
                        : super.getDeleteBatch(ownerId, personnelEmail));
    }

    @Override
    protected String getAlreadyRegisteredMessage() {
        return ResUtils.getString(R.string.message_admin_is_registered);
    }

    private String getOccupiedMessage() {
        return String.format(
                ResUtils.getString(R.string.message_error_item_occupied),
                ResUtils.getString(R.string.caption_admin));
    }
}
