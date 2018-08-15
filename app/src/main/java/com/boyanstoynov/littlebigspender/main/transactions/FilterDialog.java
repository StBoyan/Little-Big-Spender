package com.boyanstoynov.littlebigspender.main.transactions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.db.model.Category;
import com.boyanstoynov.littlebigspender.util.DateTimeUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.boyanstoynov.littlebigspender.util.Constants.INCOME_POSITION;
import static com.boyanstoynov.littlebigspender.util.Constants.EXPENSE_POSITION;

/**
 * Filter dialog to select a filter for a list of transactions. Displays
 * radio buttons which enable their input and disable the others, allowing
 * only one type of filter to be applied. Also has a button to reset the
 * filter applied. Provides callback to the listener upon selection of a
 * filter.
 *
 * @author Boyan Stoynov
 */
public class FilterDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    /**
     * Interface that must be implemented by the controller object that
     * wants to be notified of the filter selected in the dialog.
     */
    public interface FilterSelectedCallback {
        void onTypeFilterSelected(Category.Type type);
        void onCategoryFilterSelected(Category category);
        void onAccountFilterSelected(Account account);
        void onDateFilterSelected(Date date);
        void onResetSelected();
    }

    @BindView(R.id.radio_filter_type) RadioButton typeRadioButton;
    @BindView(R.id.radio_filter_category) RadioButton categoryRadioButton;
    @BindView(R.id.radio_filter_account) RadioButton accountRadioButton;
    @BindView(R.id.radio_filter_date) RadioButton dateRadioButton;
    @BindView(R.id.spinner_filter_type) Spinner typeSpinner;
    @BindView(R.id.spinner_filter_category) Spinner categorySpinner;
    @BindView(R.id.spinner_filter_account) Spinner accountSpinner;
    @BindView(R.id.dateInput_filter) EditText dateInput;

    private Unbinder unbinder;
    private List<Category> categoryList;
    private List<Account> accountList;
    private FilterSelectedCallback callback;
    private Date date;
    private View positiveButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (categoryList == null)
            throw new IllegalStateException("No categories set. Call setCategoryList() before show().");
        if (accountList == null)
            throw new IllegalStateException("No accounts set. Call setFromAccountList() before show().");
        if (callback == null)
            throw new IllegalStateException("Callback not set. Call setCallback() before show().");

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.transaction_filterDialog_title)
                .customView(R.layout.dialog_transactions_filter, true)
                .positiveText(R.string.transaction_filterDialog_positive)
                .negativeText(R.string.all_cancel)
                .neutralText(R.string.transaction_filterDialog_reset)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (typeRadioButton.isChecked()) {
                            if (typeSpinner.getSelectedItemPosition() == INCOME_POSITION)
                                callback.onTypeFilterSelected(Category.Type.INCOME);
                            else if (typeSpinner.getSelectedItemPosition() == EXPENSE_POSITION)
                                callback.onTypeFilterSelected(Category.Type.EXPENSE);
                        }
                        else if (categoryRadioButton.isChecked())
                            callback.onCategoryFilterSelected((Category)categorySpinner.getSelectedItem());
                        else if (accountRadioButton.isChecked())
                            callback.onAccountFilterSelected((Account)accountSpinner.getSelectedItem());
                        else if (dateRadioButton.isChecked())
                            callback.onDateFilterSelected(date);

                    }
                }).onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callback.onResetSelected();
                    }
                })
                .build();

        unbinder = ButterKnife.bind(this, dialog.getCustomView());

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, categoryList);
        ArrayAdapter<Account> accountAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, accountList);
        categorySpinner.setAdapter(categoryAdapter);
        accountSpinner.setAdapter(accountAdapter);
        dateInput.setText(DateTimeUtils.formatDate(date));
        //Disable spinners and date input initially
        typeSpinner.setEnabled(false);
        categorySpinner.setEnabled(false);
        accountSpinner.setEnabled(false);
        dateInput.setEnabled(false);
        //Disable positive button initially
        positiveButton = dialog.getActionButton(DialogAction.POSITIVE);
        positiveButton.setEnabled(false);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    /**
     * Disable inputs other than type and enable
     * its spinner.
     */
    @OnClick(R.id.radio_filter_type)
    public void onTypeRadioButtonClicked() {
        positiveButton.setEnabled(true);
        categoryRadioButton.setChecked(false);
        accountRadioButton.setChecked(false);
        dateRadioButton.setChecked(false);
        typeSpinner.setEnabled(true);
        categorySpinner.setEnabled(false);
        accountSpinner.setEnabled(false);
        dateInput.setEnabled(false);
    }

    /**
     * Disable inputs other than category
     * and enable its spinner.
     */
    @OnClick(R.id.radio_filter_category)
    public void onCategoryRadioButtonClicked() {
        positiveButton.setEnabled(true);
        typeRadioButton.setChecked(false);
        accountRadioButton.setChecked(false);
        dateRadioButton.setChecked(false);
        typeSpinner.setEnabled(false);
        categorySpinner.setEnabled(true);
        accountSpinner.setEnabled(false);
        dateInput.setEnabled(false);
    }

    /**
     * Disable inputs other than account and
     * enable its spinner.
     */
    @OnClick(R.id.radio_filter_account)
    public void onAccountRadioButtonClicked() {
        positiveButton.setEnabled(true);
        typeRadioButton.setChecked(false);
        categoryRadioButton.setChecked(false);
        dateRadioButton.setChecked(false);
        typeSpinner.setEnabled(false);
        categorySpinner.setEnabled(false);
        accountSpinner.setEnabled(true);
        dateInput.setEnabled(false);
    }

    /**
     * Disable inputs other than date and
     * enable its input field.
     */
    @OnClick(R.id.radio_filter_date)
    public void onDateRadioButtonClicked() {
        positiveButton.setEnabled(true);
        typeRadioButton.setChecked(false);
        categoryRadioButton.setChecked(false);
        accountRadioButton.setChecked(false);
        typeSpinner.setEnabled(false);
        categorySpinner.setEnabled(false);
        accountSpinner.setEnabled(false);
        dateInput.setEnabled(true);
    }

    @OnClick(R.id.dateInput_filter)
    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this,
                DateTimeUtils.yearToday(), DateTimeUtils.monthToday(), DateTimeUtils.dayToday());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        dateInput.setText(DateTimeUtils.formatDate(date));
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public void setCallback(FilterSelectedCallback callback) {
        this.callback = callback;
    }
}
