package com.dugan.settingsplus.utils.customtiles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.dugan.settingsplus.R;

import java.io.FileNotFoundException;

/**
 * Created by leona on 12/27/2015.
 */
public final class CustomTileHelper {
    /**
     * This is the identifier of the custom Broadcast Tile. Whatever action you configured the tile
     * for must be used when configuring the tile. For Broadcast tiles, only alphanumeric characters
     * (and periods) are allowed. Keep in mind that this excludes underscores.
     */
    private static final String BROADCAST_TILE_IDENTIFIER = "com.dugan.settingsplus.CUSTOMTILE";

    /**
     * Keeps track of the last known state of the Quick Settings custom tile. There doesn't seem to
     * be a way to query the state of the tile.
     */
    private static final String PREF_TILE_SHOWN = "com.dugan.settingsplus.CUSTOMTILE_SHOWN";

    private final Context mContext;
    private final CustomTilePreferenceHelper mCustomTilePreferenceHelper;

    public CustomTileHelper(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mCustomTilePreferenceHelper = new CustomTilePreferenceHelper(mContext);
    }

    public void showTile(String labelText, int iconID, String iconPackage) {

        mCustomTilePreferenceHelper.setBoolean(PREF_TILE_SHOWN, true);

        // Set up an Intent that will be broadcast by the system, and received by the exported
        // PublicBroadcastReceiver.
        final Intent toastIntent = new Intent(CustomTilePublicReceiver.ACTION_TOAST);
        toastIntent.putExtra(CustomTilePublicReceiver.EXTRA_MESSAGE, "Hello!");

        // Set up a PendingIntent that will be delivered back to the application on a long-click
        // of the custom Broadcast Tile.
        final Intent longClickBroadcast = new Intent(CustomTilePrivateReceiver.ACTION_NOTIFICATION);
        longClickBroadcast.putExtra(CustomTilePrivateReceiver.EXTRA_NOTIFICATION_ID, 2);
        longClickBroadcast.putExtra(CustomTilePrivateReceiver.EXTRA_NOTIFICATION_TITLE, "Test Custom Tile");
        longClickBroadcast.putExtra(CustomTilePrivateReceiver.EXTRA_NOTIFICATION_BODY, "Test long clock pending intent.");

        final PendingIntent onLongClickPendingIntent = PendingIntent.getBroadcast(
                mContext, 0, longClickBroadcast, PendingIntent.FLAG_CANCEL_CURRENT);

        // Send the update event to the Broadcast Tile. Custom tiles are hidden by default until
        // enabled with this broadcast Intent.
        mContext.sendBroadcast(new CustomTileIntentBuilder(mContext, BROADCAST_TILE_IDENTIFIER)
                .setVisible(true)
                .setLabel(labelText)
                .setIconPackage(iconPackage)
                .setIconResource(iconID)
                .setOnClickBroadcast(toastIntent)
                .setOnLongClickPendingIntent(onLongClickPendingIntent)
                .build());
    }

    public void hideTile() {
        mCustomTilePreferenceHelper.setBoolean(PREF_TILE_SHOWN, false);

        mContext.sendBroadcast(new CustomTileIntentBuilder(mContext, BROADCAST_TILE_IDENTIFIER)
                .setVisible(false)
                .build());
    }

    public boolean isLastTileStateShown() {
        return mCustomTilePreferenceHelper.getBoolean(PREF_TILE_SHOWN);
    }
}