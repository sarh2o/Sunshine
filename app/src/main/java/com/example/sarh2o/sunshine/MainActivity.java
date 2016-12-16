package com.example.sarh2o.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isNeedLoadingFragmentMain(savedInstanceState)) {
            ForecastFragment forecastFragment = new ForecastFragment();
            forecastFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_main_view, forecastFragment).commit();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.);
    }*/

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
