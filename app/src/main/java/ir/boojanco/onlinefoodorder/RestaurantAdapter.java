package ir.boojanco.onlinefoodorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private List<LastRestaurantList> restaurants;

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantBinding recyclerviewRestaurantBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.recyclerview_restaurant, parent, false);
        return new RestaurantViewHolder(recyclerviewRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        LastRestaurantList currentRestaurant = restaurants.get(position);
        holder.recyclerviewRestaurantBinding.setRestaurant(currentRestaurant);

    }

    @Override
    public int getItemCount() {
        if (restaurants != null) {
            return restaurants.size();
        } else {
            return 0;
        }
    }

    public void setRestaurantsList(List<LastRestaurantList> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantBinding recyclerviewRestaurantBinding; //this variable is from XML recyclerview_restauran
        public RestaurantViewHolder(@NonNull RecyclerviewRestaurantBinding recyclerviewRestaurantBinding) {
            super(recyclerviewRestaurantBinding.getRoot());
            this.recyclerviewRestaurantBinding = recyclerviewRestaurantBinding;
        }
    }
}
