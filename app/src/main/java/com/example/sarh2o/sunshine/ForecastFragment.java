package com.example.sarh2o.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView = null;
    private ArrayAdapter<String> forecastViewAdapter = null;
    private ListView listView = null;
    private List<String> forecastItems = null;


    public ForecastFragment() {
        // Required empty public constructor
        int x = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.forecastfragment, container, false);
        initListView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateWeather();
                break;
            case R.id.action_setting:
                openSettings();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    private void openSettings() {
        Intent settingActivityIntent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(settingActivityIntent);
    }

    private void updateWeather() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String postcode = preferences.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        String unit = preferences.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_default));
        new FetchWeatherTask().execute(postcode, unit);
    }

    private void initListView() {
        listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastViewAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textView, new ArrayList<String>());
        if (listView != null) {
            listView.setAdapter(forecastViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position,
                                        long id) {
                    // Toast Example
                    String text = forecastViewAdapter.getItem(position);
//                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    // Try to use intent
                    Intent detailActivityIntent = new Intent(getActivity(),
                            DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(detailActivityIntent);
                }
            });
        }
    }

    private class WeatherParameters {
        private String unit = "metric";
        private String appKey = "466c5bdb77e1ef542d1e83bdce7a2064";
        private int days = 7;
        private String format = "json";
        private String postcode;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        private final String JSON_KEY_LIST = "list";
        private final String JSON_KEY_DATETIME = "dt";
        private final String JSON_KEY_TEMP = "temp";
        private final String JSON_KEY_WEATHER = "weather";
        private final String JSON_KEY_MAX = "max";
        private final String JSON_KEY_MIN = "min";
        private final String JSON_KEY_MAIN = "main";
        private WeatherParameters weatherParameters;

        public FetchWeatherTask() {
            weatherParameters = new WeatherParameters();
        }

        @Override
        protected String[] doInBackground (String... params) {
            weatherParameters.setPostcode(params[0]);
            weatherParameters.setUnit(params[1]);
            return parseForecastJsonData(getForecastJasonData());
        }

        private String getForecastJasonData() {
            HttpGetter httpGetter = new HttpGetter(weatherParameters);
            String forecastJsonData = httpGetter.getData();
            if (forecastJsonData == null) return null;
            return forecastJsonData;
        }

        @Override
        protected void onPostExecute(String []result) {
            if (result.length == 0) {
                Log.w(LOG_TAG, "Didn't get any weather data!");
                return;
            }
            forecastItems = new ArrayList<String>(Arrays.<String>asList(result));
            forecastViewAdapter.clear();
            forecastViewAdapter.addAll(forecastItems);
        }

        String[] parseForecastJsonData(String forecastJsonStr) {
            if (forecastJsonStr.length() == 0) {
                return null;
            }
            String[] forecastOfDays = new String[weatherParameters.getDays()];
            try {
                JSONObject jsonObjects = new JSONObject(forecastJsonStr);
                JSONArray weatherData = jsonObjects.getJSONArray(JSON_KEY_LIST);
                SimpleDateFormat dtFormater = new SimpleDateFormat("EEE, MMM dd");
                for (int i = 0; i < weatherData.length(); i++) {
                    JSONObject weatherDataPerDay = weatherData.getJSONObject(i);
                    JSONObject tempDataPerDay = weatherDataPerDay.getJSONObject(JSON_KEY_TEMP);
                    long dt = weatherDataPerDay.getLong(JSON_KEY_DATETIME) * 1000;
                    String result = dtFormater.format(dt);
                    long maxTemp = calcTempWithUnit(tempDataPerDay.getDouble(JSON_KEY_MAX));
                    long minTemp = calcTempWithUnit(tempDataPerDay.getDouble(JSON_KEY_MIN));
                    JSONArray weatherList = weatherDataPerDay.getJSONArray(JSON_KEY_WEATHER);
                    String weatherDescription = "";
                    if (weatherList.length() > 0) {
                        weatherDescription = weatherList.getJSONObject(0).getString(JSON_KEY_MAIN);
                    }
                    forecastOfDays[i] = result + " - " + weatherDescription
                            + " - " + maxTemp + "/" + minTemp;
                    Log.v(LOG_TAG, forecastOfDays[i] + "dt = " + dt);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Weather JSON data parsing failure", e);
            }
            return forecastOfDays;
        }

        long calcTempWithUnit(double temp) {
            if (weatherParameters.getUnit().equals(getString(R.string.pref_units_imperial))) {
                temp = temp * 1.8 + 32;
            }
            return Math.round(temp);
        }

        private class HttpGetter {
            private WeatherParameters weatherParameters;

            private final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            private final String POSTCODE_PARAM = "q";
            private final String UNIT_PARAM = "units";
            private final String APP_KEY_PARAM = "APPID";
            private final String DAYS_PARAM = "cnt";
            private final String FORMAT_PARAM = "mode";
            private final String UNIT_FOR_QUERY = "metric";

            public HttpGetter(WeatherParameters wp) {
                this.weatherParameters = wp;
            }

            public String getData() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String data = "";

                try {
                    URL url = buildUrl();
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream returnedStream = urlConnection.getInputStream();
                    if (returnedStream == null) {
                        return "";
                    }
                    reader = new BufferedReader(new InputStreamReader(returnedStream));
                    StringBuffer buffer = new StringBuffer();
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        buffer.append(line);
                    }
                    data = buffer.toString();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Get forecast json data error!", e);
                    return data;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Close InputStream error!", e);
                        }
                    }
                }
                return data;
            }

            @NonNull
            private URL buildUrl() throws MalformedURLException {
                Uri uriBuilder = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(POSTCODE_PARAM, weatherParameters.getPostcode())
                        .appendQueryParameter(UNIT_PARAM, UNIT_FOR_QUERY)
                        .appendQueryParameter(APP_KEY_PARAM, weatherParameters.getAppKey())
                        .appendQueryParameter(DAYS_PARAM,
                                String.valueOf(weatherParameters.getDays()))
                        .appendQueryParameter(FORMAT_PARAM, weatherParameters.getFormat()).build();
                return new URL(uriBuilder.toString());
            }
        }
    }
}