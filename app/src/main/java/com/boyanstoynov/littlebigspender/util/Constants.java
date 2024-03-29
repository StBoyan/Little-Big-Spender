package com.boyanstoynov.littlebigspender.util;

/**
 * Global constant values used in the application.
 *
 * @author Boyan Stoynov
 */
public class Constants {

    //Income & Expense spinner and tab positions
    public static final int INCOME_POSITION = 0;
    public static final int EXPENSE_POSITION = 1;

    //Recurring modes spinner positions
    public static final int MODE_MONTHLY_POSITION = 0;
    public static final int MODE_BIWEEKLY_POSITION = 1;
    public static final int MODE_WEEKLY_POSITION = 2;

    //Input filter settings for fiat currencies
    public static final int FIAT_DIGITS_BEFORE_ZERO_FILTER = 7;
    public static final int FIAT_DIGITS_AFTER_ZERO_FILTER = 2;

    //Input filter settings for cryptocurrencies
    public static final int CRYPTO_DIGITS_BEFORE_ZERO_FILTER = 7;
    public static final int CRYPTO_DIGITS_AFTER_ZERO_FILTER = 8;

    //View types for RecyclerView ViewHolders
    public static final int FIAT_ACCOUNT_VIEW_TYPE = 1;
    public static final int CRYPTO_ACCOUNT_VIEW_TYPE = 2;

    //Cryptocurrency API constants
    public static final String CRYPTO_API_URL = "https://min-api.cryptocompare.com/data/price";
    public static final String API_CRYPTO_CURRENCY_CODE_QUERY_STRING = "fsym";
    public static final String API_FIAT_CURRENCY_CODE_QUERY_STRING = "tsyms";

    //Models field constraints
    public static final int CATEGORY_NAME_MAX_LENGTH = 25;
    public static final int ACCOUNT_NAME_MAX_LENGTH = 20;

    //Days before recurring notification spinner position modes
    public static final int RECURRING_NOTIFICATION_MODE_ZERO_DAYS = 1;
    public static final int RECURRING_NOTIFICATION_MODE_ONE_DAYS = 2;
    public static final int RECURRING_NOTIFICATION_MODE_TWO_DAYS = 5;
}
