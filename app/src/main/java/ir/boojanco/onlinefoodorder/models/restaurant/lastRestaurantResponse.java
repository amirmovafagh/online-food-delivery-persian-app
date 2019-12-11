package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class lastRestaurantResponse {
    @SerializedName("hits")
    private List<LastRestaurantList> restaurantsList= null;
    @SerializedName("total")
    private int total;

    public List<LastRestaurantList> getRestaurantsList() {
        return restaurantsList;
    }

    public int getTotal() {
        return total;
    }
}
