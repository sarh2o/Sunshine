package com.example.sarh2o.sunshine;


import android.app.Fragment;
import android.os.Bundle;

public class SettingsActivity extends SingleFragmentActivity {

    public SettingsActivity() {
        super(R.id.fragment_settings_view, (Fragment)(new SettingsFragment()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateWithFragment(savedInstanceState, R.layout.activity_settings);
    }
}
