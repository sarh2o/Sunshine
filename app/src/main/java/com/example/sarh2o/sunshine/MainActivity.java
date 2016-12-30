package com.example.sarh2o.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends SingleFragmentActivity {

    public MainActivity() {
        super(R.id.fragment_main_view, new ForecastFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateWithFragment(savedInstanceState, R.layout.activity_main);
    }
}
