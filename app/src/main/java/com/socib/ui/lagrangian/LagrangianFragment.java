package com.socib.ui.lagrangian;

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
import com.socib.ui.notifications.NotificationsViewModel;

public class LagrangianFragment extends Fragment {

    private LagrangianViewModel lagrangianViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lagrangianViewModel =
                ViewModelProviders.of(this).get(LagrangianViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lagrangian, container, false);
        final TextView textView = root.findViewById(R.id.text_lagrangian);
        lagrangianViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}