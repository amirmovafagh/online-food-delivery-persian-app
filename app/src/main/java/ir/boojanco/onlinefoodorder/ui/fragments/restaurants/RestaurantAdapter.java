package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;

public class RestaurantAdapter extends PagedListAdapter<RestaurantList, RestaurantAdapter.RestaurantViewHolder> {
    //private List<LastRestaurantList> restaurants;
    public RecyclerViewRestaurantClickListener clickListener;

    protected RestaurantAdapter(RecyclerViewRestaurantClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_item, parent, false);
        return new RestaurantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantList currentRestaurant = getItem(position);
        if (currentRestaurant != null) {
            holder.binding.setViewModel(currentRestaurant);
            /*holder.binding.toggleBookmark.setOnClickListener(v -> {
                clickListener.onRecyclerViewItemClick(holder.binding.toggleBookmark, currentRestaurant);
            });*/
            holder.binding.consLayout.setOnClickListener(v -> {
                clickListener.onRecyclerViewItemClick(holder.binding.consLayout, currentRestaurant);
            });
        }


    }

    private static DiffUtil.ItemCallback<RestaurantList> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RestaurantList>() {
                @Override
                public boolean areItemsTheSame(@NonNull RestaurantList oldItem, @NonNull RestaurantList newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull RestaurantList oldItem, @NonNull RestaurantList newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRestaurantItemBinding binding; //this variable is from XML recyclerview_restauran

        public RestaurantViewHolder(@NonNull RecyclerviewRestaurantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
