package com.socib.ui.glider;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socib.R;
import com.socib.SocibApplication;
import com.socib.integrationSocib.GetApiOperation;
import com.socib.integrationSocib.IntegrationOperationFactory;
import com.socib.model.FixedStation;
import com.socib.service.fixedStation.FixedStationApiService;
import com.socib.service.provider.SchedulerProvider;
import com.socib.service.provider.SchedulerProviderImpl;
import com.socib.ui.AbstractMapFragment;
import com.socib.viewmodel.GliderMobileStationViewModel;
import com.socib.viewmodel.factory.FixedStationViewModelFactory;

import java.util.Date;
import java.util.List;

public class GliderFragment extends AbstractMapFragment {

    private GliderMobileStationViewModel gliderMobileStationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_glider, container, false);
        mMapView = rootView.findViewById(R.id.mapView);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.title_glider);

        mMapView.onCreate(savedInstanceState);
        createViewModelInstances();
        gliderMobileStationViewModel.fetchFixedStation();
        mMapView.onResume(); // needed to get the map to display immediately
        super.checkPermissions();
        super.paintMap();
        return rootView;
    }

    private void createViewModelInstances() {
        FixedStationApiService fixedStationApiService = new FixedStationApiService(IntegrationOperationFactory
                .getAdapter()
                .create(GetApiOperation.class), getString(R.string.api_key));
        SchedulerProvider schedulerProvider = new SchedulerProviderImpl();
        FixedStationViewModelFactory fixedStationViewModelFactory =
                new FixedStationViewModelFactory(fixedStationApiService,
                        schedulerProvider);
        ViewModelProvider viewModelProviderFixedStation = new ViewModelProvider(this, fixedStationViewModelFactory);
        gliderMobileStationViewModel = viewModelProviderFixedStation.get(GliderMobileStationViewModel.class);
    }

    @Override
    protected void paintMarkers(GoogleMap googleMap) {
        Log.i("Gliders: ","paintMarkers");
        gliderMobileStationViewModel.getFixedStation()
                .observe(getViewLifecycleOwner(), fixedStations ->this.addMarker(fixedStations, googleMap));
    }

    private void addMarker(final List<FixedStation> fixedStations, GoogleMap googleMap) {
        Log.i("Gliders: ", String.valueOf(fixedStations.size()));
        fixedStations.forEach(
                fixedStation -> googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(fixedStation.getLatitude(), fixedStation.getLongitude()))
                        .title(fixedStation.getId())
                        .snippet(getLastUpdateText(fixedStation.getLastUpdateDate()))
                        .icon(BitmapDescriptorFactory.fromResource(fixedStation.getIcon()))
                ));
    }

    private String getLastUpdateText(Date lastUpdateDate){
        return getString(R.string.title_last_updated) +
                DateUtils.formatDateTime(SocibApplication.getContext(), lastUpdateDate.getTime(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
    }

}