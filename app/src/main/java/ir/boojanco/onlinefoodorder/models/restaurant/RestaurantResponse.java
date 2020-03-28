package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class RestaurantResponse {
    @SerializedName("hits")
    private List<RestaurantList> restaurantsList= null;
    @SerializedName("total")
    private int total;

    public List<RestaurantList> getRestaurantsList() {
        return restaurantsList;
    }

    public int getTotal() {
        return total;
    }
}
