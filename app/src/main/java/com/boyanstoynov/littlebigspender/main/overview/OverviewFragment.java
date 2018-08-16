package com.boyanstoynov.littlebigspender.main.overview;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.BaseFragment;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.TransactionDao;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.db.model.Transaction;
import com.boyanstoynov.littlebigspender.main.transactions.AddTransactionActivity;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;
import com.boyanstoynov.littlebigspender.util.SharedPrefsManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;

/**
 * Overview fragment which contains a summary of the finances of the
 * user, including net worth, today's cash flow, and this week's cash flow.
 * It also has buttons that can start the add transaction activity. The role
 * of the class is to calculate and populate the various views.
 *
 * @author Boyan Stoynov
 */
public class OverviewFragment extends BaseFragment {

    @BindView(R.id.text_overview_fiatCurrencyCode) TextView textFiatCurrencyCode;
    @BindView(R.id.text_overview_fiatCurrencySymbol) TextView textFiatCurrencySymbol;
    @BindView(R.id.text_overview_fiatCurrencyAmount) TextView textFiatCurrencyAmount;
    @BindView(R.id.text_overview_cryptoFiatCurrencySymbol) TextView textCryptoFiatCurrencySymbol;
    @BindView(R.id.text_overview_cryptocurrencyAmount) TextView textCryptoFiatAmount;
    @BindView(R.id.text_overview_totalCurrencySymbol) TextView textTotalCurrencySymbol;
    @BindView(R.id.text_overview_totalAmount) TextView textTotalFiatAmount;
    @BindView(R.id.text_overview_dayCurrencySymbolIncome) TextView textDayIncomeCurrencySymbol;
    @BindView(R.id.text_overview_dayAmountIncome) TextView textDayIncomeAmount;
    @BindView(R.id.text_overview_dayCurrencySymbolExpense) TextView textDayExpenseCurrencySymbol;
    @BindView(R.id.text_overview_dayAmountExpense) TextView textDayExpenseAmount;
    @BindView(R.id.text_overview_weekCurrencySymbolIncome) TextView textWeekIncomeCurrencySymbol;
    @BindView(R.id.text_overview_weekAmountIncome) TextView textWeekIncomeAmount;
    @BindView(R.id.text_overview_weekCurrencySymbolExpense) TextView textWeekExpenseCurrencySymbol;
    @BindView(R.id.text_overview_weekAmountExpense) TextView textWeekExpenseAmount;
    @BindView(R.id.text_overview_day) TextView textDay;
    @BindView(R.id.text_overview_week) TextView textWeek;

