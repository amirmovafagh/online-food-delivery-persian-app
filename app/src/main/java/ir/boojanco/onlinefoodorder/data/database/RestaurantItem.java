package ir.boojanco.onlinefoodorder.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Entity(tableName = "restaurant_table")
public class RestaurantItem {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "restaurantId")
    private long restaurantId;

    @ColumnInfo(name = "restaurantName")
    private String restaurant;

    @ColumnInfo(name = "restaurantLogo")
    private String restaurantLogo;

    @ColumnInfo(name = "restaurantCover")
    private String restaurantCover;


    public RestaurantItem() {
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }

    public String getRestaurantCover() {
        return restaurantCover;
    }

    public void setRestaurantCover(String restaurantCover) {
        this.restaurantCover = restaurantCover;
    }
}
