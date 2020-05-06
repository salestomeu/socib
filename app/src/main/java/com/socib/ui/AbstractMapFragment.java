package com.socib.ui;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.socib.R;
import com.socib.ui.util.Device;

public abstract class AbstractMapFragment extends Fragment {
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    protected MapView mMapView;
    private GoogleMap googleMap;

    protected abstract void paintMarkers(GoogleMap googleMap);

    public void checkPermissions() {
        MapsInitializer.initialize(requireActivity().getApplicationContext());
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
    }

    public void paintMap() {
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

            paintMarkers(googleMap);

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
