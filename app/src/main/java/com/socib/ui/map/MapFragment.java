package com.socib.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socib.R;
import com.socib.model.FixedStation;
import com.socib.ui.util.Device;
import com.socib.viewmodel.CoastalStationViewModel;
import com.socib.viewmodel.SeaLevelStationViewModel;
import com.socib.viewmodel.WeatherStationViewModel;

import java.util.List;
import java.util.Objects;

public class MapFragment  extends Fragment {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private MapView mMapView;
    private GoogleMap googleMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        CoastalStationViewModel coastalStationViewModel = ViewModelProviders.of(this).get(CoastalStationViewModel.class);
        SeaLevelStationViewModel seaLevelStationViewModel = ViewModelProviders.of(this).get(SeaLevelStationViewModel.class);
        WeatherStationViewModel weatherStationViewModel = ViewModelProviders.of(this).get(WeatherStationViewModel.class);
        mMapView.onResume(); // needed to get the map to display immediately

        MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
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
        });





        return rootView;
    }

    private void addMarker(final List<FixedStation> fixedStations) {
        Log.i("addMarker fixedStations.size:",String.valueOf(fixedStations.size()));
        fixedStations.forEach(
                fixedStation -> googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(fixedStation.getLatitude(), fixedStation.getLongitude()))
                        .title(fixedStation.getName())
                        .snippet(fixedStation.getType())
                        .snippet("Updated: "+fixedStation.getLastUpdateDate().toString())
                        .icon(BitmapDescriptorFactory.fromResource(fixedStation.getIcon()))
        ));

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
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION));
    }
}
