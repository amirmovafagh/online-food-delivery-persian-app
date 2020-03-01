package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewUserOrdersFoodItemsBinding;
import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private String TAG = FoodAdapter.class.getSimpleName();
    List<OrderFoodList> foodLists;


    public FoodAdapter() {

    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserOrdersFoodItemsBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_orders_food_items, parent, false);
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        OrderFoodList currentFood = foodLists.get(position);
        holder.binding.setFoodItem(currentFood);
    }

    @Override
    public int getItemCount() {
        if (foodLists != null) {
            return foodLists.size();
        } else {
            return 0;
        }
    }

    public void setFoodLists(List<OrderFoodList> foodLists) {

        this.foodLists = foodLists;
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewUserOrdersFoodItemsBinding binding;

        public FoodViewHolder(@NonNull RecyclerviewUserOrdersFoodItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
