package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.Inflater;

//import static com.example.android.sunshine.app.ForecastFragment.PerformNetwork.LOG_TAG;

/**
 * Created by ab219tx on 08-10-2016.
 */

public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter = null;
    protected String listStr = null;
    URL mUrl;
    private static final String TAG = "ForecastFragment";
    final String URI_SCHEME = "http";
    final String URI_AUTH = "api.openweathermap.org";
    final String URI_PATH = "data/2.5/forecast/daily";
    final String URI_PINCODE = "q";
    final String URI_UNITS = "units";
    final String URI_DAYS = "cnt";
    final String URI_APPID = "appid";
    Uri.Builder mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] array_forecast = {
                "Today - rainny - 23/25",
                "Tomorrow - rainny - 24/25",
                "Mon - rainny - 23/26",
                "Tue - rainny - 23/27",
                "wed - rainny - 23/28",
        };

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(array_forecast));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView weatherList = (ListView) rootView.findViewById(R.id.listview_forecast);
        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, list);
        weatherList.setAdapter(mForecastAdapter);
        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item Selected at postn : " + position, Toast.LENGTH_SHORT).show();
                Intent detailActivityIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("EXTRA", weatherList.getItemAtPosition(position).toString());

                startActivity(detailActivityIntent);
            }
        });



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.refreshmenu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: " + item);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            int daysNumber = 10;

            new PerformNetwork().execute();
            // String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=500084,in&units=metric&cnt=7&appid=c33240070fcc87147567759eadf6b689";

            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }


        return super.onOptionsItemSelected(item);
    }


    class PerformNetwork extends AsyncTask<Void, Void, String[]> {

        private static final String LOG_TAG = "performNetwork";

        /* The date/time conversion code is going to be moved outside the asynctask later,
        * so for convenience we're breaking it out into its own method now.
        */


        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String jsonStr = null;
            String[] weatherData = null;

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location = preferences.getString(getString(R.string.perf_pincode_key),
                    getString(R.string.pref_pincode_default));

            Map<String,?> keys = preferences.getAll();
            for(Map.Entry<String,?> entry : keys.entrySet()){
                Log.i("map values",entry.getKey() + ": " +
                        entry.getValue().toString());
              //  if(entry.getKey().toString().equals("perf_pincode_key")) {
                    location = entry.getValue().toString();
              //  }
            }

            Log.i(TAG,"pref location3 is " + location);

            mUri = new Uri.Builder();
            Uri.Builder builder = mUri.scheme(URI_SCHEME)
                    .authority(URI_AUTH)
                    .appendPath(URI_PATH)
                    .appendQueryParameter(URI_PINCODE, location)
                    .appendQueryParameter(URI_UNITS, "metric")
                    .appendQueryParameter(URI_DAYS, "7")
                    .appendQueryParameter(URI_APPID, "c33240070fcc87147567759eadf6b689");

            try {
                mUrl = new URL(builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {

                if (mUrl != null)
                    urlConnection = (HttpURLConnection) mUrl.openConnection();

                try {
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("sunshine", "line is " + line);
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer != null) {
                    jsonStr = stringBuffer.toString();
                    Log.i("LOG_TAG", "listStr is " + jsonStr);
                } else {
                    Log.e("LOG_TAG", "stringBuffer is null : ");
                }

                SunshineJsonParser sunshineJsonParser = new SunshineJsonParser(jsonStr);
                return sunshineJsonParser.getWeatherDataFromJson(7);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (bufferedReader != null)
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            if (result != null) {
                mForecastAdapter.clear();
                for (String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }
            }

        }
    }

}