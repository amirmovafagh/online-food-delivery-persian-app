package ir.boojanco.onlinefoodorder.ui.fragments.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodCategorySearchItemBinding;
import ir.boojanco.onlinefoodorder.models.food.FoodCategories;

public class FoodCategorySearchAdapter extends RecyclerView.Adapter<FoodCategorySearchAdapter.FoodCategorySearchAdapterViewHolder> {

    List<FoodCategories> foodCategories;
    FoodCategorySearchInterface rvInterface;

    public FoodCategorySearchAdapter(FoodCategorySearchInterface rvInterface) {
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public FoodCategorySearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFoodCategorySearchItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_category_search_item, parent, false);
        return new FoodCategorySearchAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategorySearchAdapterViewHolder holder, int position) {
        FoodCategories currentItem = foodCategories.get(position);
        holder.binding.setItem(currentItem);
        holder.binding.cardView.setOnClickListener(v -> rvInterface.onCategoryClick(currentItem.getName()));
    }

    public void setFoodCategoryItems(List<FoodCategories> foodCategories) {
        this.foodCategories = foodCategories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (foodCategories != null) {
            return foodCategories.size();
        } else {
            return 0;
        }
    }

    class FoodCategorySearchAdapterViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewFoodCategorySearchItemBinding binding;

        public FoodCategorySearchAdapterViewHolder(@NonNull RecyclerviewFoodCategorySearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
