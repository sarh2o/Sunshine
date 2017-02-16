package com.example.sarh2o.sunshine;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by lanshaibaobei0820 on 12/21/16.
 */

public class SingleFragmentActivity extends AppCompatActivity {
    private int fragmentId;
    private Fragment fragment;

    public SingleFragmentActivity(int fragmentId, Fragment fragment) {
        this.fragmentId = fragmentId;
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreateWithFragment(Bundle savedInstanceState, int layoutId) {
        setContentView(layoutId);
        if (isNeedLoadingFragmentMain(savedInstanceState, fragmentId)) {
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().
                    add(fragmentId, fragment).commit();
        }
    }
    private boolean isNeedLoadingFragmentMain(Bundle savedInstanceState, int fragmentId) {
        return !isResortedFromPreState(savedInstanceState)
                && isFragmentMainExisting(fragmentId);
    }

    private boolean isResortedFromPreState(Bundle savedInstanceState) {
        return savedInstanceState != null;
    }

    private boolean isFragmentMainExisting(int fragmentId) {
        return findViewById(fragmentId) != null;
    }
}
