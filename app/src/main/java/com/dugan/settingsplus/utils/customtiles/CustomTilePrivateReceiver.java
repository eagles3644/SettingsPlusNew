package com.dugan.settingsplus.utils.customtiles;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.dugan.settingsplus.R;

/**
 * Created by leona on 12/27/2015.
 */
public final class CustomTilePrivateReceiver extends BroadcastReceiver {
    /**
     * Action constant defining that a push notification should be displayed
     */
    public static final String ACTION_NOTIFICATION = "com.dugan.settingsplus.CUSTOMTILE_ACTION_NOTIFICATION";

    /**
     * Key for the integer extra value to be used for the notification ID
     */
    public static final String EXTRA_NOTIFICATION_ID = "com.dugan.settingsplus.CUSTOMTILE_NOTIFICATION_ID";

    /**
     * Key for the string extra to be displayed in the notification title
     */
    public static final String EXTRA_NOTIFICATION_TITLE = "com.dugan.settingsplus.CUSTOMTILE_NOTIFICATION_TITLE";

    /**
     * Key for the string extra to be displayed in the notification body
     */
    public static final String EXTRA_NOTIFICATION_BODY = "com.dugan.settingsplus.CUSTOMTILE_NOTIFICATION_BODY";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (ACTION_NOTIFICATION.equals(action)) {
            final int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);
            final String notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
            final String notificationBody = intent.getStringExtra(EXTRA_NOTIFICATION_BODY);

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            NotificationManager nm = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            nm.notify(notificationId, notification);
        }
    }
}