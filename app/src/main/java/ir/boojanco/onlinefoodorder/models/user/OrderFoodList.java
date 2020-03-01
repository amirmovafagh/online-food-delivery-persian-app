package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class OrderFoodList {
    @SerializedName("foodName")
    private String foodName;
    @SerializedName("logo")
    private String logo;
    @SerializedName("point")
    private int point;
    @SerializedName("discountPercent")
    private int discountPercent;
    @SerializedName("cost")
    private int cost;
    @SerializedName("count")
    private int count;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getLogo() {
        return webServerMediaRoute + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getCost() {
        return cost;
    }

    public String getCostMoneyFormat() {
        return moneyFormat(cost * count);
    }


    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCount() {
        return count;
    }

    public String getCountString() {
        return "تعداد " + count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";

    }
}