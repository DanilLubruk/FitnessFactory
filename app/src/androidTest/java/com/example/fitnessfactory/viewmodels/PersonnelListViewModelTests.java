package com.example.fitnessfactory.viewmodels;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.dataListeners.DataListener;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.access.PersonnelAccessRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.PersonnelListViewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import io.reactivex.Single;

@RunWith(AndroidJUnit4.class)
public abstract class PersonnelListViewModelTests extends BaseTests {

    protected UserRepository userRepository = Mockito.mock(UserRepository.class);

    protected OwnerGymRepository gymRepository = Mockito.mock(OwnerGymRepository.class);

    protected PersonnelListViewModel personnelListViewModel;

    @Before
    public void setup() {
        super.setup();
        personnelListViewModel = initViewModel();
        personnelListViewModel.setIOScheduler(testScheduler);
        personnelListViewModel.setMainThreadScheduler(testScheduler);
        personnelListViewModel.setRxErrorsHandler(new TestRxUtils());
    }

    protected abstract PersonnelListViewModel initViewModel();

    protected abstract PersonnelAccessManager getAccessManager();

    protected abstract PersonnelDataManager getDataManager();

    protected abstract PersonnelAccessRepository getAccessRepository();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract DataListener getDataListener();

    @Test
    public void tryRegisterRegisteredPersonnelTest() {
        AppPrefs.gymOwnerId().setValue("ownerId1");
        personnelListViewModel.registerPersonnel(
                Single.just("userEmail1"),
                Single.just(false),
                false);
        testScheduler.triggerActions();

        Mockito.verify(getAccessRepository())
                .isPersonnelWithThisIdRegisteredAsync("ownerId1", "useremail1");

        Mockito.verify(getAccessRepository(), Mockito.times(0))
                .getRegisterPersonnelAccessEntryBatchAsync(Mockito.any(), Mockito.anyString());

        AppPrefs.gymOwnerId().resetToDefaultValue();
    }

    @Test
    public void registerPersonnelTest() {
        AppPrefs.gymOwnerId().setValue("ownerId1");
        personnelListViewModel.registerPersonnel(
                Single.just("userEmail5"),
                Single.just(false),
                false);
        testScheduler.triggerActions();

        Mockito.verify(getAccessRepository())
                .isPersonnelWithThisIdRegisteredAsync("ownerId1", "useremail5");

        Mockito.verify(getAccessRepository())
                .getRegisterPersonnelAccessEntryBatchAsync("ownerId1", "useremail5");

        Mockito.verify(getOwnerRepository()).isPersonnelWithThisIdAddedAsync("useremail5");

        Mockito.verify(getOwnerRepository())
                .getAddPersonnelBatchAsync(Mockito.any(), Mockito.eq("useremail5"));

        AppPrefs.gymOwnerId().resetToDefaultValue();
    }

    @Test
    public void registerPersonnelWithAskSendInvitationTest() {

    }

    @Test
    public void loadListTest() {
        personnelListViewModel.startDataListener();
        Mockito.verify(getDataListener()).startDataListener();

        personnelListViewModel.getPersonnelListData();
        testScheduler.triggerActions();
        List<AppUser> personnelList = getOrAwaitValue(personnelListViewModel.getPersonnel());

        assertNotNull(personnelList);
        assertEquals(4, personnelList.size());

        for (int i = 0; i < personnelList.size(); i++) {
            AppUser appUser = personnelList.get(i);
            int userNumber = i + 1;

            assertEquals("userId" + userNumber, appUser.getId());
            assertEquals("User" + userNumber, appUser.getName());
            assertEquals("useremail" + userNumber, appUser.getEmail());
        }

        personnelListViewModel.stopDataListener();
        Mockito.verify(getDataListener()).stopDataListener();
    }

    @Test
    public void deletePersonnelItemTest() {
        AppPrefs.gymOwnerId().setValue("ownerId1");

        personnelListViewModel.startDataListener();
        Mockito.verify(getDataListener()).startDataListener();

        personnelListViewModel.deleteItem(
                AppUser
                        .builder()
                        .setEmail("useremail3")
                        .build());
        testScheduler.triggerActions();

        Mockito.verify(getAccessRepository())
                .getDeletePersonnelAccessEntryBatchAsync(
                        "ownerId1",
                        "useremail3");
        Mockito.verify(getOwnerRepository())
                .getDeletePersonnelBatchAsync(Mockito.any(), Mockito.eq("useremail3"));

        personnelListViewModel.stopDataListener();
        Mockito.verify(getDataListener()).stopDataListener();

        AppPrefs.gymOwnerId().resetToDefaultValue();
    }
}
