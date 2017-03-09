package com.example.sarh2o.sunshine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends SingleFragmentActivity {

    public DetailActivity() {
        super(R.id.activity_detail, new PlaceholderFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateWithFragment(savedInstanceState, R.layout.activity_detail);
    }

    public static class PlaceholderFragment extends Fragment {
        private TextView textView;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            textView = (TextView)rootView.findViewById(R.id.forecast_detail_text);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                textView.setText(intent.getExtras().getString(Intent.EXTRA_TEXT));
            }
            return rootView;
        }

        @Override

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.settingsfragment, menu);
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_detail_setting:
                    Intent settingsActivityIntent =
                            new Intent(getActivity(), SettingsActivity.class);
                    startActivity(settingsActivityIntent);
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
