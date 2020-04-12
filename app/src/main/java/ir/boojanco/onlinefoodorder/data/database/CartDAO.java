package ir.boojanco.onlinefoodorder.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface CartDAO {

    @Query("SELECT * FROM cart_table WHERE restaurantId=:restaurantId")
    Flowable<List<CartItem>> getAllCart(long restaurantId);

    @Query("SELECT COUNT(*) FROM cart_table WHERE restaurantId=:restaurantId")
    Single<Integer> countItemInCart(long restaurantId);

    @Query("SELECT SUM(foodPrice*foodQuantity) FROM cart_table WHERE restaurantId=:restaurantId")
    Single<Long> sumPrice(long restaurantId);

    @Query("SELECT * FROM cart_table WHERE foodId=:foodId AND restaurantId=:restaurantId")
    Single<CartItem> getItemInCart(long foodId, long restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //if conflict foodId, we will update information
    Completable insertOrReplaceAll(CartItem... cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCart(CartItem cart);

    @Delete
    Single<Integer> deleteCart(CartItem cart); //delete one row from table

    @Query("DELETE FROM cart_table WHERE restaurantId=:restaurantId")
    Single<Integer> cleanCart(long restaurantId);


    //Restaurant_table queries

    @Query("SELECT * FROM restaurant_table")
    Flowable<List<RestaurantItem>> getAllRestaurantsCart();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //if conflict restaurantId, we will update information
    Completable insertOrReplaceAllRestaurants(RestaurantItem... restaurantItems);

    @Query("DELETE FROM restaurant_table WHERE restaurantId=:restaurantId")
    Single<Integer> cleanRestaurantCart(long restaurantId);

}
