package ir.boojanco.onlinefoodorder.data.DB;

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

    @ColumnInfo(name = "discountPercent")
    private int discountPercent;

    @ColumnInfo(name = "foodPrice")
    private int foodPrice;

    @ColumnInfo(name = "foodExtraPrice")
    private int foodExtraPrice;

    @ColumnInfo(name = "foodDiscountedPrice")
    private int foodDiscountedPrice;

    @ColumnInfo(name = "foodQuantity")
    private int foodQuantity ;

    public CartItem(long foodId, long restaurantId, String foodName, String foodImage, int discounttPercent, int foodPrice, int foodDiscountedPrice, int foodQuantity) {
        this.foodId = foodId;
        this.restaurantId = restaurantId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.discountPercent = discounttPercent;
        this.foodPrice = foodPrice;
        this.foodDiscountedPrice = foodDiscountedPrice;
        this.foodQuantity = foodQuantity;
    }

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

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodExtraPrice() {
        return foodExtraPrice;
    }

    public void setFoodExtraPrice(int foodExtraPrice) {
        this.foodExtraPrice = foodExtraPrice;
    }

    public int getFoodDiscountedPrice() {
        return foodDiscountedPrice;
    }

    public void setFoodDiscountedPrice(int foodDiscountedPrice) {
        this.foodDiscountedPrice = foodDiscountedPrice;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
