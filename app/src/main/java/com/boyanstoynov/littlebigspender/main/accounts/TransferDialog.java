package com.boyanstoynov.littlebigspender.main.accounts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.boyanstoynov.littlebigspender.R;
import com.boyanstoynov.littlebigspender.db.model.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Transfer dialog for Accounts.
 *
 * @author Boyan Stoynov
 */
public class TransferDialog extends DialogFragment {

    public interface TransferDialogCallback {
        void onTransferAccount(Account from, Account to, BigDecimal amount);
    }

    @BindView(R.id.spinner_dialogTransfer_from) Spinner fromAccountSpinner;
    @BindView(R.id.spinner_dialogTransfer_to) Spinner toAccountSpinner;
    // TODO implement validation for number input if needed - this could be if number is negative display error message on inputField
    @BindView(R.id.numberInput_dialogTransfer_amount) EditText amountInput;

    private Unbinder unbinder;
    private List<Account> sourceAccountList;
    private List<Account> destinationAccountList;
    private TransferDialogCallback callback;
    private View positiveButton;
    private Account selectedSourceAccount;
    private Account selectedDestinationAccount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (sourceAccountList == null || destinationAccountList == null)
            throw new IllegalStateException("No accounts set. Call setFromAccountList() before show().");
        if (callback == null)
            throw new IllegalStateException("Callback not set. Call setCallback() before show().");

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.accounts_transferDialog_title)
                .customView(R.layout.dialog_account_transfer, true)
                .positiveText(R.string.accounts_transferDialog_transfer)
                .negativeText(R.string.all_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        callback.onTransferAccount(selectedSourceAccount, selectedDestinationAccount, new BigDecimal(amountInput.getText().toString()));
                    }
                })
                .build();

        unbinder = ButterKnife.bind(this, dialog.getCustomView());

        sourceAccountList.add(0, new Account());
        fromAccountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, sourceAccountList));

        destinationAccountList.add(0, new Account());
        toAccountSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, destinationAccountList));
        toAccountSpinner.setEnabled(false);

        positiveButton = dialog.getActionButton(DialogAction.POSITIVE);
        positiveButton.setEnabled(false);
        amountInput.setEnabled(false);

        fromAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sourceAccountList.get(position).getName() != null) {

                    selectedSourceAccount = sourceAccountList.get(position);

                    if (sourceAccountList.get(0).getName() == null)
                        sourceAccountList.remove(0);

                    if (selectedDestinationAccount != null && !selectedDestinationAccount.equals(selectedSourceAccount))
                        positiveButton.setEnabled(true);
                    else
                        positiveButton.setEnabled(false);

                    toAccountSpinner.setEnabled(true);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        toAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (destinationAccountList.get(position).getName() != null) {

                    selectedDestinationAccount = destinationAccountList.get(position);

                    if (destinationAccountList.get(0).getName() == null)
                        destinationAccountList.remove(0);

                    if (!selectedDestinationAccount.equals(selectedSourceAccount))
                        positiveButton.setEnabled(true);
                    else
                        positiveButton.setEnabled(false);

                    amountInput.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    public void setAccountList(List<Account> accountList) {
        sourceAccountList = new ArrayList<>();
        destinationAccountList = new ArrayList<>();
        sourceAccountList.addAll(accountList);
        destinationAccountList.addAll(accountList);
    }

    public void setCallback(TransferDialogCallback callback) {
        this.callback = callback;
    }
}
