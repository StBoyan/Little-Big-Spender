package com.boyanstoynov.littlebigspender.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.boyanstoynov.littlebigspender.R;

/**
 * Provides abstraction over SharedPreferences use. Before using, the class
 * must be initialised with the context of an Application or its subclass.
 * Static methods provide read and write access to the SharedPreferences file.
 * Also, for better performance, some often used values are cached and there is
 * a method to access them from the cache without using a key.
 *
 * @author Boyan Stoynov
 */
public class SharedPrefsManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    //Cached Keys and Values
    private static String currencySymbolKey;
    private static String currencyCodeKey;
    private static String currencySymbol;
    private static String currencyCode;

    /**
     * Initialises the SharedPreferenceManager and load cached keys/values into
     * memory. This should preferably be done in an Application class or its
     * subclass.
     * @param context Application context
     */
    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        if (!(context instanceof Application))
            throw new RuntimeException("Must use application context for initialisation.");

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            currencySymbolKey = context.getResources().getString(R.string.currencySymbol);
            currencySymbol = read(currencySymbolKey, "N/A");

            currencyCodeKey = context.getResources().getString(R.string.currencyCode);
            currencyCode = read(currencyCodeKey, "N/A");
        }
    }

    public static String read(String key, String defValue) {
        checkIfInitialised();
        return sharedPreferences.getString(key, defValue);
    }

    public static void write(String key, String value) {
        checkIfInitialised();
        if (key.equals(currencySymbolKey))
            currencySymbol = value;
        else if (key.equals(currencyCodeKey))
            currencyCode = value;

        editor.putString(key, value);
        editor.apply();
    }

    public static boolean read(String key, boolean defValue) {
        checkIfInitialised();
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        checkIfInitialised();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int read(String key, int defValue) {
        checkIfInitialised();
        return sharedPreferences.getInt(key, defValue);
    }

    public static void write(String key, int value) {
        checkIfInitialised();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getCurrencySymbol() {
        checkIfInitialised();
        return currencySymbol;
    }

    public static String getCurrencyCode() {
        checkIfInitialised();
        return currencyCode;
    }

    private static void checkIfInitialised() {
        if (sharedPreferences == null) {
            throw new IllegalStateException("Not initialised. Need to call init(Context context) first.");
        }
    }
}
