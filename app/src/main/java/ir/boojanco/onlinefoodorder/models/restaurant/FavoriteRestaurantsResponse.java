package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteRestaurantsResponse {
    @SerializedName("hits")
    private List<FavoriteRestaurants> favoriteRestaurantsList;

    @SerializedName("total")
    private int total;

    public List<FavoriteRestaurants> getFavoriteRestaurantsList() {
        return favoriteRestaurantsList;
    }

    public void setFavoriteRestaurantsList(List<FavoriteRestaurants> favoriteRestaurantsList) {
        this.favoriteRestaurantsList = favoriteRestaurantsList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
