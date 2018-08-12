package com.boyanstoynov.littlebigspender.util;

/**
 * Global constant values used in the application.
 *
 * @author Boyan Stoynov
 */
public class Constants {

    //Income & Expense spinner positions
    public static final int SPINNER_INCOME_POSITION = 0;
    public static final int SPINNER_EXPENSE_POSITION = 1;

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

    //Cryptocurrency API constants
    public static final String CRYPTO_API_URL = "https://min-api.cryptocompare.com/data/price";
    public static final String API_CRYPTO_CURRENCY_CODE_QUERY_STRING = "fsym";
    public static final String API_FIAT_CURRENCY_CODE_QUERY_STRING = "tsyms";
}
