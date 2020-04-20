package com.socib.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socib.integrationSocib.model.GetDataResponse;
import com.socib.integrationSocib.model.Variable;
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
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class VariableStationViewModelTest {

    private static final String DATA_FILE_NAME = "data.json";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    VariableStationApiService variableStationApiService;

    @Mock
    Observer<List<Variable>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    private Lifecycle lifecycle;
    private UtilTest utilTest;
    private Gson gson;
    private VariableStationViewModel varibaleStationviewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        varibaleStationviewModel = new VariableStationViewModel(variableStationApiService, new TestSchedulerProvider());
        varibaleStationviewModel.getVariablesStation().observeForever(observer);
        utilTest = new UtilTest();
        gson = new Gson();
    }

    @Test
    public void it_should_be_result_not_null_and_has_observers() {
        when(variableStationApiService.getVariables("aaa")).thenReturn(null);
        assertNotNull(varibaleStationviewModel.getVariablesStation());
        assertTrue(varibaleStationviewModel.getVariablesStation().hasObservers());
    }

    @Test
    public void it_should_be_api_fetch_data_success() {
        Type listType = new TypeToken<ArrayList<GetDataResponse>>() {
        }.getType();
        List<GetDataResponse> dataResponse = gson.fromJson(utilTest.getMockResponse(DATA_FILE_NAME), listType);

        when(variableStationApiService.getVariables("aaa")).thenReturn(Observable.just(dataResponse.get(0).getVariables()));

        varibaleStationviewModel.fetchVariablesStation("aaa");

        verify(observer).onChanged(dataResponse.get(0).getVariables());
    }

    @Test
    public void it_should_be_api_fetch_data_error() {
        List<Variable> variables = new ArrayList<>();
        when(variableStationApiService.getVariables("aaa")).thenReturn(Observable.error(new Throwable("Api error")));

        varibaleStationviewModel.fetchVariablesStation("aaa");

        verify(observer).onChanged(variables);
    }

    @After
    public void tear_down() {
        variableStationApiService = null;
        varibaleStationviewModel = null;
    }
}
