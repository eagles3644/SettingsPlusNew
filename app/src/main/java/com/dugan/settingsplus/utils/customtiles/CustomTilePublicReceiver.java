package com.dugan.settingsplus.utils.customtiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by leona on 12/27/2015.
 */
public final class CustomTilePublicReceiver extends BroadcastReceiver {
    /**
     * The action broadcast from the Quick Settings tile when clicked
     */
    public static final String ACTION_TOAST = "com.dugan.settingsplus.CUSTOMTILE_ACTION_TOAST";

    /**
     * Constant for the String extra to be displayed in the Toast
     */
    public static final String EXTRA_MESSAGE = "com.dugan.settingsplus.CUSTOMTILE_EXTRA_MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (ACTION_TOAST.equals(action)) {
            final String message = intent.getStringExtra(EXTRA_MESSAGE);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
