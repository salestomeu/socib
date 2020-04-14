package com.socib.ui.research;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.socib.R;

public class ResearchFragment extends Fragment {

    private ResearchViewModel researchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        researchViewModel =
                ViewModelProviders.of(this).get(ResearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_research, container, false);
        final TextView textView = root.findViewById(R.id.text_research);
        researchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}