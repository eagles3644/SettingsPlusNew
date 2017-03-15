package com.dugan.settingsplus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dugan.settingsplus.utils.customtiles.CustomTileHelper;

/**
 * Created by leona on 12/27/2015.
 */
public final class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            final CustomTileHelper helper = new CustomTileHelper(context);

            if (helper.isLastTileStateShown()) {
                //helper.showTile();
                //TODO:BOOT COMPLETE
            }
        }
    }
}