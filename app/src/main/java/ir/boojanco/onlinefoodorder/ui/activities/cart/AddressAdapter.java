package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserAddressItemBinding;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<UserAddressList> addressLists;
    private int selectedPosition = 100000;
    public RecyclerViewCartClickListener clickListener;
    private Context context;

    public AddressAdapter(RecyclerViewCartClickListener clickListener,Context context) {
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserAddressItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_address_item , parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddressList currentAddress = addressLists.get(position);
        holder.binding.setAddressList(currentAddress);

        if (selectedPosition == position) {// is selected
            holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        } else {//remove selected
            holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.binding.cvMainAddressLayout.setOnClickListener(v -> {
            //is select
            holder.binding.cvMainAddressLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            clickListener.onRecyclerViewAddressClick(holder.binding.cvMainAddressLayout,currentAddress);
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (addressLists != null) {
            return addressLists.size();
        } else {
            return 0;
        }
    }

    public void setAddressLists(List<UserAddressList> addressLists){
        this.addressLists = addressLists;
        notifyDataSetChanged();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewUserAddressItemBinding binding;
        public AddressViewHolder(@NonNull RecyclerviewUserAddressItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
