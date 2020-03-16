package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRestaurantCommentResponse {
    @SerializedName("hits")
    private List<RestaurantComments> restaurantComments;
    @SerializedName("total")
    private int total;

    public List<RestaurantComments> getRestaurantComments() {
        return restaurantComments;
    }

    public void setRestaurantComments(List<RestaurantComments> restaurantComments) {
        this.restaurantComments = restaurantComments;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
