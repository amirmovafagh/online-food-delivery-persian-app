package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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


    public AddressAdapter(AddressRecyclerViewInterface clickListener, Context context) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.context = context;

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

        if (selectedPosition == position) {// is selected
            holder.binding.frameLayout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.cardview_border_restaurant_package_on_select, null));
        } else {//remove selected
            holder.binding.frameLayout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.color.white, null));
        }

            holder.binding.cvMainAddressLayout.setOnClickListener(v -> {
                //is select
                holder.binding.frameLayout.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.cardview_border_restaurant_package_on_select, null));
                clickListener.onRecyclerViewAddressClick(holder.binding.cvMainAddressLayout, currentAddress);
                selectedPosition = position;
                notifyDataSetChanged();
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
