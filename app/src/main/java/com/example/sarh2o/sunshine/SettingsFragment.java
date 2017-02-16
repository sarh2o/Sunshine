package com.example.sarh2o.sunshine;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by lanshaibaobei0820 on 2/14/17.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_setting);
    }
}