    private final DecimalFormat fiatFormatter = new DecimalFormat("###,###,###,##0.00");
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    //Key for starting the Add transaction activity
    public static final String TAB_SELECT_KEY = "tabSelect";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.all_overview);
        View view = super.onCreateView(inflater, container, savedInstanceState);

        transactionDao = getRealmManager().createTransactionDao();
        accountDao = getRealmManager().createAccountDao();

        textFiatCurrencyCode.setText(SharedPrefsManager.getCurrencyCode());

        displayCurrencySymbols();

        populateNetWorthCard();

        populateTodayCashFlowCard();

        populateWeekCashFlowCard();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateNetWorthCard();

        populateTodayCashFlowCard();

        populateWeekCashFlowCard();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_overview;
    }

    /**
     * Gets the symbol of the home currency and populates all
     * currency symbol fields with it.
     */
    private void displayCurrencySymbols() {
        String currencySymbol = SharedPrefsManager.getCurrencySymbol();
        textFiatCurrencySymbol.setText(currencySymbol);
        textCryptoFiatCurrencySymbol.setText(currencySymbol);
        textTotalCurrencySymbol.setText(currencySymbol);
        textDayExpenseCurrencySymbol.setText(currencySymbol);
        textDayIncomeCurrencySymbol.setText(currencySymbol);
        textWeekExpenseCurrencySymbol.setText(currencySymbol);
        textWeekIncomeCurrencySymbol.setText(currencySymbol);
    }

    /**
     * Populate the net worth card with data from the database
     * about the balance of all fiat accounts, all crypto accounts
     * and a total balance.
     */
    private void populateNetWorthCard() {
        BigDecimal fiatNetWorth = new BigDecimal(0);
        BigDecimal cryptoNetWorth = new BigDecimal(0);

        //Calculate and display fiat and crypto net worth
        List<Account> accounts = accountDao.getAll();
        for (Account account : accounts) {
            if (account.isCrypto()) {
                BigDecimal cryptoFiatAmount = account.getBalance().multiply(account.getFiatValue());
                cryptoNetWorth = cryptoNetWorth.add(cryptoFiatAmount);
            }
            else {
                fiatNetWorth = fiatNetWorth.add(account.getBalance());
            }
        }
        textFiatCurrencyAmount.setText(fiatFormatter.format(fiatNetWorth));
        textCryptoFiatAmount.setText(fiatFormatter.format(cryptoNetWorth));

        //Calculate and display total net worth
        BigDecimal totalNetWorth = new BigDecimal(0);
        totalNetWorth = totalNetWorth.add(fiatNetWorth);
        totalNetWorth = totalNetWorth.add(cryptoNetWorth);
        textTotalFiatAmount.setText(fiatFormatter.format(totalNetWorth));
    }

    /**
     * Populate today's card with data from the database
     * about the total income and total expense for today.
     */
    private void populateTodayCashFlowCard() {
        //Display today's date
        Date today = new Date();
        String todayText = getResources().getString(R.string.overview_day);
        todayText += " " + DateTimeUtils.formatDayMonth(today);
        textDay.setText(todayText);

        BigDecimal todayIncome = new BigDecimal(0);
        BigDecimal todayExpense = new BigDecimal(0);

        //Calculate and display today's total income and expense
        List<Transaction> transactions = transactionDao.getByDate(today);
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().getType() == Category.Type.INCOME)
                todayIncome = todayIncome.add(transaction.getAmount());
            else
                todayExpense = todayExpense.add(transaction.getAmount());
        }
        textDayIncomeAmount.setText(fiatFormatter.format(todayIncome));
        textDayExpenseAmount.setText(fiatFormatter.format(todayExpense));
}

    /**
     * Populate this week's card with data from the database
     * about the total income and total expense for this week.
     */
    private void populateWeekCashFlowCard() {
        final int WEEK_DAYS_AFTER_MONDAY = 6;
        //Current calculation day, starting from Monday
        Date dayOfWeek = DateTimeUtils.getStartOfWeek();

        //Set Monday of current week to title
        String weekTextTitle = getResources().getString(R.string.overview_week);
        weekTextTitle += " " + DateTimeUtils.formatDayMonth(dayOfWeek);

        BigDecimal weekIncome = new BigDecimal(0);
        BigDecimal weekExpense = new BigDecimal(0);

        //Calculate and display this week's total income and expense
        for (int i = 0; i < WEEK_DAYS_AFTER_MONDAY; i++) {
            List<Transaction> transactions = transactionDao.getByDate(dayOfWeek);
            for (Transaction transaction : transactions) {
                if (transaction.getCategory().getType() == Category.Type.INCOME)
                    weekIncome = weekIncome.add(transaction.getAmount());
                else
                    weekExpense = weekExpense.add(transaction.getAmount());
            }
            dayOfWeek = DateTimeUtils.addDay(dayOfWeek);
        }
        textWeekIncomeAmount.setText(fiatFormatter.format(weekIncome));
        textWeekExpenseAmount.setText(fiatFormatter.format(weekExpense));

        //Set Sunday of current week to title and display it
        weekTextTitle += " - " + DateTimeUtils.formatDayMonth(dayOfWeek);
        textWeek.setText(weekTextTitle);
    }

    /**
     * Starts the add transaction activity with the income tab
     * selected.
     */
    @OnClick(R.id.button_overview_addIncome)
    public void startAddIncome() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        final Intent intent = new Intent(parentActivity, AddTransactionActivity.class);
        intent.putExtra(TAB_SELECT_KEY, INCOME_POSITION);
        startActivity(intent);
    }

    /**
     * Starts the add transaction activity with the expense tab
     * selected.
     */
    @OnClick(R.id.button_overview_addExpense)
    public void startAddExpense() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        final Intent intent = new Intent(parentActivity, AddTransactionActivity.class);
        intent.putExtra(TAB_SELECT_KEY, EXPENSE_POSITION);
        startActivity(intent);
    }
}
