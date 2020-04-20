package ir.boojanco.onlinefoodorder.ui.fragments.cart;

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

import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserAddressItemBinding;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;

public class AddressAdapter extends PagedListAdapter<UserAddressList, AddressAdapter.AddressViewHolder> {
    //private List<UserAddressList> addressLists;
    private int selectedPosition = 100000;
    public AddressRecyclerViewInterface clickListener;
    private Context context;
    private boolean runFromCartActivity = false;

    public AddressAdapter(AddressRecyclerViewInterface clickListener, Context context, boolean runFromCartActivity) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.context = context;
        this.runFromCartActivity = runFromCartActivity;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserAddressItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_address_item, parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddressList currentAddress = getItem(position);
        holder.binding.setAddressList(currentAddress);
        if (runFromCartActivity)
            holder.binding.linearLayoutControlButtons.setVisibility(View.GONE);
        if (selectedPosition == position && runFromCartActivity) {// is selected
            holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        } else {//remove selected
            holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (runFromCartActivity)
            holder.binding.cvMainAddressLayout.setOnClickListener(v -> {
                //is select
                holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                clickListener.onRecyclerViewAddressClick(holder.binding.cvMainAddressLayout, currentAddress);
                selectedPosition = position;
                notifyDataSetChanged();
            });

        holder.binding.imgEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRecyclerViewAddressClick(holder.binding.imgEditAddress, currentAddress);
            }
        });
    }

    private static DiffUtil.ItemCallback<UserAddressList> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<UserAddressList>() {
                @Override
                public boolean areItemsTheSame(@NonNull UserAddressList oldItem, @NonNull UserAddressList newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull UserAddressList oldItem, @NonNull UserAddressList newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class AddressViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewUserAddressItemBinding binding;

        public AddressViewHolder(@NonNull RecyclerviewUserAddressItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
