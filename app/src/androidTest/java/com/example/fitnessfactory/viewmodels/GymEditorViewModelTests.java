package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockers.OwnerGymRepositoryMocker;
import com.example.fitnessfactory.mockHelpers.mockers.access.GymAccessManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GymEditorViewModelTests extends BaseTests {

    private OwnerGymRepository mockedOwnerGymRepository =
            OwnerGymRepositoryMocker.createMocker(Mockito.mock(OwnerGymRepository.class));

    private OwnerAdminsRepository ownerAdminsRepository = Mockito.mock(OwnerAdminsRepository.class);

    private OwnerCoachesRepository ownerCoachesRepository = Mockito.mock(OwnerCoachesRepository.class);

    private GymsAccessManager gymsAccessManager;

    private GymEditorViewModel viewModel;

    @Before
    public void setup() {
        super.setup();
        gymsAccessManager =
                GymAccessManagerMocker.createMocker(
                        mockedOwnerGymRepository,
                        ownerAdminsRepository,
                        ownerCoachesRepository);

        viewModel = new GymEditorViewModel(mockedOwnerGymRepository, gymsAccessManager);
        viewModel.setIoScheduler(testScheduler);
        viewModel.setMainScheduler(testScheduler);
        viewModel.setRxErrorsHandler(new TestRxUtils());
    }

    @Test
    public void initViewModelTest() {
        initViewModel();
        //String gymId = getOrAwaitValue(viewModel.getGymId());
        //assertEquals("gymId2", gymId);
    }

    private void initViewModel() {
        SingleLiveEvent<Gym> gymData = viewModel.getGym("gymId2");
        testScheduler.triggerActions();
        Gym gym = getOrAwaitValue(gymData);
        assertNotNull(gym);
        assertEquals("gymId2", gym.getId());
        assertEquals("gymName2", gym.getName());
        assertEquals("gymAddress2", gym.getAddress());

        viewModel.setGym(gym);
        Gym viewModelGym = viewModel.gym.get();
        assertNotNull(viewModelGym);
        assertEquals("gymId2", viewModelGym.getId());
        assertEquals("gymName2", viewModelGym.getName());
        assertEquals("gymAddress2", viewModelGym.getAddress());
    }

    @Test
    public void isModifiedTest() {
        boolean isModified = getOrAwaitValue(viewModel.isModified());
        assertFalse(isModified);

        initViewModel();

        isModified = getOrAwaitValue(viewModel.isModified());
        assertFalse(isModified);

        Gym gym = viewModel.gym.get();
        assertNotNull(gym);
        gym.setName("anotherName");

        isModified = getOrAwaitValue(viewModel.isModified());
        assertTrue(isModified);
        gym.setName("gymName2");

        isModified = getOrAwaitValue(viewModel.isModified());
        assertFalse(isModified);

        gym.setAddress("anotherAddress");
        isModified = getOrAwaitValue(viewModel.isModified());
        assertTrue(isModified);
        gym.setAddress("gymAddress2");

        isModified = getOrAwaitValue(viewModel.isModified());
        assertFalse(isModified);

        gym.setName("anotherName");
        gym.setAddress("anotherAddress");
        isModified = getOrAwaitValue(viewModel.isModified());
        assertTrue(isModified);
    }

    @Test
    public void saveTest() {
        boolean isSaved = getOrAwaitValue(viewModel.save());
        assertFalse(isSaved);

        initViewModel();

        SingleLiveEvent<Boolean> saveResult = viewModel.save();
        testScheduler.triggerActions();
        isSaved = getOrAwaitValue(saveResult);
    }

    @Test
    public void restoreStateTest() {

    }
}
