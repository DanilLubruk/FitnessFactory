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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class GymEditorViewModelTests extends BaseTests {

    private OwnerGymRepository mockedOwnerGymRepository;

    private OwnerAdminsRepository ownerAdminsRepository;

    private OwnerCoachesRepository ownerCoachesRepository;

    private GymsAccessManager gymsAccessManager;

    private GymEditorViewModel viewModel;

    @Before
    public void setup() {
        super.setup();
        mockedOwnerGymRepository =
                OwnerGymRepositoryMocker.createMocker(Mockito.mock(OwnerGymRepository.class));
        ownerAdminsRepository = Mockito.mock(OwnerAdminsRepository.class);
        ownerCoachesRepository = Mockito.mock(OwnerCoachesRepository.class);

        gymsAccessManager =
                GymAccessManagerMocker.createMocker(
                        mockedOwnerGymRepository,
                        ownerAdminsRepository,
                        ownerCoachesRepository);


        viewModel = getViewModel(mockedOwnerGymRepository, gymsAccessManager, testScheduler);
    }

    private GymEditorViewModel getViewModel(OwnerGymRepository mockedOwnerGymRepository,
                                            GymsAccessManager gymsAccessManager,
                                            Scheduler scheduler) {
        GymEditorViewModel viewModel = new GymEditorViewModel(mockedOwnerGymRepository, gymsAccessManager);
        viewModel.setIOScheduler(scheduler);
        viewModel.setMainThreadScheduler(scheduler);
        viewModel.setRxErrorsHandler(new TestRxUtils());

        return viewModel;
    }

    @Test
    public void initViewModelTest() {
        initViewModel();
    }

    private void initViewModel() {
        Gym viewModelGym = viewModel.gym.get();
        assertNull(viewModelGym);

        SingleLiveEvent<Gym> gymData = viewModel.getGym("gymId2");
        testScheduler.triggerActions();
        Gym gym = getOrAwaitValue(gymData);
        assertNotNull(gym);
        assertEquals("gymId2", gym.getId());
        assertEquals("gymName2", gym.getName());
        assertEquals("gymAddress2", gym.getAddress());

        viewModel.setGym(gym);
        viewModelGym = viewModel.gym.get();
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

        gym.setName("gymName2");
        gym.setAddress("gymAddress2");
    }

    @Test
    public void saveNewGymTest() {
        boolean isSaved = getOrAwaitValue(viewModel.save());
        assertFalse(isSaved);

        SingleLiveEvent<Gym> gymData = viewModel.getGym("gymId6");
        testScheduler.triggerActions();
        Gym gym = getOrAwaitValue(gymData);
        assertNotNull(gym);
        assertEquals("", gym.getId());
        assertEquals("", gym.getName());
        assertEquals("", gym.getAddress());

        viewModel.setGym(gym);
        Gym viewModelGym = viewModel.gym.get();
        assertNotNull(viewModelGym);
        assertEquals("", viewModelGym.getId());
        assertEquals("", viewModelGym.getName());
        assertEquals("", viewModelGym.getAddress());

        gym.setName("gymName6");
        gym.setAddress("gymAddress6");

        SingleLiveEvent<Boolean> saveResult = viewModel.save();
        testScheduler.triggerActions();
        isSaved = getOrAwaitValue(saveResult);
        assertTrue(isSaved);

        Mockito.verify(mockedOwnerGymRepository).saveAsync(viewModelGym);

        viewModelGym = viewModel.gym.get();
        assertNotNull(viewModelGym);
        assertEquals("newGymId", viewModelGym.getId());
        assertEquals("gymName6", viewModelGym.getName());
        assertEquals("gymAddress6", viewModelGym.getAddress());
    }

    @Test
    public void saveTest() {
        boolean isSaved = getOrAwaitValue(viewModel.save());
        assertFalse(isSaved);

        initViewModel();

        SingleLiveEvent<Boolean> saveResult = viewModel.save();
        testScheduler.triggerActions();
        isSaved = getOrAwaitValue(saveResult);
        assertTrue(isSaved);

        String gymId = getOrAwaitValue(viewModel.getGymId());
        assertEquals("gymId2", gymId);

        Mockito.when(mockedOwnerGymRepository.saveAsync(Mockito.any()))
                .thenAnswer(invocation -> {
                    return Single.error(new Exception("Something wrong on the server side"));
                });

        saveResult = viewModel.save();
        testScheduler.triggerActions();
        isSaved = getOrAwaitValue(saveResult);
        assertFalse(isSaved);
    }

    @Test
    public void deleteTest() {
        SingleLiveEvent<Boolean> deleteResult = viewModel.delete();
        testScheduler.triggerActions();
        boolean isDeleted = getOrAwaitValue(deleteResult);
        assertFalse(isDeleted);

        initViewModel();

        SingleLiveEvent<Boolean> saveResult = viewModel.save();
        testScheduler.triggerActions();
        boolean isSaved = getOrAwaitValue(saveResult);
        assertTrue(isSaved);

        deleteResult = viewModel.delete();
        testScheduler.triggerActions();
        isDeleted = getOrAwaitValue(deleteResult);
        assertTrue(isDeleted);
    }

    @Test
    public void restoreStateTest() {
        initViewModel();

        boolean isModified = getOrAwaitValue(viewModel.isModified());
        assertFalse(isModified);

        Gym viewModelGym = viewModel.gym.get();
        assertNotNull(viewModelGym);
        viewModelGym.setName("anotherName");
        viewModelGym.setAddress("anotherAddress");

        Bundle state = new Bundle();
        viewModel.saveState(state);

        viewModel = getViewModel(mockedOwnerGymRepository, gymsAccessManager, testScheduler);
        viewModelGym = viewModel.gym.get();
        assertNull(viewModelGym);

        viewModel.restoreState(state);

        SingleLiveEvent<Gym> gymData = viewModel.getGym("gymId2");
        testScheduler.triggerActions();
        Gym gym = getOrAwaitValue(gymData);
        assertNotNull(gym);
        assertEquals("gymId2", gym.getId());
        assertEquals("anotherName", gym.getName());
        assertEquals("anotherAddress", gym.getAddress());

        viewModel.setGym(gym);
        viewModelGym = viewModel.gym.get();
        assertNotNull(viewModelGym);
        assertEquals("gymId2", viewModelGym.getId());
        assertEquals("anotherName", viewModelGym.getName());
        assertEquals("anotherAddress", viewModelGym.getAddress());
    }
}
