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
 * Base class for Recycler View Adapters.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseRecyclerAdapter<T extends RealmObject, F extends BaseFragment> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected final List<T> dataSet = new ArrayList<>();
    protected final F fragment;

    public BaseRecyclerAdapter(F fragment) {
        this.fragment = fragment;
    }

    public void setData(@NonNull List<T> data) {
        dataSet.clear();
        dataSet.addAll(data);
        notifyDataSetChanged();
    }

    public T getItemAt(int position) {
        return dataSet.get(position);
    }

    //TODO put @SuppressWarning("unchecked") if it works
    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindItem(getItemAt(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //TODO may need to change Type T to another letter
    public abstract class ViewHolder<T extends RealmObject> extends RecyclerView.ViewHolder {

        protected boolean isExpanded;

        public ViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));

            // Bind ButterKnife to ViewHolder
            ButterKnife.bind(this, parent);
        }

        public abstract void bindItem(T t);
    }
}
