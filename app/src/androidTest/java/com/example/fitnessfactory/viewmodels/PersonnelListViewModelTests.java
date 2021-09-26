package com.example.fitnessfactory.viewmodels;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.dataListeners.DataListener;
import com.example.fitnessfactory.data.managers.access.PersonnelAccessManager;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListViewModel;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class PersonnelListViewModelTests extends BaseTests {

    private PersonnelAccessManager accessManager;
    private PersonnelDataManager dataManager;
    private DataListener dataListener;

    protected PersonnelListViewModel personnelListViewModel;

    @Before
    public void setup() {
        super.setup();

    }

    protected abstract PersonnelListViewModel getViewModel(PersonnelAccessManager accessManager,
                                                           PersonnelDataManager dataManager,
                                                           DataListener dataListener);
}
