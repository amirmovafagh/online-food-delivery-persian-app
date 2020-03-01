package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserOrdersItemBinding;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersRecyclerViewInterface;

public class OrdersAdapter extends PagedListAdapter<OrderItem, OrdersAdapter.OrdersViewHolder> {

    private OrdersRecyclerViewInterface clickListener;
    private Context context;


    public OrdersAdapter(OrdersRecyclerViewInterface clickListener, Context context) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.context = context;

    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserOrdersItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_orders_item, parent, false);
        return new OrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        OrderItem currentOrder = getItem(position);
        holder.binding.setOrderItem(currentOrder);
    }

    private static DiffUtil.ItemCallback<OrderItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OrderItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull OrderItem oldItem, @NonNull OrderItem newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class OrdersViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewUserOrdersItemBinding binding;

        public OrdersViewHolder(@NonNull RecyclerviewUserOrdersItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
