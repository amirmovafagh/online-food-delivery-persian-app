package ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFavoriteFoodsItemBinding;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteFoodsRecyclerViewInterface;


public class FavoriteFoodsAdapter extends PagedListAdapter<FavoriteFoods, FavoriteFoodsAdapter.FavoriteFoodsViewHolder> {

    private FavoriteFoodsRecyclerViewInterface clickListener;


    public FavoriteFoodsAdapter(FavoriteFoodsRecyclerViewInterface clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoriteFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFavoriteFoodsItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_favorite_foods_item, parent, false);
        return new FavoriteFoodsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteFoodsViewHolder holder, int position) {
        FavoriteFoods currentFood = getItem(position);
        holder.binding.setFoodItem(currentFood);
        holder.binding.cvFoodDetails.setOnClickListener(v -> clickListener.onRecyclerViewFaveFoodsClick(v, currentFood));
    }

    private static DiffUtil.ItemCallback<FavoriteFoods> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FavoriteFoods>() {
                @Override
                public boolean areItemsTheSame(@NonNull FavoriteFoods oldItem, @NonNull FavoriteFoods newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull FavoriteFoods oldItem, @NonNull FavoriteFoods newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class FavoriteFoodsViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewFavoriteFoodsItemBinding binding;

        public FavoriteFoodsViewHolder(@NonNull RecyclerviewFavoriteFoodsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
