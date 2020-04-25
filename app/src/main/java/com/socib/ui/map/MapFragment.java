package com.socib.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socib.R;
import com.socib.SocibApplication;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.model.VariableStation;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.service.provider.SchedulerProviderImpl;
import com.socib.ui.util.Device;
import com.socib.viewmodel.factory.FixedStationViewModelFactory;
import com.socib.viewmodel.factory.VariableStationViewModelFactory;
import com.socib.viewmodel.fixedStation.BuoyFixedStationViewModel;
import com.socib.viewmodel.fixedStation.CoastalFixedStationViewModel;
import com.socib.viewmodel.fixedStation.SeaLevelFixedStationViewModel;
import com.socib.viewmodel.fixedStation.WeatherFixedStationViewModel;
import com.socib.viewmodel.variableStation.AbstractVariableStationViewModel;
import com.socib.viewmodel.variableStation.VariableBuoyStationViewModel;
import com.socib.viewmodel.variableStation.VariableCosatalStationViewModel;
import com.socib.viewmodel.variableStation.VariableSeaLevelStationViewModel;
import com.socib.viewmodel.variableStation.VariableWeatherStationViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MapFragment extends Fragment {
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Map<String, FixedStation> mapFixedStations;

    private CoastalFixedStationViewModel coastalStationViewModel;
    private SeaLevelFixedStationViewModel seaLevelStationViewModel;
    private WeatherFixedStationViewModel weatherStationViewModel;
    private BuoyFixedStationViewModel buoyStationViewModel;
    private AbstractVariableStationViewModel variableCoastalStationViewModel;
    private AbstractVariableStationViewModel variableSeaLevelStationViewModel;
    private AbstractVariableStationViewModel variableWeatherStationViewModel;
    private AbstractVariableStationViewModel variableBuoyStationViewModel;

   /* @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coastalStationViewModel = new ViewModelProvider(
                getViewModelStore(),
                viewModelFactory).get(FixedStationViewModel.class);
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mapFixedStations = new HashMap<>();

        createViewModelInstances();

        coastalStationViewModel.fetchFixedStation();
        seaLevelStationViewModel.fetchFixedStation();
        weatherStationViewModel.fetchFixedStation();
        buoyStationViewModel.fetchFixedStation();

        mMapView.onResume(); // needed to get the map to display immediately

        MapsInitializer.initialize(requireActivity().getApplicationContext());
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true);

            double lat = Double.parseDouble(getResources().getString(R.string.map_start_latitude));
            double lng = Double.parseDouble(getResources().getString(R.string.map_start_longitude));

            // For dropping a marker at a point on the Map
            final LatLng SOCIB = new LatLng(lat, lng);

            // For zooming automatically to the location of the marker
            int startZoom = Device.isTablet(getActivity()) ? getResources().getInteger(R.integer.map_start_zoom_tablet) :
                    getResources().getInteger(R.integer.map_start_zoom);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(SOCIB).zoom(startZoom).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            coastalStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), fixedStationList -> fetchDataVariablesStation(fixedStationList, variableCoastalStationViewModel)
            );
            seaLevelStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), fixedStationList -> fetchDataVariablesStation(fixedStationList, variableSeaLevelStationViewModel)
            );
            weatherStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), fixedStationList -> fetchDataVariablesStation(fixedStationList, variableWeatherStationViewModel)
            );
            buoyStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), fixedStationList -> fetchDataVariablesStation(fixedStationList, variableBuoyStationViewModel)
            );
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.infowindow_fixed_station, null, false);
                    TextView name = view.findViewById(R.id.textName);
                    TextView type = view.findViewById(R.id.textType);
                    TextView lastUpdated = view.findViewById(R.id.textLastUpdated);

                    FixedStation fixedStation = mapFixedStations.get(marker.getTitle());
                    if (fixedStation != null) {
                        name.setText(fixedStation.getName());
                        type.setText(fixedStation.getType());
                        String lastUpdateText = getString(R.string.title_last_updated) +
                                DateUtils.formatDateTime(SocibApplication.getContext(), fixedStation.getLastUpdateDate().getTime(),
                                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
                        lastUpdated.setText(lastUpdateText);
                        LinearLayout listVariables = view.findViewById(R.id.listVariables);
                        switch (fixedStation.getIcon()) {
                            case R.drawable.ic_map_station:
                                variableCoastalStationViewModel
                                        .getVariablesStation()
                                        .observe(getViewLifecycleOwner(),
                                                response -> addVariables(fixedStation, response, listVariables));
                                break;
                            case R.drawable.ic_map_sea_level:
                                variableSeaLevelStationViewModel
                                        .getVariablesStation()
                                        .observe(getViewLifecycleOwner(),
                                                response -> addVariables(fixedStation, response, listVariables));
                                break;
                            case R.drawable.ic_map_meteo:
                                variableWeatherStationViewModel
                                        .getVariablesStation()
                                        .observe(getViewLifecycleOwner(),
                                                response -> addVariables(fixedStation, response, listVariables));
                                break;
                            case R.drawable.ic_map_buoy:
                                variableBuoyStationViewModel
                                        .getVariablesStation()
                                        .observe(getViewLifecycleOwner(),
                                                response -> addVariables(fixedStation, response, listVariables));
                                break;
                        }
                    }
                    return view;
                }


            });
        });
        return rootView;
    }

    private void createViewModelInstances() {
        FixedStationApiService fixedStationApiSerive = new FixedStationApiService(IntegrationOperationFactory
                .getAdapter()
                .create(GetApiOperation.class));
        SchedulerProvider schedulerProvider = new SchedulerProviderImpl();
        FixedStationViewModelFactory fixedStationViewModelFactory =
                new FixedStationViewModelFactory(fixedStationApiSerive,
                        schedulerProvider);
        ViewModelProvider viewModelProviderFixedStation = new ViewModelProvider(this, fixedStationViewModelFactory);
        coastalStationViewModel = viewModelProviderFixedStation.get(CoastalFixedStationViewModel.class);
        seaLevelStationViewModel = viewModelProviderFixedStation.get(SeaLevelFixedStationViewModel.class);
        weatherStationViewModel = viewModelProviderFixedStation.get(WeatherFixedStationViewModel.class);
        buoyStationViewModel = viewModelProviderFixedStation.get(BuoyFixedStationViewModel.class);

        VariableStationApiService variableStationApiService = new VariableStationApiService(IntegrationOperationFactory
                .getAdapter()
                .create(GetApiOperation.class));
        VariableStationViewModelFactory variableStationViewModelFactory =
                new VariableStationViewModelFactory(variableStationApiService, schedulerProvider);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, variableStationViewModelFactory);
        variableCoastalStationViewModel = viewModelProvider.get(VariableCosatalStationViewModel.class);
        variableSeaLevelStationViewModel = viewModelProvider.get(VariableSeaLevelStationViewModel.class);
        variableWeatherStationViewModel = viewModelProvider.get(VariableWeatherStationViewModel.class);
        variableBuoyStationViewModel = viewModelProvider.get(VariableBuoyStationViewModel.class);
    }


    private void addVariables(final FixedStation fixedStation, final Set<VariableStation> variablesStationList, LinearLayout listVariables) {
        if (variablesStationList != null) {
            Set<VariableStation> selectedVariables = variablesStationList
                    .stream()
                    .filter(variableStation -> fixedStation.getDataSourceId().contains(variableStation.getDataSourceId()))
                    .collect(Collectors.toSet());
            selectedVariables.forEach(var -> {
                View viewVariable = getLayoutInflater().inflate(R.layout.infowindow_variable, null, false);
                TextView variableName = viewVariable.findViewById(R.id.name);
                TextView variableValue = viewVariable.findViewById(R.id.value);

                variableName.setText(getVariableNameDescription(var.getName()));
                variableValue.setText(var.getValue());
                listVariables.addView(viewVariable);
            });
        }

    }

    private String getVariableNameDescription(String name){
        int idVariableName = getResources().getIdentifier(name,
                "string",
                this.requireActivity().getPackageName());
         return idVariableName==0?name:getString(idVariableName);
    }

    private void fetchDataVariablesStation(final List<FixedStation> fixedStations, final AbstractVariableStationViewModel abstractVariableStationViewModel) {
        addMarker(fixedStations);

        abstractVariableStationViewModel.fetchVariablesStation(fixedStations);
    }

    private void addMarker(final List<FixedStation> fixedStations) {
        fixedStations.forEach(
                fixedStation -> {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(fixedStation.getLatitude(), fixedStation.getLongitude()))
                            .title(fixedStation.getId())
                            .icon(BitmapDescriptorFactory.fromResource(fixedStation.getIcon()))
                    );
                    mapFixedStations.put(fixedStation.getId(), fixedStation);
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private boolean canAccessLocation() {
        return (hasPermission());
    }

    private boolean hasPermission() {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
    }
}
