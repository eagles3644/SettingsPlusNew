package com.dugan.settingsplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.dugan.settingsplus.utils.Constants;
import com.dugan.settingsplus.utils.MySQLHelper;

/**
 * Created by leona on 12/5/2015.
 */
public class SettingsFragment extends MyPreferenceFragment {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

    SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(Constants.PREF_LOG_ROW_LIMIT)){
                final int logLimit = Integer.parseInt(prefs.getString(Constants.PREF_LOG_ROW_LIMIT, "10000"));
                new DBTask().execute(new DBRunnable() {
                    @Override
                    public void executeDBTask() {
                        MySQLHelper db = new MySQLHelper(getActivity().getApplication().getApplicationContext());
                        db.deleteOlderLogRows(logLimit);
                    }

                    @Override
                    public void postExecuteDBTask() {

                    }
                });
            } else //noinspection StatementWithEmptyBody
                if(key.equals(Constants.PREF_LOG_PROF)){
                //Nothing to do
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        prefs.unregisterOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

}
