package com.example.fitnessfactory.managers.data;

import static org.junit.Assert.assertEquals;

import com.example.fitnessfactory.BaseTests;
import com.example.fitnessfactory.data.managers.data.PersonnelDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerPersonnelRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import io.reactivex.observers.TestObserver;

public abstract class PersonnelDataManagerTests extends BaseTests {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private OwnerGymRepository gymRepository = Mockito.mock(OwnerGymRepository.class);

    protected PersonnelDataManager dataManager;

    @Before
    public void setup() {
        super.setup();
        dataManager = instantiateDataManager();
    }

    protected abstract PersonnelDataManager instantiateDataManager();

    protected abstract OwnerPersonnelRepository getOwnerRepository();

    protected UserRepository getUserRepository() {
        return userRepository;
    }

    protected OwnerGymRepository getGymRepository() {
        return gymRepository;
    }

    @Test
    public void getPersonnelUsersListTest() {
        TestObserver<List<AppUser>> subscriber = subscribe(dataManager.getPersonnelListAsync());

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        List<AppUser> users = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(4, users.size());
        for (int i = 0; i < users.size(); i++) {
            AppUser user = users.get(i);
            int userNumber = i + 1;

            assertEquals("userId" + userNumber, user.getId());
            assertEquals("User" + userNumber, user.getName());
            assertEquals("useremail" + userNumber, user.getEmail());
        }
    }

    @Test
    public void getPersonnelListByGymIdTest() {
        TestObserver<List<AppUser>> subscriber =
                subscribe(dataManager.getPersonnelListByGymIdAsync("gymId1"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        List<AppUser> users = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(2, users.size());
        assertEquals("userId1", users.get(0).getId());
        assertEquals("User1", users.get(0).getName());
        assertEquals("useremail1", users.get(0).getEmail());

        assertEquals("userId2", users.get(1).getId());
        assertEquals("User2", users.get(1).getName());
        assertEquals("useremail2", users.get(1).getEmail());

        subscriber = subscribe(dataManager.getPersonnelListByGymIdAsync("gymId2"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        users = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(2, users.size());
        assertEquals("userId3", users.get(0).getId());
        assertEquals("User3", users.get(0).getName());
        assertEquals("useremail3", users.get(0).getEmail());

        assertEquals("userId4", users.get(1).getId());
        assertEquals("User4", users.get(1).getName());
        assertEquals("useremail4", users.get(1).getEmail());

        subscriber = subscribe(dataManager.getPersonnelListByGymIdAsync("gymId3"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        users = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(1, users.size());
        assertEquals("userId4", users.get(0).getId());
        assertEquals("User4", users.get(0).getName());
        assertEquals("useremail4", users.get(0).getEmail());

        subscriber = subscribe(dataManager.getPersonnelListByGymIdAsync("gymId4"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        users = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(0, users.size());
    }

    @Test
    public void getGymsByPersonnelEmailTests() {
        TestObserver<List<Gym>> subscriber =
                subscribe(dataManager.getPersonnelGymsByEmail("useremail1"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        List<Gym> gyms = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(1, gyms.size());
        assertEquals("gymId1", gyms.get(0).getId());
        assertEquals("gymName1", gyms.get(0).getName());
        assertEquals("gymAddress1", gyms.get(0).getAddress());

        subscriber = subscribe(dataManager.getPersonnelGymsByEmail("useremail2"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        gyms = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(1, gyms.size());
        assertEquals("gymId1", gyms.get(0).getId());
        assertEquals("gymName1", gyms.get(0).getName());
        assertEquals("gymAddress1", gyms.get(0).getAddress());

        subscriber = subscribe(dataManager.getPersonnelGymsByEmail("useremail3"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        gyms = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(1, gyms.size());
        assertEquals("gymId2", gyms.get(0).getId());
        assertEquals("gymName2", gyms.get(0).getName());
        assertEquals("gymAddress2", gyms.get(0).getAddress());

        subscriber = subscribe(dataManager.getPersonnelGymsByEmail("useremail4"));

        subscriber.assertNoErrors();
        subscriber.assertComplete();
        gyms = subscriber.values().get(0);
        subscriber.dispose();

        assertEquals(2, gyms.size());
        assertEquals("gymId2", gyms.get(0).getId());
        assertEquals("gymName2", gyms.get(0).getName());
        assertEquals("gymAddress2", gyms.get(0).getAddress());

        assertEquals("gymId3", gyms.get(1).getId());
        assertEquals("gymName3", gyms.get(1).getName());
        assertEquals("gymAddress3", gyms.get(1).getAddress());
    }
}
