package com.boyanstoynov.littlebigspender.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.boyanstoynov.littlebigspender.BaseActivity;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.CryptoData;
import com.boyanstoynov.littlebigspender.main.MainActivity;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Controller for settings activity. Shows various application wide
 * settings, takes the user preferences and writes then to the shared
 * preferences file.
 *
 * @author Boyan Stoynov
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar_settings) Toolbar toolbar;
    @BindView(R.id.image_settings_currency) ImageView currencyImage;
    @BindView(R.id.text_settings_currencycode) TextView textCurrencyCode;
    @BindView(R.id.switch_settings_transactionOverdraft) Switch transactionOverdraftSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_settings);

        displayCurrency();

        // Get transaction overdraft state from shared preferences
        boolean overdraftAllowed = SharedPrefsManager.read(
                getResources().getString(R.string.allowTransactionOverdraft), false);
        transactionOverdraftSwitch.setChecked(overdraftAllowed);

        // Save changes of transaction overdraft switch to shared preferences
        transactionOverdraftSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPrefsManager.write(
                        getResources().getString(R.string.allowTransactionOverdraft), isChecked);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    /**
     * Shows the currency picker dialog to the user and gets the input.
     * If the currency is changed, write changes to shared preferences and
     * invalidate stored cryptocurrency data since it will need to be updated
     * by user for the new fiat currency.
     */
    @OnClick(R.id.button_settings_currency)
    public void onCurrencyClicked() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                //Check if currency has been changed
                if (!code.equals(
                        SharedPrefsManager.read(
                                getResources().getString(R.string.currencyCode), ""))) {
                    SharedPrefsManager.write(getResources().getString(R.string.currencyCode), code);
                    SharedPrefsManager.write(getResources().getString(R.string.currencySymbol), symbol);
                    SharedPrefsManager.write(getResources().getString(R.string.currencyDrawableId), flagDrawableResID);
                    invalidateCryptoData();
                    displayCurrency();
                }
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
    }

    /**
     * Display the current currency code and its flag.
     */
    private void displayCurrency() {
        textCurrencyCode.setText(SharedPrefsManager.read(getResources().getString(R.string.currencyCode), "N/A"));
        currencyImage.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),
                SharedPrefsManager.read(getResources().getString(R.string.currencyDrawableId), R.drawable.flag_gbp)));
    }

    /**
     * Erase the stored cryptocurrency data for each cryptocurrency account.
     * Note: the crypto data are its fiat value and last update time, not the
     * account itself.
     */
    private void invalidateCryptoData() {
        AccountDao accountDao = getRealmManager().createAccountDao();
        List<Account> accounts = accountDao.getAllCrypto();

        for (Account account : accounts) {
            accountDao.editCryptoData(account, new CryptoData());
        }
    }

    /**
     * Shows a confirmation dialog upon pressing the erase all data button.
     * If user confirms, deletes all entities from the database, sets the first
     * launch boolean in shared preferences to true and launches the main activity.
     */
    @OnClick(R.id.textButton_settings_eraseData)
    public void onEraseDataButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.settings_erase_all_data_warning);
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.all_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Set first launch to true
                SharedPrefsManager.write(getResources().getString(R.string.firstStart), true);
                //Erase database
                getRealmManager().eraseRealm();
                //Launch start activity
                final Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.all_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
