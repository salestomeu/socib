package com.socib.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socib.model.FixedStation;
import com.socib.model.MobileStation;
import com.socib.model.StationType;
import com.socib.service.mobileStation.MobileStationApiService;
import com.socib.service.provider.TestSchedulerProvider;
import com.socib.util.UtilTest;
import com.socib.viewmodel.mobileStation.AbstractMobileStationViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class GliderMobileStationViewModelTest {
    private static final String MOBILE_STATION_FILE_NAME = "mobileStationList.json";
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    MobileStationApiService mobileStationApiService;

    @Mock
    Observer<List<MobileStation>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    private Lifecycle lifecycle;
    private UtilTest utilTest;
    private Gson gson;
    private AbstractMobileStationViewModel mobileStationViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        mobileStationViewModel = new GliderMobileStationViewModel(mobileStationApiService, new TestSchedulerProvider());
        mobileStationViewModel.getMobileStation().observeForever(observer);
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_result_not_null_and_has_observers() {
        when(mobileStationApiService.getMobileStations(StationType.GLIDER)).thenReturn(null);
        assertNotNull(mobileStationViewModel.getMobileStation());
        assertTrue(mobileStationViewModel.getMobileStation().hasObservers());
    }

    @Test
    public void it_should_be_api_fetch_data_success() {

        Type listType = new TypeToken<ArrayList<MobileStation>>() {
        }.getType();
        List<MobileStation> mobileStations = gson.fromJson(utilTest.getMockResponse(MOBILE_STATION_FILE_NAME), listType);

        when(mobileStationApiService.getMobileStations(StationType.GLIDER)).thenReturn(Observable.just(mobileStations));

        mobileStationViewModel.fetchMobileStation();

        verify(observer).onChanged(mobileStations);
    }

    @Test
    public void it_should_be_api_fetch_data_error() {
        List<MobileStation> mobileStations = new ArrayList<>();
        when(mobileStationApiService.getMobileStations(StationType.GLIDER)).thenReturn(Observable.error(new Throwable("Api error")));

        mobileStationViewModel.fetchMobileStation();

        verify(observer).onChanged(mobileStations);
    }


    @After
    public void tear_down() {
        mobileStationApiService = null;
        mobileStationViewModel = null;
    }

}
