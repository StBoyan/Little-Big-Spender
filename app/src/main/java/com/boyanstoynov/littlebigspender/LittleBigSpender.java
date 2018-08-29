package com.boyanstoynov.littlebigspender;

import android.app.Application;

import com.boyanstoynov.littlebigspender.db.dao.RealmManager;
import com.boyanstoynov.littlebigspender.db.dao.RecurringDao;
import com.boyanstoynov.littlebigspender.db.model.Recurring;
import com.boyanstoynov.littlebigspender.notifications.NotificationScheduler;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.util.List;

import io.realm.Realm;

/**
 * Application class which initialises Realm database and the
 * SharedPreferences Manager, and sets future notifications.
 * d
 *
 * @author Boyan Stoynov
 */
public class LittleBigSpender extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialise Realm database on launch with default config
        Realm.init(this);
        //Initialise SharedPreferences manager
        SharedPrefsManager.init(this);
        //Set notifications to be displayed in the future
        setNotifications();
    }

    /**
     * Sets a notification tobe displayed in the future for
     * each Recurring Transaction.
     */
    private void setNotifications() {
        RealmManager realmManager = new RealmManager();
        realmManager.open();
        RecurringDao recurringDao = realmManager.createRecurringDao();

        List<Recurring> recurringTransactions = recurringDao.getAllRecurringTransactions();
        NotificationScheduler notificationScheduler = new NotificationScheduler(this);

        for (Recurring recurring : recurringTransactions) {
            notificationScheduler.schedule(recurring);
        }
        realmManager.close();
    }
}
