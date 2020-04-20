package com.socib.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.integrationSocib.model.Variable;
import com.socib.model.FixedStation;
import com.socib.model.StationType;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.fixedStation.VariableStationApiService;
import com.socib.service.fixedStation.factory.FixedStationFactory;
import com.socib.service.provider.SchedulerProviderImpl;
import com.socib.ui.util.Device;
import com.socib.viewmodel.FixedStationViewModel;
import com.socib.viewmodel.FixedStationViewModelFactory;
import com.socib.viewmodel.VariableStationViewModel;
import com.socib.viewmodel.VariableStationViewModelFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MapFragment  extends Fragment {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Map<String, FixedStation> mapFixedStations;

    private FixedStationViewModel coastalStationViewModel;
    private FixedStationViewModel seaLevelStationViewModel;
    private FixedStationViewModel weatherStationViewModel;
    private VariableStationViewModel variableStationViewModel;

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
                             ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mapFixedStations = new HashMap<>();

        coastalStationViewModel = ViewModelProviders.of(this,
                new FixedStationViewModelFactory(new FixedStationApiService(IntegrationOperationFactory
                .getAdapter()
                .create(GetApiOperation.class)),
                        new SchedulerProviderImpl())).get(FixedStationViewModel.class);
        seaLevelStationViewModel = ViewModelProviders.of(this,
                new FixedStationViewModelFactory(new FixedStationApiService(IntegrationOperationFactory
                        .getAdapter()
                        .create(GetApiOperation.class)),
                        new SchedulerProviderImpl())).get(FixedStationViewModel.class);;
        weatherStationViewModel = ViewModelProviders.of(this,
                new FixedStationViewModelFactory(new FixedStationApiService(IntegrationOperationFactory
                        .getAdapter()
                        .create(GetApiOperation.class)),
                        new SchedulerProviderImpl())).get(FixedStationViewModel.class);;
        variableStationViewModel = ViewModelProviders.of(this,
                new VariableStationViewModelFactory(new VariableStationApiService(IntegrationOperationFactory
                        .getAdapter()
                        .create(GetApiOperation.class)),
                        new SchedulerProviderImpl())).get(VariableStationViewModel.class);

        coastalStationViewModel.fetchFixedStation(StationType.COASTALSTATION);
        seaLevelStationViewModel.fetchFixedStation(StationType.SEALEVEL);
        weatherStationViewModel.fetchFixedStation(StationType.WEATHERSTATION);

        mMapView.onResume(); // needed to get the map to display immediately

        MapsInitializer.initialize(requireActivity().getApplicationContext());
        if(!canAccessLocation()){
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
                    getViewLifecycleOwner(), this::addMarker
            );
            seaLevelStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), this::addMarker
            );
            weatherStationViewModel.getFixedStation().observe(
                    getViewLifecycleOwner(), this::addMarker
            );
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
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
                    Log.i("infoName:",fixedStation.getName());
                    name.setText(fixedStation.getName());
                    type.setText(fixedStation.getType());
                    lastUpdated.setText("Updated: "+fixedStation.getLastUpdateDate());
                    LinearLayout listVariables = view.findViewById(R.id.listVariables);
                    fixedStation.getDataSourceId().forEach(dataSourceId ->{
                        variableStationViewModel.fetchVariablesStation(dataSourceId);
                        variableStationViewModel
                                .getVariablesStation()
                                .observe( getViewLifecycleOwner(),
                                response -> this.addVariables(response, dataSourceId, listVariables));

                    });

                    return view;
                }

                private void addVariables(List<Variable> variablesStationList, String dataSourceId, LinearLayout listVariables) {
                    Log.i("addVariables dataSourceId:", dataSourceId);
                    if (variablesStationList != null) {
                        Log.i("variableStationList.size:",String.valueOf(variablesStationList.size()));
                        variablesStationList.forEach(var -> {
                            View viewVariable = getLayoutInflater().inflate(R.layout.infowindow_variable, null, false);
                            TextView variableName =  viewVariable.findViewById(R.id.name);
                            TextView variableValue = viewVariable.findViewById(R.id.value);
                            variableName.setText(var.getLong_name()!=null?var.getLong_name():var.getStandard_name());
                            variableValue.setText(var.getData()+" "+var.getUnits());
                            listVariables.addView(viewVariable);
                        });
                    }

                }
            });
        });


        return rootView;
    }



    private void addMarker(final List<FixedStation> fixedStations) {
        Log.i("addMarker fixedStations.size:", String.valueOf(fixedStations.size()));
        fixedStations.forEach(
                fixedStation -> {
                    googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(fixedStation.getLatitude(), fixedStation.getLongitude()))
                        .title(fixedStation.getId())
                        .icon(BitmapDescriptorFactory.fromResource(fixedStation.getIcon()))
                    );
                    mapFixedStations.put(fixedStation.getId(), fixedStation);
                });

        int icon = R.drawable.ic_map_station;
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
        return(hasPermission());
    }

    private boolean hasPermission() {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
    }
}
