package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerAdminsRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.mockHelpers.mockers.access.GymAccessManagerMocker;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GymsListViewModelTests extends BaseTests {

    private OwnerGymRepository ownerGymRepository = Mockito.mock(OwnerGymRepository.class);

    private OwnerAdminsRepository ownerAdminsRepository = Mockito.mock(OwnerAdminsRepository.class);

    private OwnerCoachesRepository ownerCoachesRepository = Mockito.mock(OwnerCoachesRepository.class);

    private GymsAccessManager gymsAccessManager;

    private GymsListDataListener gymsListDataListener = Mockito.mock(GymsListDataListener.class);

    private GymsListViewModel viewModel;

    @Before
    public void setup() {
        super.setup();
        gymsAccessManager =
                GymAccessManagerMocker
                        .createMocker(
                                ownerGymRepository,
                                ownerAdminsRepository,
                                ownerCoachesRepository);

        viewModel = new GymsListViewModel(gymsAccessManager, gymsListDataListener);
        viewModel.setIoScheduler(testScheduler);
        viewModel.setMainScheduler(testScheduler);
        viewModel.setRxErrorsHandler(new TestRxUtils());
    }

    @Test
    public void initListTest() {
        viewModel.startDataListener();
        testScheduler.triggerActions();
        Mockito.verify(gymsListDataListener).startDataListener();

        viewModel.stopDataListener();
        testScheduler.triggerActions();
        Mockito.verify(gymsListDataListener).stopDataListener();
    }

    @Test
    public void deleteGymTest() {
        viewModel.deleteItem(null);
        testScheduler.triggerActions();
        Mockito.verify(ownerGymRepository, Mockito.times(0)).getDeleteGymBatchAsync(Mockito.anyString());
        Mockito.verify(ownerAdminsRepository, Mockito.times(0))
                .getRemoveGymFromAdminBatchAsync(Mockito.any(), Mockito.anyString());
        Mockito.verify(ownerCoachesRepository, Mockito.times(0))
                .getRemoveGymFromCoachBatchAsync(Mockito.any(), Mockito.anyString());

        viewModel.deleteItem(Gym
                        .builder()
                        .setId("gymId2")
                        .build());
        testScheduler.triggerActions();

        Mockito.verify(ownerGymRepository).getDeleteGymBatchAsync("gymId2");
        Mockito.verify(ownerAdminsRepository)
                .getRemoveGymFromAdminBatchAsync(Mockito.any(), Mockito.eq("gymId2"));
        Mockito.verify(ownerCoachesRepository)
                .getRemoveGymFromCoachBatchAsync(Mockito.any(), Mockito.eq("gymId2"));
    }
}
