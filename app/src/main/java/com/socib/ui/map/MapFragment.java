package com.socib.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socib.R;
import com.socib.ui.util.Device;

import java.util.Objects;

public class MapFragment  extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                double lat = Double.parseDouble(getResources().getString(R.string.map_start_latitude));
                double lng = Double.parseDouble(getResources().getString(R.string.map_start_longitude));

                // For dropping a marker at a point on the Map
                final LatLng SOCIB = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions()
                        .position(SOCIB)
                        .title("Socib Station")
                        .snippet("Socib Station in Parc bit")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_meteo))
                        );

                // For zooming automatically to the location of the marker
                int startZoom = Device.isTablet(getActivity()) ? getResources().getInteger(R.integer.map_start_zoom_tablet) :
                        getResources().getInteger(R.integer.map_start_zoom);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(SOCIB).zoom(startZoom).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
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
}
