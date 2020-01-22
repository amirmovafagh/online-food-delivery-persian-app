package ir.boojanco.onlinefoodorder.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeSearchFilterItemBinding;

public class FoodTypeSearchFilterAdapter extends RecyclerView.Adapter<FoodTypeSearchFilterAdapter.FoodTypeSearchFilterViewHolder> {

    ArrayList<FoodTypeFilterItem> foodTypeFilterItems;


    @NonNull
    @Override
    public FoodTypeSearchFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFoodTypeSearchFilterItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_type_search_filter_item,parent, false);
        return new FoodTypeSearchFilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodTypeSearchFilterViewHolder holder, int position) {
        holder.binding.setText(foodTypeFilterItems.get(position).getName());
        holder.binding.imageViewFoodTypeFilterPic.setImageResource(foodTypeFilterItems.get(position).getPic());
    }

    public void setFoodTypeFilterItems(ArrayList<FoodTypeFilterItem> foodTypeFilterItems){
        this.foodTypeFilterItems = foodTypeFilterItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (foodTypeFilterItems != null) {
            return foodTypeFilterItems.size();
        } else {
            return 0;
        }
    }

    class FoodTypeSearchFilterViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewFoodTypeSearchFilterItemBinding binding;
        public FoodTypeSearchFilterViewHolder(@NonNull RecyclerviewFoodTypeSearchFilterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
