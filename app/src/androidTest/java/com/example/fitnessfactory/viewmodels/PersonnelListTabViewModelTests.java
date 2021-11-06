package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymPersonnelListTabViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import io.reactivex.Completable;

public abstract class PersonnelListTabViewModelTests extends BaseTests {

    protected UserRepository userRepository = Mockito.mock(UserRepository.class);

    protected OwnerGymRepository gymRepository = Mockito.mock(OwnerGymRepository.class);

    private GymPersonnelListTabViewModel viewModel;

    protected abstract GymPersonnelListTabViewModel initViewModel();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract PersonnelDataManager getPersonnelDataManager();

    protected abstract ArgDataListener getDataListener();

    @Before
    public void setup() {
        super.setup();
        viewModel = initViewModel();
        viewModel.setIOScheduler(testScheduler);
        viewModel.setMainThreadScheduler(testScheduler);
        viewModel.setRxErrorsHandler(new TestRxUtils());
    }

    @Test
    public void initViewModelTest() {
        viewModel.startDataListener();
        Mockito.verify(getDataListener(), Mockito.times(0)).startDataListener(Mockito.anyString());

        viewModel.getPersonnelData();
        testScheduler.triggerActions();
        checkLiveDataNotSet(viewModel.getPersonnel());

        Mockito.verify(getOwnerRepository(), Mockito.times(0))
                .getPersonnelEmailsByGymIdAsync(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(0))
                .getUsersByEmailsAsync(Mockito.anyList());

        viewModel.resetGymId("gymId2");

        viewModel.startDataListener();
        testScheduler.triggerActions();
        Mockito.verify(getDataListener()).startDataListener("gymId2");

        viewModel.getPersonnelData();
        testScheduler.triggerActions();

        Mockito.verify(getOwnerRepository()).getPersonnelEmailsByGymIdAsync("gymId2");
        Mockito.verify(userRepository).getUsersByEmailsAsync(Mockito.anyList());

        List<AppUser> personnel = getOrAwaitValue(viewModel.getPersonnel());

        assertEquals(2, personnel.size());

        assertEquals("userId3", personnel.get(0).getId());
        assertEquals("User3", personnel.get(0).getName());
        assertEquals("useremail3", personnel.get(0).getEmail());

        assertEquals("userId4", personnel.get(1).getId());
        assertEquals("User4", personnel.get(1).getName());
        assertEquals("useremail4", personnel.get(1).getEmail());

        viewModel.stopDataListener();
        Mockito.verify(getDataListener()).stopDataListener();
    }

    @Test
    public void addPersonnelToGymTest() {
        Mockito.when(getOwnerRepository().addGymToPersonnelAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        viewModel.addPersonnelToGym("useremail1");
        testScheduler.triggerActions();

        Mockito.verify(getOwnerRepository(), Mockito.times(0))
                .addGymToPersonnelAsync(Mockito.any(), Mockito.anyString());

        viewModel.resetGymId("gymId2");

        viewModel.addPersonnelToGym("useremail1");
        testScheduler.triggerActions();

        Mockito.verify(getOwnerRepository()).addGymToPersonnelAsync("useremail1", "gymId2");
    }

    @Test
    public void deletePersonnelItemTest() {
        Mockito.when(getOwnerRepository().removeGymFromPersonnelAsync(Mockito.any(), Mockito.anyString()))
                .thenReturn(Completable.complete());

        AppUser personnel = AppUser
                        .builder()
                        .setEmail("useremail3")
                        .build();

        viewModel.deleteItem(personnel);
        testScheduler.triggerActions();

        Mockito.verify(getOwnerRepository(), Mockito.times(0))
                .removeGymFromPersonnelAsync(Mockito.any(), Mockito.anyString());

        viewModel.resetGymId("gymId2");

        viewModel.deleteItem(personnel);
        testScheduler.triggerActions();

        Mockito.verify(getOwnerRepository()).removeGymFromPersonnelAsync("useremail3", "gymId2");
    }
}
