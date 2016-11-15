package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.R.attr.id;

public class DetailActivity extends ActionBarActivity {

    private final String LOG_TAG = DetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent startIntent = getIntent();
        String extras;
        extras = startIntent.getStringExtra("EXTRA");
        Log.i(LOG_TAG, "extras: " + extras);

        TextView detailStringTV = (TextView) findViewById(R.id.activityDetailTextView);
        detailStringTV.setText(extras);

        /*Bundle detailFragmentBundle = new Bundle();
        Fragment detailFragment = new DetailActivityFragment();
        detailFragment.setArguments(detailFragmentBundle);
        detailFragmentBundle.putString("EXTRA",extras);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailFragment)
                .commit();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_TAG, "onOptionsItemSelected: " + item);
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.i(LOG_TAG, "onOptionsItemSelected: inside action settings");
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
