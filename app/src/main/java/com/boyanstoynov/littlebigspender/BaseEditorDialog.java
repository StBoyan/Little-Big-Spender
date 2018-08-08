package com.boyanstoynov.littlebigspender;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmObject;

/**
 * Base class for Editor Dialogs that provides display of data
 * of an item in the database and allows it to be updated by the user.
 * Before calling show(), the data must be set with setData() method,
 * otherwise an IllegalStateException is thrown.
 * <p>
 * Subclasses need only implement the abstract methods of this class.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseEditorDialog<E extends RealmObject> extends DialogFragment {

    /**
     * Interface that must be implemented by controllers that want to be
     * notified whenever the positive (save) button has been clicked on
     * the EditDialog.
     *
     * @param <E> RealmObject model
     */
    public interface DialogListener<E extends RealmObject> {
        void onDialogPositiveClick(E realmObject);
    }

    private DialogListener<E> listener;
    protected E item;
    private Unbinder unbinder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (item == null)
            throw new IllegalStateException("No data item. Call setData() first.");

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(getTitleResource())
                .customView(getLayoutResource(), true)
                .positiveText(R.string.all_save)
                .negativeText(R.string.all_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onPositiveClick())
                            listener.onDialogPositiveClick(item);
                    }
                }).build();

        // Bind ButterKnife
        unbinder = ButterKnife.bind(this, dialog.getCustomView());

        populateDialog();

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind ButterKnife
        unbinder.unbind();
    }

    /**
     * Set data to be displayed by this Dialog.
     * <p>
     * Note: for this class to work correctly, the RealmObject must
     * be unmanaged by Realm (i.e. it needs to be a POJO). Hence, if a
     * managed object is passed as parameter, this method will throw an
     * IllegalArgumentException. Updating the  database should happen in
     * the listener (controller).
     *
     * @param item unmanaged data item
     */
    public final void setData(E item) {
        if (item.isManaged())
            throw new IllegalArgumentException("Object is managed by Realm. Need to use an unmanaged RealmObject instead.");

        this.item = item;
    }

    /**
     * Gets the ID of the String resource title that will
     * be displayed on this dialog.
     *
     * @return resource title ID
     */
    protected abstract int getTitleResource();

    /**
     * Gets the ID of the layout that will fill this dialog.
     *
     * @return layout ID
     */
    protected abstract int getLayoutResource();

    /**
     * Implement the logic for what happens on a positive click
     * (save) in this method. This includes any necessary validation
     * of input and updating the unmanaged data object that will be
     * passed to the listener. The returned boolean indicates whether
     * the validation is passed. If true modified data item will be
     * passed to listener.
     *
     * @return boolean whether validation is passed
     */
    protected abstract boolean onPositiveClick();

    /**
     * Implement how the data object should be displayed in this
     * dialog.
     */
    protected abstract void populateDialog();
}
