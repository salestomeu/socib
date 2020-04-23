package com.socib.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socib.model.VariableStation;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.TestSchedulerProvider;
import com.socib.util.UtilTest;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class VariableStationViewModelTest {

    private static final String VARIABLE_STATION_FILE_NAME = "variableStation.json";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    VariableStationApiService variableStationApiService;

    @Mock
    Observer<Set<VariableStation>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    private Lifecycle lifecycle;
    private UtilTest utilTest;
    private Gson gson;
    private VariableStationViewModel variableStationviewModel;
    private Set<String> dataSources = new HashSet<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        variableStationviewModel = new VariableStationViewModel(variableStationApiService, new TestSchedulerProvider());
        variableStationviewModel.getVariablesStation().observeForever(observer);
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_result_not_null_and_has_observers() {

        when(variableStationApiService.getVariables(dataSources)).thenReturn(null);
        assertNotNull(variableStationviewModel.getVariablesStation());
        assertTrue(variableStationviewModel.getVariablesStation().hasObservers());
    }

    @Test
    public void it_should_be_api_fetch_data_success() {
        Type listType = new TypeToken<ArrayList<VariableStation>>() {
        }.getType();
        List<VariableStation> variableStations = gson.fromJson(utilTest.getMockResponse(VARIABLE_STATION_FILE_NAME), listType);

        when(variableStationApiService.getVariables(dataSources)).thenReturn(Observable.just(new HashSet<>(variableStations)));

        variableStationviewModel.fetchVariablesStation(dataSources);

        //verify(observer).onChanged(variableStations);
    }

    @Test
    public void it_should_be_api_fetch_data_error() {
        Set<VariableStation> variables = new HashSet<>();
        when(variableStationApiService.getVariables(dataSources)).thenReturn(Observable.error(new Throwable("Api error")));

        variableStationviewModel.fetchVariablesStation(dataSources);

        verify(observer).onChanged(variables);
    }

    @After
    public void tear_down() {
        variableStationApiService = null;
        variableStationviewModel = null;
    }
}
