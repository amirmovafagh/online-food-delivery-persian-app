package ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFavoriteRestaurantsItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurants;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsRecyclerViewInterface;


public class FavoriteRestaurantsAdapter extends PagedListAdapter<FavoriteRestaurants, FavoriteRestaurantsAdapter.FavoriteRestaurantsViewHolder> {

    private FavoriteRestaurantsRecyclerViewInterface clickListener;


    public FavoriteRestaurantsAdapter(FavoriteRestaurantsRecyclerViewInterface clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoriteRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewFavoriteRestaurantsItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_favorite_restaurants_item, parent, false);
        return new FavoriteRestaurantsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRestaurantsViewHolder holder, int position) {
        FavoriteRestaurants currentRestaurant = getItem(position);
        holder.binding.textViewTaglist.setText(getTagList(currentRestaurant.getTagList()));
        holder.binding.setRestaurant(currentRestaurant);
        holder.binding.cvRestauranDetails.setOnClickListener(v -> clickListener.onRecyclerViewFaveRestaurantClick(v, currentRestaurant));
    }

    public String getTagList(List<String> tagList) {
        StringBuilder tagListString = new StringBuilder();
        tagListString.append(" ");
        if (tagList != null)
            /*for(String t : tagList){
                tagListString.append("(").append(t).append(")").append(" ");
            }*/ for (int i = 0; i < tagList.size(); i++) {
            String t = tagList.get(i);
            if (i == tagList.size() - 1) {
                tagListString.append(t);
            } else {
                tagListString.append(t).append(" • ");
            }

        }
        return tagListString.toString();
    }

    private static DiffUtil.ItemCallback<FavoriteRestaurants> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FavoriteRestaurants>() {
                @Override
                public boolean areItemsTheSame(@NonNull FavoriteRestaurants oldItem, @NonNull FavoriteRestaurants newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull FavoriteRestaurants oldItem, @NonNull FavoriteRestaurants newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class FavoriteRestaurantsViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewFavoriteRestaurantsItemBinding binding;

        public FavoriteRestaurantsViewHolder(@NonNull RecyclerviewFavoriteRestaurantsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
