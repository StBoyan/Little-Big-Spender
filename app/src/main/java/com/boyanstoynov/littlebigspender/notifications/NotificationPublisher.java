package com.boyanstoynov.littlebigspender.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.boyanstoynov.littlebigspender.notifications.NotificationScheduler.NOTIFICATION_ID_KEY;
import static com.boyanstoynov.littlebigspender.notifications.NotificationScheduler.NOTIFICATION_KEY;

/**
 * Broadcast receiver for notifications.
 *
 * @author Boyan Stoynov
 */
public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION_KEY);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY, 0);
        notificationManager.notify(notificationId, notification);
    }
}
