package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeHeaderBinding;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantFoodMenuBinding;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.FoodItem;

public class RestaurantFoodMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    //private List<AllFoodList> foodLists;
    private ArrayList<ListItemType> items;
    private FoodTypeHeader header;
    private CartDataSource cartDataSource;

    private Long extraRestaurantId;
    public RecyclerViewRestaurantFoodMenuClickListener clickListener;

    public RestaurantFoodMenuAdapter(RecyclerViewRestaurantFoodMenuClickListener clickListener, CartDataSource cartDataSource, Long extraRestaurantId){
        this.clickListener = clickListener;
        this.cartDataSource =cartDataSource;
        this.extraRestaurantId = extraRestaurantId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ListItemType.TYPE_HEADER){
            RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_type_header, parent, false);
            return new RestaurantFoodHeaderViewHolder(recyclerviewFoodTypeHeaderBinding);
        }else if(viewType == ListItemType.TYPE_ITEM){
            RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodMenuBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_food_menu, parent, false);
            return new RestaurantFoodViewHolder(recyclerviewRestaurantFoodMenuBinding);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof RestaurantFoodHeaderViewHolder){
            header = (FoodTypeHeader) items.get(position);
            ((RestaurantFoodHeaderViewHolder) holder).recyclerviewFoodTypeHeaderBinding.setHeader(header);
        }else if(holder instanceof RestaurantFoodViewHolder){
            FoodItem foodItem = (FoodItem) items.get(position);

            ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.setFoodItem(foodItem);
            getFoodByIdFromDB(foodItem,extraRestaurantId, (RestaurantFoodViewHolder) holder,false,false);
            ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.imgBtnIncrease.setOnClickListener(v -> {
                TextView textViewItems = ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.textViewItemCount;
                int countItem = Integer.parseInt(textViewItems.getText().toString());

                if(countItem == 0){
                    Log.i(TAG,"AA");
                    clickListener.onRecyclerViewItemClick(position,((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.imgBtnIncrease, foodItem);
                    getFoodByIdFromDB(foodItem,extraRestaurantId, (RestaurantFoodViewHolder) holder,false,false); //just for update counter
                }else if(Integer.parseInt(textViewItems.getText().toString()) < 99){ //increase
                    getFoodByIdFromDB(foodItem,extraRestaurantId, (RestaurantFoodViewHolder) holder,true,false); //update DB and Counter
                }
            });
            ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.imgBtnDecrease.setOnClickListener(v -> {
                TextView textViewItems = ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.textViewItemCount;
                int countItem = Integer.parseInt(textViewItems.getText().toString());
                if(countItem == 1){
                    getFoodByIdFromDB(foodItem, extraRestaurantId, (RestaurantFoodViewHolder) holder,false,true);
                }else if(countItem > 1)
                    getFoodByIdFromDB(foodItem, extraRestaurantId, (RestaurantFoodViewHolder) holder,false,true);
            });

            if(foodItem.getDiscount()>0){
                TextView textViewCostStrikeThrough  = ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.textViewPrice;
                textViewCostStrikeThrough.setPaintFlags(textViewCostStrikeThrough.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

    }


    private void getFoodByIdFromDB(FoodItem currentFood, long extraRestaurantId,RestaurantFoodViewHolder holder, boolean isIncrease,boolean isDecrease){
                cartDataSource.getItemInCart(currentFood.getId(),extraRestaurantId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CartItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(CartItem cartItem) {
                        if(cartItem.getFoodQuantity() == 1)
                            holder.recyclerviewRestaurantFoodMenuBinding.textViewItemCount.setText(String.valueOf((cartItem.getFoodQuantity())));
                        if(isIncrease && cartItem.getFoodQuantity() < 99) {
                            cartItem.setFoodQuantity(cartItem.getFoodQuantity() + 1);
                            updateCartItemDatabase(cartItem,holder);
                        }
                        if(isDecrease && cartItem.getFoodQuantity() == 1){
                            deleteCartItemDatabase(cartItem,holder,0);
                        }
                        else if(isDecrease && cartItem.getFoodQuantity() > 1 )
                            cartItem.setFoodQuantity(cartItem.getFoodQuantity() - 1);
                            updateCartItemDatabase(cartItem,holder);
                    }

                    @Override
                    public void onError(Throwable e) {
                        holder.recyclerviewRestaurantFoodMenuBinding.textViewItemCount.setText("0");
                        Log.e(TAG,"{add Cart throwable}->"+e.getMessage());
                    }
                });
    }
    private void updateCartItemDatabase(CartItem cartItem,RestaurantFoodViewHolder holder){
        cartDataSource.updateCart(cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        holder.recyclerviewRestaurantFoodMenuBinding.textViewItemCount.setText(String.valueOf(cartItem.getFoodQuantity()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"{UPDATE CART ITEM}-> "+e.getMessage());
                    }
                });
    }

    private void deleteCartItemDatabase(CartItem currentItem,RestaurantFoodViewHolder holder, int num){
        cartDataSource.deleteCart(currentItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        holder.recyclerviewRestaurantFoodMenuBinding.textViewItemCount.setText(String.valueOf(num));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"{DELETE CART ITEM}-> "+e.getMessage());
                    }
                });
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }


    public void setFoodLists(ArrayList<ListItemType> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class RestaurantFoodViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodMenuBinding; //this variable is from XML recyclerview_restauran_food
        public RestaurantFoodViewHolder(@NonNull RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodBinding) {
            super(recyclerviewRestaurantFoodBinding.getRoot());
            this.recyclerviewRestaurantFoodMenuBinding = recyclerviewRestaurantFoodBinding;
        }
    }
    class RestaurantFoodHeaderViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding; //this variable is from XML recyclerview_food_type_header
        public RestaurantFoodHeaderViewHolder(@NonNull RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding) {
            super(recyclerviewFoodTypeHeaderBinding.getRoot());
            this.recyclerviewFoodTypeHeaderBinding = recyclerviewFoodTypeHeaderBinding;
        }
    }
}
