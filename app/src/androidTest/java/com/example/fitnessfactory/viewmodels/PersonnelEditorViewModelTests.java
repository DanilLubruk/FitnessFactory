package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.editors.PersonnelEditorViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.List;

import io.reactivex.Completable;

@RunWith(AndroidJUnit4.class)
public abstract class PersonnelEditorViewModelTests extends BaseTests {

    protected UserRepository userRepository = Mockito.mock(UserRepository.class);

    protected OwnerGymRepository gymRepository = Mockito.mock(OwnerGymRepository.class);

    protected PersonnelEditorViewModel personnelEditorViewModel;

    protected String ownerId = "ownerId";

    protected AppUser personnel;
    protected String userId = "userId4";
    protected String userName = "User4";
    protected String userEmail = "useremail4";

    protected Gym gym;
    protected String gymId = "gymId";
    protected String gymName = "gymName";
    protected String gymAddress = "gymAddress";

    @Before
    public void setup() {
        super.setup();
        personnelEditorViewModel = getViewModel();
        personnelEditorViewModel.setIOScheduler(testScheduler);
        personnelEditorViewModel.setMainThreadScheduler(testScheduler);

        personnel = new AppUser();
        personnel.setId(userId);
        personnel.setName(userName);
        personnel.setEmail(userEmail);

        gym = new Gym();
        gym.setId(gymId);
        gym.setName(gymName);
        gym.setAddress(gymAddress);
    }

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract PersonnelAccessManager getAccessManager();

    protected abstract PersonnelDataManager getDataManager();

    protected abstract ArgDataListener getDataListener();

    protected abstract PersonnelEditorViewModel getViewModel();

    protected abstract Intent getDataIntent(AppUser appUser);

    @Test
    public void initViewModelTest() {
        personnelEditorViewModel.getGymsData();
        testScheduler.triggerActions();
        checkLiveDataNotSet(personnelEditorViewModel.getGyms());

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));
        AppUser viewModelPersonnel = personnelEditorViewModel.personnel.get();

        assertNotNull(viewModelPersonnel);
        assertEquals(viewModelPersonnel.getId(), userId);
        assertEquals(viewModelPersonnel.getName(), userName);
        assertEquals(viewModelPersonnel.getEmail(), userEmail);

        personnelEditorViewModel.startDataListener();

        Mockito.verify(getDataListener()).startDataListener(userEmail);

        personnelEditorViewModel.getGymsData();
        testScheduler.triggerActions();
        List<Gym> gyms = getOrAwaitValue(personnelEditorViewModel.getGyms());

        assertNotNull(gyms);

        assertEquals(2, gyms.size());
        assertEquals("gymId2", gyms.get(0).getId());
        assertEquals("gymName2", gyms.get(0).getName());
        assertEquals("gymAddress2", gyms.get(0).getAddress());

        assertEquals("gymId3", gyms.get(1).getId());
        assertEquals("gymName3", gyms.get(1).getName());
        assertEquals("gymAddress3", gyms.get(1).getAddress());

        personnel.setEmail("notTheRightEmail");
        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.getGymsData();
        testScheduler.triggerActions();
        gyms = getOrAwaitValue(personnelEditorViewModel.getGyms());

        assertNotNull(gyms);
        assertEquals(0, gyms.size());

        personnelEditorViewModel.stopDataListener();
        Mockito.verify(getDataListener()).stopDataListener();
    }

    @Test
    public void addGymTest() {
        Mockito.when(getOwnerRepository().addGymToPersonnel(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        personnelEditorViewModel.addGym(gym.getId());
        Mockito.verify(getOwnerRepository(), Mockito.times(0))
                .addGymToPersonnel(userEmail, gymId);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.addGym(gym.getId());
        Mockito.verify(getOwnerRepository()).addGymToPersonnel(userEmail, gymId);
    }

    @Test
    public void deleteGymTest() {
        Mockito.when(getOwnerRepository().removeGymFromPersonnel(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        personnelEditorViewModel.deleteItem(gym);
        Mockito.verify(getOwnerRepository(), Mockito.times(0))
                .removeGymFromPersonnel(userEmail, gymId);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.deleteItem(gym);
        Mockito.verify(getOwnerRepository())
                .removeGymFromPersonnel(userEmail, gymId);
    }

    @Test
    public void isModifiedTest() {
        boolean isModified = getOrAwaitValue(personnelEditorViewModel.isModified());
        assertFalse(isModified);
    }

    @Test
    public void saveTest() {
        boolean isSaved = getOrAwaitValue(personnelEditorViewModel.save());
        assertTrue(isSaved);
    }

    @Test
    public void deletePersonnelTest() {
        AppPrefs.gymOwnerId().setValue(ownerId);

        SingleLiveEvent<Boolean> deleteResult = personnelEditorViewModel.delete();
        testScheduler.triggerActions();
        boolean isDeleted = getOrAwaitValue(deleteResult);
        assertFalse(isDeleted);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        deleteResult = personnelEditorViewModel.delete();
        testScheduler.triggerActions();
        isDeleted = getOrAwaitValue(deleteResult);
        assertTrue(isDeleted);

        personnel.setEmail("notTheRightEmail");
        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        deleteResult = personnelEditorViewModel.delete();
        testScheduler.triggerActions();
        isDeleted = getOrAwaitValue(deleteResult);
        assertFalse(isDeleted);

        AppPrefs.gymOwnerId().resetToDefaultValue();
    }
}
