package com.boyanstoynov.littlebigspender.util;

import android.content.Context;

import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.dao.AccountDao;
import com.boyanstoynov.littlebigspender.db.dao.CategoryDao;
import com.boyanstoynov.littlebigspender.db.dao.RealmManager;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;


/**
 * Initial setup Runnable class to be executed upon the first
 * launch of the application. Provides initial database population and
 * settings.
 *
 * @author Boyan Stoynov
 */
public class InitialSetup implements Runnable {

    private RealmManager realmManager = new RealmManager();
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private Context context;

    /**
     * Takes context to access application resources.
     * @param context context
     */
    public InitialSetup(Context context) {
        this.context = context;
    }

    /**
     * Tasks to be executed on first application launch.
     */
    @Override
    public void run() {
        realmManager.open();
        categoryDao = realmManager.createCategoryDao();
        accountDao = realmManager.createAccountDao();

        createStartingAccounts();

        createStartingCategories();

        setInitialCurrency();

        realmManager.close();
        //Relinquish context reference to prevent memory leak
        context = null;
    }

    /**
     * Create the starting categories in the database.
     */
    private void createStartingCategories() {
        Category cat1 = new Category();
        cat1.setName("Household");
        cat1.setType(Category.Type.EXPENSE);
        categoryDao.save(cat1);

        Category cat2 = new Category();
        cat2.setName("Entertainment");
        cat2.setType(Category.Type.EXPENSE);
        categoryDao.save(cat2);

        Category cat3 = new Category();
        cat3.setName("Utilities");
        cat3.setType(Category.Type.EXPENSE);
        categoryDao.save(cat3);

        Category cat4 = new Category();
        cat4.setName("Rent");
        cat4.setType(Category.Type.EXPENSE);
        categoryDao.save(cat4);

        Category cat5 = new Category();
        cat5.setName("Misc");
        cat5.setType(Category.Type.EXPENSE);
        categoryDao.save(cat5);

        Category cat6 = new Category();
        cat6.setName("Clothing");
        cat6.setType(Category.Type.EXPENSE);
        categoryDao.save(cat6);

        Category cat7 = new Category();
        cat7.setName("Salary");
        cat7.setType(Category.Type.INCOME);
        categoryDao.save(cat7);

        Category cat8 = new Category();
        cat8.setName("Interest");
        cat8.setType(Category.Type.INCOME);
        categoryDao.save(cat8);

        Category cat9 = new Category();
        cat9.setName("Dividends");
        cat9.setType(Category.Type.INCOME);
        categoryDao.save(cat9);

        Category cat10 = new Category();
        cat10.setName("Other");
        cat10.setType(Category.Type.INCOME);
        categoryDao.save(cat10);
    }

    /**
     * Create the starting accounts in the database.
     */
    private void createStartingAccounts() {
        Account acc1 = new Account();
        acc1.setName("Bank Account");
        acc1.setBalance(new BigDecimal(0));

        Account acc2 = new Account();
        acc2.setName("Cash");
        acc2.setBalance(new BigDecimal(0));

        accountDao.save(acc1);
        accountDao.save(acc2);
    }

    /**
     * Detects the local currency and saves code, symbol and flag id to the
     * SharedPreferences.
     */
    private void setInitialCurrency() {
        Locale defaultLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(defaultLocale);
        ExtendedCurrency localCurrency = ExtendedCurrency.getCurrencyByISO(currency.getCurrencyCode());
        SharedPreferencesManager.write(context.getResources().getString(R.string.currencyCode), localCurrency.getCode());
        SharedPreferencesManager.write(context.getResources().getString(R.string.currencySymbol), localCurrency.getSymbol());
        SharedPreferencesManager.write(context.getResources().getString(R.string.currencyDrawableId), localCurrency.getFlag());
    }
}
