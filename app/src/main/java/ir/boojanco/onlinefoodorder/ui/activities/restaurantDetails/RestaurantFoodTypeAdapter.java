package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeItemBinding;

public class RestaurantFoodTypeAdapter extends RecyclerView.Adapter<RestaurantFoodTypeAdapter.FoodTypeViewHolder> {
    private static final String TAG = RestaurantFoodTypeAdapter.class.getSimpleName();
    private List<String> foodTypeLists;
    private Context context;
    public RecyclerViewRestaurantFoodTypeClickListener clickListener;

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int selectedPosition = 0;


    public RestaurantFoodTypeAdapter(RecyclerViewRestaurantFoodTypeClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public FoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFoodTypeItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_type_item, parent, false);
        return new FoodTypeViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodTypeViewHolder holder, int position) {
        holder.binding.setFoodType(foodTypeLists.get(position));
        if (selectedPosition == position) {// is selected
            holder.binding.cvFoodTypeTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.tvNameType.setTextColor(context.getResources().getColor(R.color.white));
        } else {//remove selected
            holder.binding.cvFoodTypeTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.binding.tvNameType.setTextColor(context.getResources().getColor(R.color.gray));
        }

        holder.binding.tvNameType.setOnClickListener(v -> {
            //is select
            holder.binding.cvFoodTypeTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.tvNameType.setTextColor(context.getResources().getColor(R.color.white));
            clickListener.onRecyclerViewTypeItemClick(holder.binding.tvNameType, foodTypeLists.get(position));
            selectedPosition = position;
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
        private RecyclerviewFoodTypeItemBinding binding;

        public FoodTypeViewHolder(@NonNull RecyclerviewFoodTypeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
