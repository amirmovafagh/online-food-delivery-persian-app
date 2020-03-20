package ir.boojanco.onlinefoodorder.ui.fragments.userProfile;

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
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserProfileAddressItemBinding;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;

public class AddressProfileAdapter extends PagedListAdapter<UserAddressList, AddressProfileAdapter.AddressViewHolder> {
    //private List<UserAddressList> addressLists;
    private int selectedPosition = 100000;
    public AddressRecyclerViewInterface clickListener;
    private Context context;

    public AddressProfileAdapter(AddressRecyclerViewInterface clickListener, Context context) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserProfileAddressItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_profile_address_item, parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddressList currentAddress = getItem(position);
        holder.binding.setAddressList(currentAddress);

        holder.binding.imgEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRecyclerViewAddressClick(holder.binding.imgEditAddress, currentAddress, position);
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
        private RecyclerviewUserProfileAddressItemBinding binding;

        public AddressViewHolder(@NonNull RecyclerviewUserProfileAddressItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
