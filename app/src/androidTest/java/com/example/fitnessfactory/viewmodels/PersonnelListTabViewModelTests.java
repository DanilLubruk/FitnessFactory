package com.example.fitnessfactory.viewmodels;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.TestRxUtils;
import com.example.fitnessfactory.data.dataListeners.DataListenerStringArgument;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListTabViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public abstract class PersonnelListTabViewModelTests extends BaseTests {

    protected UserRepository userRepository = Mockito.mock(UserRepository.class);

    protected OwnerGymRepository gymRepository = Mockito.mock(OwnerGymRepository.class);

    private PersonnelListTabViewModel viewModel;

    protected abstract PersonnelListTabViewModel initViewModel();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected abstract PersonnelDataManager getPersonnelDataManager();

    protected abstract DataListenerStringArgument getDataListener();

    @Before
    public void setup() {
        super.setup();
        viewModel = initViewModel();
        viewModel.setIoScheduler(testScheduler);
        viewModel.setMainScheduler(testScheduler);
        viewModel.setRxErrorsHandler(new TestRxUtils());
    }

    @Test
    public void addPersonnelToGymTest() {

    }
}
