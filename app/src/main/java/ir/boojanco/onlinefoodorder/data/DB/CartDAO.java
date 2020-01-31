package ir.boojanco.onlinefoodorder.data.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {

    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void Delete(Cart cart);

    @Query("SELECT * FROM cart_table")
    Flowable<Cart> getCartItems();

    @Query("SELECT * FROM cart_table WHERE foodId=:foodItemId")
    Flowable<Cart> getCartItemById(long foodItemId);

    @Query("SELECT * FROM cart_table WHERE restaurantId=:restaurantId")
    Flowable<Cart> getAllCartById(long restaurantId);

    @Query("SELECT COUNT(*) FROM cart_table WHERE restaurantId=:restaurantId")
    Single<Integer> countItemInCart(long restaurantId);

    @Query("SELECT COUNT(*) from cart_table")
    int countCartItems();

    @Query("DELETE FROM cart_table")
    void emptyCart();

}
