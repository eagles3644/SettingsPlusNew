package com.dugan.settingsplus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dugan.settingsplus.DBRunnable;

/**
 * Created by leona on 12/12/2015.
 */
public class MyLogger {

    private MySQLHelper mDatabase;

    public MyLogger(){
        //empty constructor
    }

    public void insertLogRow(Context context, final String action, final String to, final String from){

        if(mDatabase == null) {
            mDatabase = new MySQLHelper(context);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final int logLimit = Integer.parseInt(prefs.getString(Constants.PREF_LOG_ROW_LIMIT, "100000"));

        new DBTask().execute(new UtilDBRunnable() {
            @Override
            public void executeDBTask() {
                mDatabase.insertLog(action, to, from);
                mDatabase.deleteOlderLogRows(logLimit);
            }

            @Override
            public void postExecuteDBTask() {
                mDatabase.close();
                mDatabase = null;
            }
        });

    }

}
