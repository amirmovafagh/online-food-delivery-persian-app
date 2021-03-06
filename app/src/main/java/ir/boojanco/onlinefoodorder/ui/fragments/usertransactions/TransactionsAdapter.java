package ir.boojanco.onlinefoodorder.ui.fragments.usertransactions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserOrdersItemBinding;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewWalletActivityItemBinding;
import ir.boojanco.onlinefoodorder.models.user.WalletActivity;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersRecyclerViewInterface;

public class TransactionsAdapter extends PagedListAdapter<WalletActivity, TransactionsAdapter.OrdersViewHolder> {

    private Context context;

    public TransactionsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewWalletActivityItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_wallet_activity_item, parent, false);
        return new OrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        WalletActivity currentActivity = getItem(position);
        holder.binding.setWalletItem(currentActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            if (currentActivity != null && currentActivity.getType().equals("کاهش موجودی بابت سفارش"))
                holder.binding.transactionImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_negative_transaction, context.getApplicationContext().getTheme()));
            else holder.binding.transactionImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_positive_transaction, context.getApplicationContext().getTheme()));
        } else {
            if (currentActivity != null && currentActivity.getType().equals("کاهش موجودی بابت سفارش"))
                holder.binding.transactionImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_negative_transaction));
            else holder.binding.transactionImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_positive_transaction));
        }

    }

    private static DiffUtil.ItemCallback<WalletActivity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WalletActivity>() {
                @Override
                public boolean areItemsTheSame(@NonNull WalletActivity oldItem, @NonNull WalletActivity newItem) {
                    return Objects.equals(oldItem.getDate(), newItem.getDate());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull WalletActivity oldItem, @NonNull WalletActivity newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class OrdersViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewWalletActivityItemBinding binding;

        public OrdersViewHolder(@NonNull RecyclerviewWalletActivityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
