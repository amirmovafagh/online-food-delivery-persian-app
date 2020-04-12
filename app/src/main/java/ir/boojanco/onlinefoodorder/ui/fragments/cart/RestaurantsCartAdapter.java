package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.RestaurantItem;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantsCartBinding;

class RestaurantsCartAdapter extends RecyclerView.Adapter<RestaurantsCartAdapter.RestaurantsCartViewHolder> {
    private static String TAG = RestaurantsCartAdapter.class.getSimpleName();
    private List<RestaurantItem> restaurantItems;
    public RecyclerViewCartClickListener cartClickListener;
    private CartDataSource cartDataSource;

    public RestaurantsCartAdapter(RecyclerViewCartClickListener cartClickListener, CartDataSource cartDataSource) {
        this.cartClickListener = cartClickListener;
        this.cartDataSource = cartDataSource;
    }

    @NonNull
    @Override
    public RestaurantsCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantsCartBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurants_cart, parent, false);
        return new RestaurantsCartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsCartViewHolder holder, int position) {

        RestaurantItem currentItem = restaurantItems.get(position);
        holder.binding.setRestaurantCart(currentItem);
        holder.binding.consLayout.setOnClickListener(v -> cartClickListener.onRecyclerViewRestaurantCartItemClick(holder.binding.consLayout, currentItem));
        holder.binding.removeCart.setOnClickListener(v -> {
            cleanRestaurantCartItemDatabase(currentItem, position);
        });
    }


    private void cleanRestaurantCartItemDatabase(RestaurantItem currentItem, int position) {
        cartDataSource.cleanRestaurantCart(currentItem.getRestaurantId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        if (restaurantItems != null) {
                            restaurantItems.remove(position);
                            notifyItemRemoved(position);
                            clearCart(currentItem.getRestaurantId());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "{DELETE Restaurant CART ITEM}-> " + e.getMessage());
                    }
                });
    }

    private void clearCart(long restaurantId){
        cartDataSource.cleanCart(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "{DELETE CART ITEM}-> " + e.getMessage());
                    }
                });
    }
    @Override
    public int getItemCount() {
        if (restaurantItems != null) {
            return restaurantItems.size();
        } else {
            return 0;
        }
    }

    public void setRestaurantItems(List<RestaurantItem> restaurantItems) {
        this.restaurantItems = restaurantItems;
        notifyDataSetChanged();

    }

    class RestaurantsCartViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRestaurantsCartBinding binding;

        public RestaurantsCartViewHolder(@NonNull RecyclerviewRestaurantsCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
