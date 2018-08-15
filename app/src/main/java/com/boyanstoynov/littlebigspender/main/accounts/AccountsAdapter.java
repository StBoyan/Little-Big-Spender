package com.boyanstoynov.littlebigspender.main.accounts;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.BaseRecyclerAdapter;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.util.Constants.CRYPTO_ACCOUNT_VIEW_TYPE;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_ACCOUNT_VIEW_TYPE;

/**
 * RecyclerView adapter for Account entities.
 *
 * @author Boyan Stoynov
 */
public class AccountsAdapter extends BaseRecyclerAdapter<Account> {

    public interface CryptoRefreshButtonListener {
        void onRefreshButtonClicked(String cryptoAccountId);
    }

    private static CryptoRefreshButtonListener refreshButtonListener;

    AccountsAdapter(RecyclerViewListener listener) {
        super(listener);
        refreshButtonListener = (CryptoRefreshButtonListener) listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FIAT_ACCOUNT_VIEW_TYPE)
            return new AccountViewHolder(parent, this);
        else
            return new CryptoViewHolder(parent, this);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).isCrypto() ? CRYPTO_ACCOUNT_VIEW_TYPE : FIAT_ACCOUNT_VIEW_TYPE;
    }

    public static class AccountViewHolder extends BaseRecyclerAdapter.ViewHolder<Account> {

        @BindView(R.id.text_itemAccount_account) TextView textAccount;
        @BindView(R.id.text_itemAccount_balance) TextView textBalance;
        @BindView(R.id.text_itemAccount_currency) TextView textCurrency;

        AccountViewHolder(ViewGroup parent, AccountsAdapter adapter) {
            super(parent, R.layout.item_account, adapter);
        }

        @Override
        protected void setItemPresentation(Account account) {
            textAccount.setText(account.getName());
            textBalance.setText(getFiatFormatter().format(account.getBalance()));
            textCurrency.setText(SharedPrefsManager.getCurrencySymbol());
        }
    }

    public static class CryptoViewHolder extends BaseRecyclerAdapter.ViewHolder<Account> {

        @BindView(R.id.text_itemCrypto_account) TextView textAccount;
        @BindView(R.id.text_itemCrypto_cryptoBalance) TextView textBalance;
        @BindView(R.id.text_itemCrypto_fiatValue) TextView textFiatValue;
        @BindView(R.id.text_itemCrypto_fiatCurrency) TextView textFiatCurrency;
        @BindView(R.id.text_itemCrypto_lastUpdated) TextView textLastUpdated;

        private Resources resources;

        CryptoViewHolder(ViewGroup parent, AccountsAdapter adapter) {
            super(parent, R.layout.item_crypto_account, adapter);
            resources = parent.getResources();
        }

        @Override
        protected void setItemPresentation(Account cryptoAccount) {
            textAccount.setText(cryptoAccount.getName());
            textBalance.setText(getCryptoFormatter().format(cryptoAccount.getBalance()));
            textFiatCurrency.setText(SharedPrefsManager.getCurrencySymbol());

            BigDecimal fiatValue = cryptoAccount.getFiatValue();

            // Display fiat value if converted rate has been fetched
            if (fiatValue.doubleValue() != 0.0)
                textFiatValue.setText(getFiatFormatter().format(
                        cryptoAccount.getBalance().multiply(fiatValue)));

            else
                textFiatValue.setText(R.string.all_not_available);

            long lastUpdate = cryptoAccount.getLastUpdated();
            if (lastUpdate != 0)
                textLastUpdated.setText(formatLastUpdated(DateTimeUtils.getElapsedTime(lastUpdate)));
            else
                textLastUpdated.setText(R.string.accounts_never);
        }

        /**
         * Returns a formatted string denoting the time of the last updated
         * according to the elapsed time in milliseconds.
         *
         * @param elapsedTime elapsed time in milliseconds
         * @return formatted string
         */
        private String formatLastUpdated(long elapsedTime) {
            //If elapsed time is at least a day
            long elapsedDays = TimeUnit.MILLISECONDS.toDays(elapsedTime);
            if (elapsedDays > 7)
                return resources.getString(R.string.accounts_moreThanAWeek);
            else if (elapsedDays > 1)
                return elapsedDays + " " + resources.getString(R.string.accounts_daysAgo);
            else if (elapsedDays == 1)
                return resources.getString(R.string.accounts_aDayAgo);

            //If elapsed time is at least an hour
            long elapsedHours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
            if (elapsedHours > 1)
                return elapsedHours + " " + resources.getString(R.string.accounts_hoursAgo);
            else if (elapsedHours == 1)
                return resources.getString(R.string.accounts_anHourAgo);

            //If elapsed time is less than an hour
            long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
            if (elapsedMinutes > 1)
                return elapsedMinutes + " " + resources.getString(R.string.accounts_minutesAgo);
            else if (elapsedMinutes == 1)
                return resources.getString(R.string.accounts_aMinuteAgo);
            else
                return resources.getString(R.string.accounts_lessThanAMinute);
        }

        /**
         * Handles a click to the refresh button. Calls the listener's
         * onRefreshButtonClicked method from a new thread, so that
         * several clicks from different view holders will not interfere
         * with each other.
         */
        @OnClick(R.id.button_itemCrypto_refresh)
        public void onRefreshButtonClicked() {
            final String cryptoAccountId = item.getId();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    refreshButtonListener.onRefreshButtonClicked(cryptoAccountId);
                }
            });
        }
    }
}
