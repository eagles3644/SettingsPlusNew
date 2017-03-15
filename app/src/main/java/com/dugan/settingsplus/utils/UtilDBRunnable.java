package com.dugan.settingsplus.utils;

import android.os.AsyncTask;

/**
 * Created by leona on 12/12/2015.
 */
public interface UtilDBRunnable {
    void executeDBTask();
    void postExecuteDBTask();
}

class DBTask extends AsyncTask<UtilDBRunnable, Void, UtilDBRunnable> {
    @Override
    protected UtilDBRunnable doInBackground(UtilDBRunnable... runnables){
        runnables[0].executeDBTask();
        return runnables[0];
    }

    @Override
    protected void onPostExecute(UtilDBRunnable runnable){
        runnable.postExecuteDBTask();
    }
}