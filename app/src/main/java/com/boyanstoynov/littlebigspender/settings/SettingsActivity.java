package com.boyanstoynov.littlebigspender.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for settings activity.
 *
 * @author Boyan Stoynov
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar_settings) Toolbar toolbar;
    @BindView(R.id.image_settings_currency) ImageView currencyImage;
    @BindView(R.id.text_settings_currencycode) TextView textCurrencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_settings);

        displayCurrency();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    // TODO put this in intro so it doesn't crash !!!!!!!!!!
    @OnClick(R.id.container_settings_currency)
    public void changeCurrency() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = prefs.edit();

                e.putString("currencyCode", code);
                e.putString("currencySymbol", symbol);
                e.putInt("currencyDrawableId", flagDrawableResID);
                e.apply();
                picker.dismiss();
                displayCurrency();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }

    //TODO remove temporary debugging method.
    @OnClick(R.id.button_settings_reset)
    public void resetPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = prefs.edit();

        e.remove("firstStart");
        e.apply();
    }

    private void displayCurrency() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        textCurrencyCode.setText(prefs.getString("currencyCode", "N/A"));
        //TODO remove flag EUR
        currencyImage.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), prefs.getInt("currencyDrawableId", R.drawable.flag_eur)));
    }
}
