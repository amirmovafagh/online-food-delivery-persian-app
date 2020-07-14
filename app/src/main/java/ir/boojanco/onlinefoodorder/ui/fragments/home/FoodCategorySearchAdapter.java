package ir.boojanco.onlinefoodorder.ui.fragments.home;

import android.content.Context;
import android.os.Build;
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
    Context context;
    FoodCategorySearchInterface rvInterface;

    int categoryIcons[] = {R.drawable.ic_fast_food, R.drawable.ic_burger, R.drawable.ic_pizza, R.drawable.ic_sandwich,
            R.drawable.ic_hotdog, R.drawable.ic_fried_chicken, R.drawable.ic_rice, R.drawable.ic_coffee, R.drawable.ic_chinese_food,
            R.drawable.ic_italian_food, R.drawable.ic_kebab, R.drawable.ic_vegetables_food, R.drawable.ic_seafood, R.drawable.ic_desert};

    public FoodCategorySearchAdapter(Context context, FoodCategorySearchInterface rvInterface) {
        this.rvInterface = rvInterface;
        this.context = context;
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
        if (position <= 13)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
                holder.binding.imgFood.setImageDrawable(context.getResources().getDrawable(categoryIcons[position], context.getApplicationContext().getTheme()));
            } else {
                holder.binding.imgFood.setImageDrawable(context.getResources().getDrawable(categoryIcons[position]));
            }
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
