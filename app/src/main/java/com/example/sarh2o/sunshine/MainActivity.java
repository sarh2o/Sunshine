package com.example.sarh2o.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isNeedLoadingFragmentMain(savedInstanceState)) {
            PlaceholderFragment placeholderFragment = new PlaceholderFragment();
            placeholderFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_main_view, placeholderFragment).commit();
        }
    }

    private boolean isNeedLoadingFragmentMain(Bundle savedInstanceState) {
        return !isResortedFromPreState(savedInstanceState)
                && isFragmentMainExisting();
    }

    private boolean isResortedFromPreState(Bundle savedInstanceState) {
        return savedInstanceState != null;
    }

    private boolean isFragmentMainExisting() {
        return findViewById(R.id.fragment_main_view) != null;
    }
}
