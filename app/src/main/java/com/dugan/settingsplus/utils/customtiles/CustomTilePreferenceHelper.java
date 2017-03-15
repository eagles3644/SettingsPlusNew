package com.dugan.settingsplus.utils.customtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by leona on 12/27/2015.
 */
final class CustomTilePreferenceHelper {

    private static final String PREFS_NAME = "tile_prefs";

    private final SharedPreferences mSharedPreferences;

    CustomTilePreferenceHelper(@NonNull Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    void setBoolean(@NonNull String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    boolean getBoolean(@NonNull String key) {
        return mSharedPreferences.getBoolean(key, false);
    }
}
