package com.socib.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.TestSchedulerProvider;
import com.socib.util.UtilTest;
import com.socib.viewmodel.fixedStation.AbstractFixedStationViewModel;
import com.socib.viewmodel.fixedStation.CoastalFixedStationViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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

public class CoastalFixedStationViewModelTest {
    private static final String FIXED_STATION_FILE_NAME = "fixedStationList.json";
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FixedStationApiService fixedStationApiService;

    @Mock
    Observer<List<FixedStation>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    private Lifecycle lifecycle;
    private UtilTest utilTest;
    private Gson gson;
    private AbstractFixedStationViewModel fixedStationviewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        fixedStationviewModel = new CoastalFixedStationViewModel(fixedStationApiService, new TestSchedulerProvider());
        fixedStationviewModel.getFixedStation().observeForever(observer);
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_result_not_null_and_has_observers() {
        when(fixedStationApiService.getFixedStations(StationType.COASTALSTATION)).thenReturn(null);
        assertNotNull(fixedStationviewModel.getFixedStation());
        assertTrue(fixedStationviewModel.getFixedStation().hasObservers());
    }

    @Test
    public void it_should_be_api_fetch_data_success() {

        Type listType = new TypeToken<ArrayList<FixedStation>>() {
        }.getType();
        List<FixedStation> fixedStations = gson.fromJson(utilTest.getMockResponse(FIXED_STATION_FILE_NAME), listType);

        when(fixedStationApiService.getFixedStations(StationType.COASTALSTATION)).thenReturn(Observable.just(fixedStations));

        fixedStationviewModel.fetchFixedStation();

        verify(observer).onChanged(fixedStations);
    }

    @Test
    public void it_should_be_api_fetch_data_error() {
        List<FixedStation> fixedStations = new ArrayList<>();
        when(fixedStationApiService.getFixedStations(StationType.COASTALSTATION)).thenReturn(Observable.error(new Throwable("Api error")));

        fixedStationviewModel.fetchFixedStation();

        verify(observer).onChanged(fixedStations);
    }

    @After
    public void tear_down() {
        fixedStationApiService = null;
        fixedStationviewModel = null;
    }
}
