package com.example.fitnessfactory.mockHelpers.mockers;

import android.util.Log;

import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.repositories.OrganisationInfoRepository;
import com.example.fitnessfactory.mockHelpers.mockdata.OrganisationInfoDataProvider;
import com.example.fitnessfactory.utils.StringUtils;

import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.Single;

public class OrganisationInfoRepositoryMocker {

    public static OrganisationInfoRepository createMocker(OrganisationInfoRepository organisationInfoRepository) {
        OrganisationInfoDataProvider dataProvider = new OrganisationInfoDataProvider();

        Mockito.when(organisationInfoRepository.getOrganisationNameAsync())
                .thenAnswer(invocation -> {
                    return Single.just(dataProvider.getName());
                });

        Mockito.when(organisationInfoRepository.setOrganisationNameAsync(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String organisationName = invocation.getArgument(0);

                    dataProvider.setName(organisationName);

                    return Completable.complete();
                });

        Mockito.when(organisationInfoRepository.checkOrganisationNameAsync(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String organisationName = invocation.getArgument(0);

                    if (StringUtils.isEmpty(organisationName)) {
                        AppPrefs.askForOrganisationName().setValue(true);
                    } else {
                        AppPrefs.organisationName().setValue(organisationName);
                    }

                    return Completable.complete();
                });

        return organisationInfoRepository;
    }
}
