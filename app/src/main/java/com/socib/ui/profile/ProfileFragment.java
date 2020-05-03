package com.socib.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.socib.R;
import com.socib.service.profile.ProfileService;

public class ProfileFragment extends Fragment {

    private ProfileService profileService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.help){
            openHelp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openHelp() {
        Intent intent = new Intent(getActivity(), HelpProfileActivity.class);
        startActivity(intent);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.title_profile);

        profileService = ProfileService.getInstance();
        Spinner spinnerProfile = root.findViewById(R.id.profile_spinner);
        ArrayAdapter<CharSequence> adapterProfile = ArrayAdapter.createFromResource(getActivity(),
                R.array.profile_array, android.R.layout.simple_spinner_item);
        adapterProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfile.setAdapter(adapterProfile);

        setSpinnerUnits(root.findViewById(R.id.units_spinner));

        return root;
    }

    private void setSpinnerUnits(Spinner spinnerUnits) {
        ArrayAdapter<CharSequence> adapterUnits = ArrayAdapter.createFromResource(getActivity(),
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapterUnits);
        spinnerUnits.setSelection(getPositionUnits());
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        profileService.getProfile().setUnits("ms");
                        break;
                    case 1:
                        profileService.getProfile().setUnits("km");
                        break;
                    case 2:
                        profileService.getProfile().setUnits("knots");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getPositionUnits() {
        int result = 0;
        switch (profileService.getProfile().getUnits()) {
            case "ms":
                result = 0;
                break;
            case "km":
                result = 1;
                break;
            case "knots":
                result = 2;
                break;
        }
        return result;
    }

}