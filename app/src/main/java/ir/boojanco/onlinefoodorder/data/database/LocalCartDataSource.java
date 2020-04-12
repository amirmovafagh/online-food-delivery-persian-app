package ir.boojanco.onlinefoodorder.data.database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {
    private CartDAO cartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }


    @Override
    public Flowable<List<CartItem>> getAllCart(long restaurantId) {
        return cartDAO.getAllCart(restaurantId);
    }

    @Override
    public Single<Integer> countItemInCart(long restaurantId) {
        return cartDAO.countItemInCart(restaurantId);
    }

    @Override
    public Single<Long> sumPrice(long restaurantId) {
        return cartDAO.sumPrice(restaurantId);
    }

    @Override
    public Single<CartItem> getItemInCart(long foodId, long restaurantId) {
        return cartDAO.getItemInCart(foodId,restaurantId);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCart(CartItem cart) {
        return cartDAO.updateCart(cart);
    }

    @Override
    public Single<Integer> deleteCart(CartItem cart) {
        return cartDAO.deleteCart(cart);
    }

    @Override
    public Single<Integer> cleanCart(long restaurantId) {
        return cartDAO.cleanCart(restaurantId);
    }

    @Override
    public Flowable<List<RestaurantItem>> getAllRestaurantsCart() {
        return cartDAO.getAllRestaurantsCart();
    }

    @Override
    public Completable insertOrReplaceAllRestaurants(RestaurantItem... restaurantItems) {
        return cartDAO.insertOrReplaceAllRestaurants(restaurantItems);
    }

    @Override
    public Single<Integer> cleanRestaurantCart(long restaurantId) {
        return cartDAO.cleanRestaurantCart(restaurantId);
    }
}
