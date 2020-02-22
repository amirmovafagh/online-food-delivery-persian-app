package ir.boojanco.onlinefoodorder.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Entity(tableName = "cart_table")
public class CartItem implements Serializable {

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

    @ColumnInfo(name = "foodDiscount")
    private int foodDiscount= 0;

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

    public String getFoodPriceMoneyFormat(){
        int i =foodPrice*foodQuantity;
        return moneyFormat(i);
    }

    public String getDiscountedPrice(){
        if(foodDiscount > 0){
            double i= 100-foodDiscount;
            double percent = (i/100);
            i = foodPrice; //convert int to double
            i = i * percent;
            int disPrice = (int)i;
            return moneyFormat(disPrice);
        }
        return " ";
    }
    private String moneyFormat(int cost){
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber+" تومان";
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public void setFoodDiscount(int foodDiscount) {
        this.foodDiscount = foodDiscount;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
