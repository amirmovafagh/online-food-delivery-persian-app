package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewCartItemBinding;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static String TAG = CartAdapter.class.getSimpleName();
    private List<CartItem> cartItems;
    public RecyclerViewCartClickListener cartClickListener;
    private CartDataSource cartDataSource;

    public CartAdapter(RecyclerViewCartClickListener cartClickListener, CartDataSource cartDataSource) {
        this.cartClickListener = cartClickListener;
        this.cartDataSource = cartDataSource;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewCartItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_cart_item, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartItem currentItem = cartItems.get(position);
        holder.binding.setCartItem(currentItem);
        holder.binding.imgBtnIncrease.setOnClickListener(v ->{
            //if increase food number
            if(currentItem.getFoodQuantity() < 99){
                currentItem.setFoodQuantity(currentItem.getFoodQuantity()+1);
                updateCartDatabase(holder, currentItem);
            }

        });
        holder.binding.imgBtnDecrease.setOnClickListener(v ->{
            //if Decrease food number
            if(currentItem.getFoodQuantity() > 1) {
                currentItem.setFoodQuantity(currentItem.getFoodQuantity() - 1);
                updateCartDatabase(holder, currentItem);
            }
        });
    }

    private void updateCartDatabase(CartViewHolder holder, CartItem currentItem){
        cartDataSource.updateCart(currentItem)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                if(holder != null)
                    holder.binding.setCartItem(currentItem);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"{UPDATE CART}-> "+e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartItems != null) {
            return cartItems.size();
        } else {
            return 0;
        }
    }

    public void setCartItems(List<CartItem> cartItems){
        this.cartItems = cartItems;
        notifyDataSetChanged();

    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewCartItemBinding binding;
        public CartViewHolder(@NonNull RecyclerviewCartItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
