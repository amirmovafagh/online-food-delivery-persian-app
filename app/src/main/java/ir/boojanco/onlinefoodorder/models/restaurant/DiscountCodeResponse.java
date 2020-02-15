package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DiscountCodeResponse {
    @SerializedName("code")
    private String code;

    @SerializedName("maximumDiscountAmount")
    private int maximumDiscountAmount;

    @SerializedName("freeShipping")
    private boolean freeShipping;

    @SerializedName("discountPercent")
    private int discountPercent;

    @SerializedName("forAllFoods")
    private boolean forAllFoods;

    @SerializedName("foodList")
    Map<Long , String> foodList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(int maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isForAllFoods() {
        return forAllFoods;
    }

    public void setForAllFoods(boolean forAllFoods) {
        this.forAllFoods = forAllFoods;
    }

    public Map<Long, String> getFoodList() {
        return foodList;
    }

    public void setFoodList(Map<Long, String> foodList) {
        this.foodList = foodList;
    }
}
