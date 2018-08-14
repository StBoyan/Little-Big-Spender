package com.boyanstoynov.littlebigspender.settings;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Controller for settings activity.
 *
 * @author Boyan Stoynov
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar_settings)
    Toolbar toolbar;
    @BindView(R.id.image_settings_currency)
    ImageView currencyImage;
    @BindView(R.id.text_settings_currencycode)
    TextView textCurrencyCode;

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

    @OnClick(R.id.container_settings_currency)
    public void changeCurrency() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                SharedPrefsManager.write(getResources().getString(R.string.currencyCode), code);
                SharedPrefsManager.write(getResources().getString(R.string.currencySymbol), symbol);
                SharedPrefsManager.write(getResources().getString(R.string.currencyDrawableId), flagDrawableResID);

                picker.dismiss();
                displayCurrency();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }

    //TODO remove temporary debugging method.
    @OnClick(R.id.button_settings_reset)
    public void resetPreferences() {
        SharedPrefsManager.write(getResources().getString(R.string.firstStart), true);
    }

    private void displayCurrency() {
        textCurrencyCode.setText(SharedPrefsManager.read(getResources().getString(R.string.currencyCode), "N/A"));

        currencyImage.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),
                SharedPrefsManager.read(getResources().getString(R.string.currencyDrawableId), R.drawable.flag_gbp)));
    }
}
