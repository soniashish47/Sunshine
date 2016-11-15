package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.R.attr.id;

/**
 * Created by ab219tx on 15-10-2016.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.refreshmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView  = inflater.inflate(R.layout.detail_fragment,container,false);
        String getArgument = getArguments().getString("EXTRA");
        TextView weatherDetails = (TextView) rootView.findViewById(R.id.detailTextView);
        weatherDetails.setText(getArgument);

        return rootView;

    }
}
