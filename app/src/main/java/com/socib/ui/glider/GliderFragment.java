package com.socib.ui.glider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.socib.R;
import com.socib.ui.notifications.NotificationsViewModel;

public class GliderFragment extends Fragment {

    private GliderViewModel gliderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gliderViewModel =
                ViewModelProviders.of(this).get(GliderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_glider, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.title_glider);

        final TextView textView = root.findViewById(R.id.text_glider);
        gliderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}