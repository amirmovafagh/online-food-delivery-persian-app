package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeBinding;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantFoodMenuViewModel;

public class RestaurantFoodTypeAdapter extends RecyclerView.Adapter<RestaurantFoodTypeAdapter.FoodTypeViewHolder> {
    private static final String TAG = RestaurantFoodTypeAdapter.class.getSimpleName();
    private List<String> foodTypeLists;
    public RecyclerViewRestaurantFoodTypeClickListener clickListener;

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int selectedPosition = 0;


    public RestaurantFoodTypeAdapter(RecyclerViewRestaurantFoodTypeClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public FoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFoodTypeBinding recyclerviewFoodTypeBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_type, parent, false);
        return new FoodTypeViewHolder(recyclerviewFoodTypeBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodTypeViewHolder holder, int position) {
        holder.recyclerviewFoodTypeBinding.setFoodType(foodTypeLists.get(position));

        holder.recyclerviewFoodTypeBinding.radioBt.setChecked(selectedPosition == position);
        Log.i(TAG,"position: "+position);
        holder.recyclerviewFoodTypeBinding.tvNameType.setOnClickListener(v -> {
            holder.recyclerviewFoodTypeBinding.radioBt.setChecked(true);
            clickListener.onRecyclerViewTypeItemClick(holder.recyclerviewFoodTypeBinding.tvNameType, foodTypeLists.get(position));
            selectedPosition=position;
            notifyDataSetChanged();
        });


    }


    @Override
    public int getItemCount() {
        if (foodTypeLists != null) {
            return foodTypeLists.size();
        } else {
            return 0;
        }
    }

    public void setFoodTypeLists(List<String> foodTypeLists) {
        this.foodTypeLists = foodTypeLists;
        notifyDataSetChanged();
    }

    class FoodTypeViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewFoodTypeBinding recyclerviewFoodTypeBinding;

        public FoodTypeViewHolder(@NonNull RecyclerviewFoodTypeBinding recyclerviewFoodTypeBinding) {
            super(recyclerviewFoodTypeBinding.getRoot());
            this.recyclerviewFoodTypeBinding = recyclerviewFoodTypeBinding;
        }
    }
}
