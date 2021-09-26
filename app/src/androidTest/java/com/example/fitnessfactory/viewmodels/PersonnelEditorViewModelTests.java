package com.example.fitnessfactory.viewmodels;

import android.content.Intent;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.PersonnelEditorViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@RunWith(AndroidJUnit4.class)
public abstract class PersonnelEditorViewModelTests extends BaseTests {

    protected OwnerPersonnelRepository ownerRepository;
    protected PersonnelAccessManager accessManager;
    protected PersonnelDataManager dataManager;
    protected DataListenerStringArgument dataListener;

    protected PersonnelEditorViewModel personnelEditorViewModel;

    protected String ownerId = "ownerId";

    protected AppUser personnel;
    protected String userId = "userId";
    protected String userName = "userName";
    protected String userEmail = "userEmail";

    protected Gym gym;
    protected String gymId = "gymId";
    protected String gymName = "gymName";
    protected String gymAddress = "gymAddress";

    @Before
    public void setup() {
        super.setup();
        ownerRepository = Mockito.mock(OwnerPersonnelRepository.class);
        accessManager = Mockito.mock(PersonnelAccessManager.class);
        dataManager = Mockito.mock(PersonnelDataManager.class);
        dataListener = Mockito.mock(DataListenerStringArgument.class);
        personnelEditorViewModel = getViewModel(ownerRepository, accessManager, dataManager, dataListener);
        personnelEditorViewModel.setIoScheduler(testScheduler);
        personnelEditorViewModel.setMainScheduler(testScheduler);

        personnel = new AppUser();
        personnel.setId(userId);
        personnel.setName(userName);
        personnel.setEmail(userEmail);

        gym = new Gym();
        gym.setId(gymId);
        gym.setName(gymName);
        gym.setAddress(gymAddress);
    }

    protected abstract PersonnelEditorViewModel getViewModel(OwnerPersonnelRepository ownerRepository,
                                                             PersonnelAccessManager accessManager,
                                                             PersonnelDataManager dataManager,
                                                             DataListenerStringArgument dataListener);

    protected abstract Intent getDataIntent(AppUser appUser);

    @Test
    public void initViewModelTest() {
        Mockito.when(dataManager.getPersonnelGymsByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                   String emailArgument = invocation.getArgument(0);

                   if (emailArgument.equals(userEmail)) {
                       return Single.just(getGymsList());
                   } else {
                       return Single.just(new ArrayList<Gym>());
                   }
                });

        personnelEditorViewModel.getGymsData();
        Mockito.verify(dataManager, Mockito.times(0))
                .getPersonnelGymsByEmail(userEmail);
        personnelEditorViewModel.getGyms().observeForever(gyms -> {
            assertNull(gyms);
        });


        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));
        AppUser viewModelPersonnel = personnelEditorViewModel.personnel.get();

        assertNotNull(viewModelPersonnel);
        assertEquals(viewModelPersonnel.getId(), userId);
        assertEquals(viewModelPersonnel.getName(), userName);
        assertEquals(viewModelPersonnel.getEmail(), userEmail);

        personnelEditorViewModel.startDataListener();

        Mockito.verify(dataListener).startDataListener(userEmail);

        personnelEditorViewModel.getGymsData();
        Mockito.verify(dataManager).getPersonnelGymsByEmail(userEmail);
        personnelEditorViewModel.getGyms().observeForever(gyms -> {
            assertNotNull(gyms);
            assertEquals(3, gyms.size());

            for (int i = 0; i < gyms.size(); i++) {
                Gym gym = gyms.get(0);

                int gymNumber = i + 1;
                assertEquals("id" + gymNumber, gym.getId());
                assertEquals("Name" + gymNumber, gym.getName());
                assertEquals("Address" + gymNumber, gym.getAddress());
            }
        });


        personnel.setEmail("notTheRightEmail");
        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.getGymsData();
        Mockito.verify(dataManager).getPersonnelGymsByEmail("notTheRightEmail");
        personnelEditorViewModel.getGyms().observeForever(gyms -> {
            assertNotNull(gyms);
            assertEquals(0, gyms.size());
        });

        personnelEditorViewModel.stopDataListener();
        Mockito.verify(dataListener).stopDataListener();
    }

    @Test
    public void addGymTest() {
        Mockito.when(ownerRepository.addGymToPersonnel(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        personnelEditorViewModel.addGym(gym.getId());
        Mockito.verify(ownerRepository, Mockito.times(0))
                .addGymToPersonnel(userEmail, gymId);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.addGym(gym.getId());
        Mockito.verify(ownerRepository).addGymToPersonnel(userEmail, gymId);
    }

    @Test
    public void deleteGymTest() {
        Mockito.when(ownerRepository.removeGymFromPersonnel(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        personnelEditorViewModel.deleteItem(gym);
        Mockito.verify(ownerRepository, Mockito.times(0))
                .removeGymFromPersonnel(userEmail, gymId);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        personnelEditorViewModel.deleteItem(gym);
        Mockito.verify(ownerRepository)
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
        boolean isDeleted = getOrAwaitValue(personnelEditorViewModel.delete());
        assertFalse(isDeleted);

        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        Mockito.when(accessManager.deletePersonnelSingle(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                   String ownerIdArg = invocation.getArgument(0);
                   String emailArg = invocation.getArgument(1);

                   if (ownerIdArg.equals(ownerId) && emailArg.equals(userEmail)) {
                       return Single.just(true);
                   } else {
                       return Single.just(false);
                   }
                });

        isDeleted = getOrAwaitValue(personnelEditorViewModel.delete());
        assertTrue(isDeleted);

        personnel.setEmail("notTheRightEmail");
        personnelEditorViewModel.setPersonnelData(getDataIntent(personnel));

        isDeleted = getOrAwaitValue(personnelEditorViewModel.delete());
        assertFalse(isDeleted);
    }

    private List<Gym> getGymsList() {
        return new ArrayList<Gym>() {{
            add(Gym.builder()
                    .setId("id1")
                    .setName("Name1")
                    .setAddress("Address1")
                    .build());

            add(Gym.builder()
                    .setId("id2")
                    .setName("Name2")
                    .setAddress("Address2")
                    .build());

            add(Gym.builder()
                    .setId("id3")
                    .setName("Name3")
                    .setAddress("Address3")
                    .build());
        }};
    }
}
