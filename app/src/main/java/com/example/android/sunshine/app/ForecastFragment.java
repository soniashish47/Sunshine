package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * Created by ab219tx on 08-10-2016.
 */

public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter = null;
    protected String listStr = null;
    URL mUrl;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri.Builder mUri = new Uri.Builder();
        Uri.Builder builder = mUri.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5/forecast/daily")
                .appendQueryParameter("q", "500084,in")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("cnt", "7")
                .appendQueryParameter("appid", "c33240070fcc87147567759eadf6b689");

        try {
            mUrl = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
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

/*        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=500084,in&units=metric&cnt=7&appid=c33240070fcc87147567759eadf6b689";
        new PerformNetwork().execute(url);*/

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(array_forecast));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);
        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, list);
        lv.setAdapter(mForecastAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.refreshmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

           // String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=500084,in&units=metric&cnt=7&appid=c33240070fcc87147567759eadf6b689";
            new PerformNetwork().execute(mUrl.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class PerformNetwork extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            //  String jsonStr = null;

            try {
                if (params != null)
                    url = new URL(params[0]);
                Log.i("sunshine", "url is " + url.toString());
                if (url != null)
                    urlConnection = (HttpURLConnection) url.openConnection();

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
                    listStr = stringBuffer.toString();
                    Log.i("sunshine", "listStr is " + listStr);
                } else {
                    Log.e("sunshine", "stringBuffer is null : ");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
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
    }

}