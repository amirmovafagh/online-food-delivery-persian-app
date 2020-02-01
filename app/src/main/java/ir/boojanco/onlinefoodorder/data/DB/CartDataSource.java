package ir.boojanco.onlinefoodorder.data.DB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {
    Flowable<List<CartItem>> getAllCart(long restaurantId);

    Single<Integer> countItemInCart(long restaurantId);

    Single<Long> sumPrice(long restaurantId);

    Single<CartItem> getItemInCart(long foodId, long restaurantId);

    Completable insertOrReplaceAll(CartItem...cartItems);

    Single<Integer> updateCart(CartItem cart);

    Single<Integer> deleteCart(CartItem cart);

    Single<Integer> cleanCart(long restaurantId);

}
