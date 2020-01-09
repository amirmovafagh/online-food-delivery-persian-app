package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeBinding;

public class RestaurantFoodTypeAdapter extends RecyclerView.Adapter<RestaurantFoodTypeAdapter.FoodTypeViewHolder> {
    private List<String> foodTypeLists;
    public RecyclerViewRestaurantFoodTypeClickListener clickListener;

    public RestaurantFoodTypeAdapter(RecyclerViewRestaurantFoodTypeClickListener clickListener){
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
        holder.recyclerviewFoodTypeBinding.tvNameType.setOnClickListener(v->{
            clickListener.onRecyclerViewTypeItemClick(holder.recyclerviewFoodTypeBinding.tvNameType, foodTypeLists.get(position));
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

    class FoodTypeViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewFoodTypeBinding recyclerviewFoodTypeBinding;
        public FoodTypeViewHolder(@NonNull RecyclerviewFoodTypeBinding recyclerviewFoodTypeBinding) {
            super(recyclerviewFoodTypeBinding.getRoot());
            this.recyclerviewFoodTypeBinding = recyclerviewFoodTypeBinding;
        }
    }
}
