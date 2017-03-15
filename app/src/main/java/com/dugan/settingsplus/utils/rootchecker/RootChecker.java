package com.dugan.settingsplus.utils.rootchecker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dugan.settingsplus.utils.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Todd on 2/14/2015.
 */
public class RootChecker {


    private static boolean canExecuteSuCommand(){

        try{
            Runtime.getRuntime().exec("su");
            Log.d("RootChecker:", "RootChecker-CanExecuteSuCommand = True");
            return true;
        }

        catch (IOException localIOException){
            Log.d("RootChecker:", "RootChecker-CanExecuteSuCommand = False");
            return false;
        }

    }

    private static boolean superuserApkExists(){

        File file = new File("/system/app/Superuser.apk");
        Boolean bolFileExists = file.exists();
        Log.d("RootChecker:", "RootChecker-superuserApkExists = " + bolFileExists);
        return bolFileExists;

    }

    private static boolean testKeyBuildExists(){

        Boolean bolExists = false;
        String buildTag = Build.TAGS;
        if ((buildTag != null) && (buildTag.contains("test-keys"))){
            bolExists = true;
        }
        Log.d("RootChecker:", "RootChecker-testKeyBuildExists = " + bolExists);
        return bolExists;

    }

    public static boolean deviceRooted(Context context){

        boolean rooted = canExecuteSuCommand() || superuserApkExists() || testKeyBuildExists();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        if(rooted != prefs.getBoolean(Constants.PREF_DEVICE_ROOTED, false)){
            editor.putBoolean(Constants.PREF_DEVICE_ROOTED, rooted);
            editor.apply();
        }

        editor.putLong(Constants.PREF_LAST_ROOT_CHECK, System.currentTimeMillis());
        editor.apply();

        return rooted;
    }

}
