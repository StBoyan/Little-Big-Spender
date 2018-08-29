package com.boyanstoynov.littlebigspender.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.recurring.RecurringActivity;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.util.Date;

import static com.boyanstoynov.littlebigspender.util.Constants.RECURRING_NOTIFICATION_MODE_ONE_DAYS;
import static com.boyanstoynov.littlebigspender.util.Constants.RECURRING_NOTIFICATION_MODE_TWO_DAYS;
import static com.boyanstoynov.littlebigspender.util.Constants.RECURRING_NOTIFICATION_MODE_ZERO_DAYS;

/**
 * Handles scheduling recurring transaction notifications
 * to be displayed on the device when the system is in the background.
 * Notifications are displayed N days in advance as set by the user.
 * This class uses an AlarmManager to schedule the notifications in
 * the future.
 *
 * @author Boyan Stoynov
 */
public class NotificationScheduler {

    private final int RECURRING_MODE_ZERO = 0;
    private final int RECURRING_MODE_ONE = 1;
    private final int RECURRING_MODE_TWO = 2;
    private final String RECURRING_NOTIFICATION_CHANNEL = "recurring";
    // Keys used in Bundles for notifications
    protected static final String NOTIFICATION_KEY = "notification";
    protected static final String NOTIFICATION_ID_KEY = "notificationId";

    private int recurringMode;
    private Context context;
    private int notificationId = 0;

    /**
     * Constructor that takes application context.
     * @param context application context
     */
    public NotificationScheduler(Context context) {
        this.context = context;
        recurringMode = SharedPrefsManager.read(
                context.getResources().getString(R.string.recurringNotificationMode), 0);
    }

    /**
     * Schedules a notification to be displayed by the system for the specified
     * Recurring Transaction. Upon pressing the notification the user is taken
     * to the Recurring Transaction screen.
     * @param recurring
     */
    public void schedule(Recurring recurring) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, RECURRING_NOTIFICATION_CHANNEL)
                .setContentTitle(context.getString(R.string.recurring_notification_title))
                .setContentText(formatNotificationText(recurring))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        Intent intent = new Intent(context, RecurringActivity.class);
        PendingIntent activity = PendingIntent.getActivity(
                context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID_KEY, notificationId);
        notificationIntent.putExtra(NOTIFICATION_KEY, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long notificationShowTime = getRecurringNotificationAdvanceInMillis(
                recurring.getNextTransactionDate());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationShowTime, pendingIntent);
        notificationId++;
    }

    /**
     * Formats the string that is displayed in the recurring transactions
     * notification.
     * @param recurring Recurring transaction object
     * @return formatted string ready to be displayed
     */
    private String formatNotificationText(Recurring recurring) {
        String notificationText = "";
        //Appends the days to left before the recurring transaction
        switch (recurringMode) {
            case RECURRING_MODE_ZERO:
                notificationText += RECURRING_NOTIFICATION_MODE_ZERO_DAYS;
                break;
            case RECURRING_MODE_ONE:
                notificationText += RECURRING_NOTIFICATION_MODE_ONE_DAYS;
                break;
            case RECURRING_MODE_TWO:
                notificationText += RECURRING_NOTIFICATION_MODE_TWO_DAYS;
                break;
        }
        notificationText += " " + context.getString(R.string.recurring_notification_text);
        notificationText += " " + SharedPrefsManager.getCurrencySymbol();
        notificationText += " " + recurring.getAmount().toString();

        return notificationText;
    }

    /**
     * Gets the time in advance according the recurring notification mode of
     * the specified date in milliseconds.
     * @param date Date
     * @return time in milliseconds
     */
    private long getRecurringNotificationAdvanceInMillis(Date date) {
        Date advanceDate = null;
        switch (recurringMode) {
            case RECURRING_MODE_ZERO:
                advanceDate = DateTimeUtils.subtractDays(date, RECURRING_NOTIFICATION_MODE_ZERO_DAYS);
                break;
            case RECURRING_MODE_ONE:
                advanceDate = DateTimeUtils.subtractDays(date, RECURRING_NOTIFICATION_MODE_ONE_DAYS);
                break;
            case RECURRING_MODE_TWO:
                advanceDate = DateTimeUtils.subtractDays(date, RECURRING_NOTIFICATION_MODE_TWO_DAYS);
                break;
        }
        return advanceDate.getTime();
    }
}
