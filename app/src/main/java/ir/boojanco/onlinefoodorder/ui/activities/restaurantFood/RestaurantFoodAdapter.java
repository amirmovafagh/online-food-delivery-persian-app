package ir.boojanco.onlinefoodorder.ui.activities.restaurantFood;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantFoodBinding;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;

public class RestaurantFoodAdapter extends RecyclerView.Adapter<RestaurantFoodAdapter.RestaurantFoodViewHolder> {
    private List<AllFoodList> foodLists;
    public RecyclerViewRestaurantFoodClickListener clickListener;

    public RestaurantFoodAdapter(RecyclerViewRestaurantFoodClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RestaurantFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantFoodBinding recyclerviewRestaurantFoodBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_food, parent, false);
        return new RestaurantFoodViewHolder(recyclerviewRestaurantFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantFoodViewHolder holder, int position) {
        AllFoodList currentFoodList= foodLists.get(position);
        holder.recyclerviewRestaurantFoodBinding.setAllFood(currentFoodList);
        holder.recyclerviewRestaurantFoodBinding.layoutLinear.setOnClickListener(v -> {
            clickListener.onRecyclerViewItemClick(holder.recyclerviewRestaurantFoodBinding.layoutLinear, foodLists.get(position));
        });
        holder.recyclerviewRestaurantFoodBinding.ivPic.setOnClickListener(v ->{
            clickListener.onRecyclerViewItemClick(holder.recyclerviewRestaurantFoodBinding.ivPic, foodLists.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (foodLists != null) {
            return foodLists.size();
        } else {
            return 0;
        }
    }

    public void setFoodLists(List<AllFoodList> foodLists) {
        this.foodLists = foodLists;
        notifyDataSetChanged();
    }

    class RestaurantFoodViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantFoodBinding recyclerviewRestaurantFoodBinding; //this variable is from XML recyclerview_restauran_food
        public RestaurantFoodViewHolder(@NonNull RecyclerviewRestaurantFoodBinding recyclerviewRestaurantFoodBinding) {
            super(recyclerviewRestaurantFoodBinding.getRoot());
            this.recyclerviewRestaurantFoodBinding = recyclerviewRestaurantFoodBinding;
        }
    }
}
