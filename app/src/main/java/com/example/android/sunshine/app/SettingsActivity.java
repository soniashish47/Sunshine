package com.example.android.sunshine.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.preference.Preference.OnPreferenceChangeListener;

import static android.R.attr.value;
import static com.example.android.sunshine.app.R.xml.preference;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

       // getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return true;
    }


    public static class SettingsFragment extends PreferenceFragment {

        private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
        private EditTextPreference editTextPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(preference);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    OnPreferenceChangeListener listener = ( OnPreferenceChangeListener) getActivity();
                    listener.onPreferenceChange(preference, newValue);
                    return true;
                }
            };

            SettingsFragment sf = new SettingsFragment();
            EditTextPreference editTextPreference = (EditTextPreference) SettingsFragment.this.findPreference(getString(R.string.perf_pincode_key));
            if(editTextPreference != null) {
                editTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
            } else {
                Log.i(LOG_TAG,"edit PRef is null");
            }
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        /* private void bindPreferenceSummaryToValue(Preference preference) {
            if(preference != null) {
                preference.setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener) this);

                ((Preference.OnPreferenceChangeListener) getActivity()).onPreferenceChange(preference, PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
            }
            else {
                Log.w(LOG_TAG,"preference is null");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            SettingsFragment settingsFragment = new SettingsFragment();
            Preference pincodePref = getPreferenceManager().findPreference(getString(R.string.pref_pincode_key));
                    //settingsFragment.findPreference(getString(R.string.pref_pincode_key));
            if(pincodePref == null) {
                Log.w(LOG_TAG,"pincodePref is null");
            } else {
                pincodePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        return true;
                    }
                });
            }

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_pincode_key)));
            return super.onCreateView(inflater, container, savedInstanceState);
        }



        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) sharedPreferences;
                int prefIndex = listPreference.findIndexOfValue(key);
                if (prefIndex >= 0) {
                    ((ListPreference) sharedPreferences).setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                editor.putString(getString(R.string.pref_pincode_key),key);
            }
        }*/
    }
}
