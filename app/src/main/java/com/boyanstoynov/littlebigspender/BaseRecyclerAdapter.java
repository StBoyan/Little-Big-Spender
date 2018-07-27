package com.boyanstoynov.littlebigspender;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.RealmObject;

/**
 * Base class for Recycler View Adapters which provides storage of dataSet
 * that is used and means to update it. Subclasses need only implement the
 * onBindViewHolder method to return the concrete ViewHolder they are using.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseRecyclerAdapter<E extends RealmObject, T extends BaseFragment> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    private final List<E> dataSet = new ArrayList<>();
    protected final T fragment;

    /**
     * Constructor which takes the Fragment that serves as controller
     * for this RecyclerView adapter.
     * @param fragment controller
     */
    public BaseRecyclerAdapter(T fragment) {
        this.fragment = fragment;
    }

    /**
     * Set data for this adapter and refresh all views to
     * reflect new data.
     * @param data List<E> data items
     */
    public void setData(@NonNull List<E> data) {
        dataSet.clear();
        dataSet.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Get item from list at position.
     * @param position of item
     * @return item of data set
     */
    private E getItemAt(int position) {
        return dataSet.get(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindItem(getItemAt(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     * Base ViewHolder inner static class which stores the item that
     * populates it and the controlling fragment, and binds ButterKnife.
     * Subclasses need to provide an implementation of the presentation
     * and provide the ID of the layout that will be used.
     * @param <E> RealmObject data item
     * @param <T> BaseFragment controller
     */
    public static abstract class ViewHolder<E extends RealmObject, T extends BaseFragment> extends RecyclerView.ViewHolder {

        protected E item;
        protected T fragment;

        /**
         * ViewHolder constructor.
         * @param parent parent's ViewGroup
         * @param layoutId id of layout that will inflate the ViewHolder
         * @param fragment fragment which contains ViewHolder
         */
        public ViewHolder(ViewGroup parent, @LayoutRes int layoutId, T fragment) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            this.fragment = fragment;

            // Bind ButterKnife to ViewHolder
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binds data item to this ViewHolder.
         * @param item data item for ViewHolder
         */
        void bindItem(E item) {
            this.item = item;
            setItemPresentation(item);
        }

        /**
         * Subclasses need to provide their own implementation of how
         * data is presented in the ViewHolder. This method is called as
         * soon as an item is bound.
         */
        protected abstract void setItemPresentation(E item);
    }
}
