package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private List<LastRestaurantList> restaurants;
    public RecyclerViewRestaurantClickListener clickListener;

    public RestaurantAdapter(RecyclerViewRestaurantClickListener clickListener){
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
        LastRestaurantList currentRestaurant = restaurants.get(position);
        holder.binding.setRestaurant(currentRestaurant);
        holder.binding.toggleBookmark.setOnClickListener(v -> {
            clickListener.onRecyclerViewItemClick(holder.binding.toggleBookmark, restaurants.get(position));
        });
        holder.binding.consLayout.setOnClickListener(v ->{
            clickListener.onRecyclerViewItemClick(holder.binding.consLayout, restaurants.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (restaurants != null) {
            return restaurants.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return restaurants.get(position).getId();
    }

    public void setRestaurantsList(List<LastRestaurantList> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();

    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantItemBinding binding; //this variable is from XML recyclerview_restauran
        public RestaurantViewHolder(@NonNull RecyclerviewRestaurantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
