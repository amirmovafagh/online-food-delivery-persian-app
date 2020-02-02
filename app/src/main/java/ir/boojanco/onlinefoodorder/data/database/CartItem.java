package ir.boojanco.onlinefoodorder.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class CartItem {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "foodId")
    private long foodId;

    @ColumnInfo(name = "restaurantId")
    private long restaurantId;

    @ColumnInfo(name = "foodName")
    private String foodName;

    @ColumnInfo(name = "foodImage")
    private String foodImage;

    @ColumnInfo(name = "foodPrice")
    private int foodPrice;

    @ColumnInfo(name = "foodQuantity")
    private int foodQuantity ;

    public CartItem() {}

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
