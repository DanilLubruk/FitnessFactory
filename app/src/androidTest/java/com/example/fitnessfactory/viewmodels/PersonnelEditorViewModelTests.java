package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class PersonnelEditorViewModelTests extends BaseTests {

    protected OwnerPersonnelRepository ownerRepository;
    protected PersonnelAccessManager accessManager;
    protected PersonnelDataManager dataManager;
    protected DataListenerStringArgument dataListener;

    protected PersonnelEditorViewModel personnelEditorViewModel;

    protected AppUser personnel;
    protected String userId = "userId";
    protected String userName = "userName";
    protected String userEmail = "userEmail";

    @Before
    public void setup() {
        super.setup();
        ownerRepository = Mockito.mock(OwnerPersonnelRepository.class);
        accessManager = Mockito.mock(PersonnelAccessManager.class);
        dataManager = Mockito.mock(PersonnelDataManager.class);
        dataListener = Mockito.mock(DataListenerStringArgument.class);
        personnelEditorViewModel = getViewModel(ownerRepository, accessManager, dataManager, dataListener);

        personnel = new AppUser();
        personnel.setId(userId);
        personnel.setName(userName);
        personnel.setEmail(userEmail);
    }

    protected abstract PersonnelEditorViewModel getViewModel(OwnerPersonnelRepository ownerRepository,
                                                             PersonnelAccessManager accessManager,
                                                             PersonnelDataManager dataManager,
                                                             DataListenerStringArgument dataListener);

    protected abstract Intent getDataIntent(AppUser appUser);

    @Test
    public void initViewModelTest() {
        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));
        AppUser viewModelPersonnel = personnelEditorViewModel.personnel.get();

        assertNotNull(viewModelPersonnel);
        assertEquals(viewModelPersonnel.getId(), userId);
        assertEquals(viewModelPersonnel.getName(), userName);
        assertEquals(viewModelPersonnel.getEmail(), userEmail);

        personnelEditorViewModel.startDataListener();

        Mockito.verify(dataListener).startDataListener(userEmail);
    }
}
