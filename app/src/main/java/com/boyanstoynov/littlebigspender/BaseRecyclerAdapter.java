package com.boyanstoynov.littlebigspender;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmObject;

/**
 * Base class for Recycler View Adapters which provides storage of dataSet
 * that is used and means to update it. It also observes its ViewHolders to
 * notify them to hide their expanded buttons when another ViewHolder has
 * them opened.
 *
 * Subclasses need only implement the onBindViewHolder method to return
 * the concrete ViewHolder they are using.
 *
 * @author Boyan Stoynov
 */
public abstract class BaseRecyclerAdapter<E extends RealmObject> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    /**
     * Interface that must be implemented by controllers that want to be
     * notified whenever the Delete or Edit buttons has been clicked on
     * one of the ViewHolders.
     * @param <E>
     */
    public interface RecyclerViewListener<E extends RealmObject> {
        void onDeleteButtonClicked(E item);
        void onEditButtonClicked(E item);
    }

    private final List<E> dataSet = new ArrayList<>();
    private final List<ViewHolder> viewHolders = new ArrayList<>();
    private final RecyclerViewListener listener;

    /**
     * Constructor that takes the listener that will process the events
     * passed from this RecyclerView.
     * @param listener Controller that implements RecyclerViewListener interface
     */
    public BaseRecyclerAdapter(RecyclerViewListener listener) {
        this.listener = listener;
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
     * Hides the buttons of all ViewHolders except the last expanded
     * one.
     * @param expandedViewHolder last expanded ViewHolder
     */
    private void hideViewHolderButtons(ViewHolder expandedViewHolder) {
        for (ViewHolder viewHolder: viewHolders) {
            if (viewHolder != expandedViewHolder)
                viewHolder.hideButtons();
        }
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
        holder.setItemPresentation(getItemAt(position));
        holder.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        viewHolders.add(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        viewHolders.remove(holder);
    }

    /**
     * Base ViewHolder inner static class which stores the item that
     * populates it and binds ButterKnife. It also responds to clicks on the View
     * which show and hide the edit and delete buttons. Upon button press the listener
     * registered with the ViewHolder is notified.

     * Subclasses need to provide an implementation of the presentation
     * and provide the ID of the layout that will be used.
     * @param <E> RealmObject data item
     */
    public static abstract class ViewHolder<E extends RealmObject> extends RecyclerView.ViewHolder {

        @BindView(R.id.button_delete) Button deleteButton;
        @BindView(R.id.button_edit) Button editButton;
        @BindView(R.id.divider_expandableButtons) View divider;

        protected E item;
        private RecyclerViewListener listener;
        private BaseRecyclerAdapter adapter;
        private boolean isExpanded;

        /**
         * ViewHolder constructor.
         * @param parent parent's ViewGroup
         * @param layoutId ID of layout that will inflate into the ViewHOlder
         * @param adapter adapter that created the ViewHolder
         */
        public ViewHolder(ViewGroup parent, @LayoutRes int layoutId,BaseRecyclerAdapter adapter) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            this.adapter = adapter;

            // Bind ButterKnife to ViewHolder
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpanded)
                        showButtons();
                    else
                        hideButtons();
                }
            });
        }

        /**
         * Binds data item to this ViewHolder.
         * @param item data item for ViewHolder
         */
        void bindItem(E item) {
            this.item = item;
        }

        /**
         * Sets the presentation of the data for this ViewHolder.
         *
         * Subclasses need to provide their own implementations according
         * to the model they are bound to. This method is called after
         * an item is bound.
         */
        protected abstract void setItemPresentation(E item);

        /**
         * Shows buttons on this ViewHolder and notify adapter
         * to hide open buttons on other ViewHolders.
         */
        private void showButtons() {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            isExpanded = true;

            adapter.hideViewHolderButtons(this);
        }

        /**
         * Hides buttons on this ViewHolder.
         */
        private void hideButtons() {
            if (isExpanded) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
                isExpanded = false;
            }
        }


        private void setListener(RecyclerViewListener listener) {
            this.listener = listener;
        }

        @SuppressWarnings("unchecked")
        @OnClick(R.id.button_delete)
        public void onDeleteButtonClicked() {
            listener.onDeleteButtonClicked(item);
        }

        @SuppressWarnings("unchecked")
        @OnClick(R.id.button_edit)
        public void onEditButtonClicked() {
            listener.onEditButtonClicked(item);
        }
    }
}
