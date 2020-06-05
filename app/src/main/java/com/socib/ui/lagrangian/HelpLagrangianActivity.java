package com.socib.ui.lagrangian;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.socib.R;

public class HelpLagrangianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_lagrangian);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener(v -> finish());

        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.help_lagrangian_section);
    }
}
