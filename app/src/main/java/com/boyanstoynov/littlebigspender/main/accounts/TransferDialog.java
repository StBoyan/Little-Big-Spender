package com.boyanstoynov.littlebigspender.main.accounts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;
import com.boyanstoynov.littlebigspender.util.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_AFTER_ZERO_FILTER;
import static com.boyanstoynov.littlebigspender.util.Constants.FIAT_DIGITS_BEFORE_ZERO_FILTER;

/**
 * Transfer dialog for transferring balance between fiat accounts.
 * Validates user input and notifies callback about source, destination
 * accounts and amount.
 *
 * @author Boyan Stoynov
 */
public class TransferDialog extends DialogFragment {

    /**
     * Interface that must be implemented by a controller that
     * wants to be notified when the user confirms a balance transfer.
     */
    public interface TransferDialogCallback {
        void onTransferAccount(Account from, Account to, BigDecimal amount);
    }

    @BindView(R.id.spinner_dialogTransfer_from) Spinner fromAccountSpinner;
    @BindView(R.id.spinner_dialogTransfer_to) Spinner toAccountSpinner;
    @BindView(R.id.numberInput_dialogTransfer_amount) EditText amountInput;

    private Unbinder unbinder;
    private List<Account> accountList;
    private TransferDialogCallback callback;
    private View positiveButton;
    private Account selectedSourceAccount;
    private Account selectedDestinationAccount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (accountList == null)
            throw new IllegalStateException("No accounts set. Call setAccountList() before show().");
        if (callback == null)
            throw new IllegalStateException("Callback not set. Call setCallback() before show().");

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.accounts_transferDialog_title)
                .customView(R.layout.dialog_account_transfer, true)
                .positiveText(R.string.accounts_transferDialog_transfer)
                .negativeText(R.string.all_cancel)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (amountIsValid()) {
                            callback.onTransferAccount(
                                    selectedSourceAccount, selectedDestinationAccount,
                                    new BigDecimal(amountInput.getText().toString()));
                            dialog.dismiss();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        unbinder = ButterKnife.bind(this, dialog.getCustomView());

        fromAccountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, accountList));
        toAccountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, accountList));
        /* Set the source and destination account to the first position since
           the spinners select it by default */
        selectedDestinationAccount = accountList.get(0);
        selectedSourceAccount = accountList.get(0);

        positiveButton = dialog.getActionButton(DialogAction.POSITIVE);
        positiveButton.setEnabled(false);

        amountInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                FIAT_DIGITS_BEFORE_ZERO_FILTER, FIAT_DIGITS_AFTER_ZERO_FILTER)});

        setUpSpinnersListeners();

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public void setCallback(TransferDialogCallback callback) {
        this.callback = callback;
    }

    /**
     * Setup the spinner listeners for the source and account destination.
     * If distinct account are chosen for both, enable the positive button,
     * otherwise disable it.
     */
    private void setUpSpinnersListeners() {
        fromAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSourceAccount = accountList.get(position);
                // If different destination account is chosen enable positive button
                if (!selectedDestinationAccount.equals(selectedSourceAccount))
                    positiveButton.setEnabled(true);
                else
                    positiveButton.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        toAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDestinationAccount = accountList.get(position);
                // If different source account is chosen enable positive button
                if (!selectedSourceAccount.equals(selectedDestinationAccount))
                    positiveButton.setEnabled(true);
                else
                    positiveButton.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Checks amount user input and returns boolean whether it is
     * valid or not. If invalid displays error message to user.
     * @return boolean whether valid or not
     */
    private boolean amountIsValid() {
        if (amountInput.getText().toString().equals("")) {
            amountInput.setError(getResources().getString(R.string.all_blank_field_error));
            return false;
        }

        BigDecimal transferAmount = new BigDecimal(amountInput.getText().toString());
        if (transferAmount.compareTo(BigDecimal.ZERO) < 1) {
            amountInput.setError(getResources().getString(R.string.all_cannot_be_zero_error));
            return false;
        }

        if(transferAmount.compareTo(selectedSourceAccount.getBalance()) > 0) {
            amountInput.setError(getResources().getString(R.string.accounts_transfer_exceed_error));
            return false;
        }

        return true;
    }
}
