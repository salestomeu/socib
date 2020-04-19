package com.socib.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.socib.integrationSocib.model.Variable;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class VariableStationViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    VariableStationApiService variableStationApiService;

    @Mock
    Observer<List<Variable>> observer;

    @Mock
    LifecycleOwner lifecycleOwner;
    private Lifecycle lifecycle;

    private VariableStationViewModel varibaleStationviewModel;

    @Before
    public void  setUp(){
        MockitoAnnotations.initMocks(this);
        lifecycle = new LifecycleRegistry(lifecycleOwner);
        varibaleStationviewModel = new VariableStationViewModel(variableStationApiService, new TestSchedulerProvider());
        varibaleStationviewModel.getVariablesStation().observeForever(observer);
    }

    @Test
    public void testNull() {
        when(variableStationApiService.getVariables("aaa")).thenReturn(null);
        assertNotNull(varibaleStationviewModel.getVariablesStation());
        assertTrue(varibaleStationviewModel.getVariablesStation().hasObservers());
    }
}
