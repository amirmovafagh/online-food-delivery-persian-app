package ir.boojanco.onlinefoodorder.data.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {

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

    @ColumnInfo(name = "discounttPercent")
    private int discounttPercent;

    @ColumnInfo(name = "foodPrice")
    private int foodPrice;

    @ColumnInfo(name = "foodDiscountedPrice")
    private int foodDiscountedPrice;

    @ColumnInfo(name = "foodQuantity")
    private int foodQuantity ;

    public Cart(long foodId, long restaurantId, String foodName, String foodImage, int discounttPercent, int foodPrice, int foodDiscountedPrice, int foodQuantity) {
        this.foodId = foodId;
        this.restaurantId = restaurantId;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.discounttPercent = discounttPercent;
        this.foodPrice = foodPrice;
        this.foodDiscountedPrice = foodDiscountedPrice;
        this.foodQuantity = foodQuantity;
    }
}
