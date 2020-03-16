package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantCommentItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantComments;

public class RestaurantCommentAdapter extends PagedListAdapter<RestaurantComments, RestaurantCommentAdapter.CommentViewHolder> {

    public RestaurantCommentAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantCommentItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_comment_item, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        RestaurantComments currentComment = getItem(position);
        holder.binding.setCommentItem(currentComment);
    }

    private static DiffUtil.ItemCallback<RestaurantComments> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RestaurantComments>() {
                @Override
                public boolean areItemsTheSame(@NonNull RestaurantComments oldItem, @NonNull RestaurantComments newItem) {
                    return oldItem.getCommentId() == newItem.getCommentId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull RestaurantComments oldItem, @NonNull RestaurantComments newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class CommentViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRestaurantCommentItemBinding binding;

        public CommentViewHolder(@NonNull RecyclerviewRestaurantCommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
