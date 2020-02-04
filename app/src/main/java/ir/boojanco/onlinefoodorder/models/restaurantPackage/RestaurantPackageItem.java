package ir.boojanco.onlinefoodorder.models.restaurantPackage;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

public class RestaurantPackageItem {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("requiredPoint")
    private int requiredPoint;

    @SerializedName("startDate")
    private long startDate;

    @SerializedName("endDate")
    private long endDate;

    @SerializedName("minimumOrderCost")
    private int minimumOrderCost;

    @SerializedName("maximumDiscountAmount")
    private int maximumDiscountAmount;

    @SerializedName("freeShipping")
    private boolean freeShipping;

    @SerializedName("discountPercent")
    private int discountPercent;

    @SerializedName("discountForAllFoods")
    private boolean discountForAllFoods;

    @SerializedName("foodList")
    Map<Long ,String> foodList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredPoint() {
        return requiredPoint;
    }

    public String getRequiredPointString(){
        return "امتیاز مورد نیاز "+requiredPoint;
    }

    public void setRequiredPoint(int requiredPoint) {
        this.requiredPoint = requiredPoint;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getMinimumOrderCost() {
        return minimumOrderCost;
    }

    public String getMinimumOrderCostString(){
        return "حداقل سفارش "+moneyFormat(minimumOrderCost);
    }

    public void setMinimumOrderCost(int minimumOrderCost) {
        this.minimumOrderCost = minimumOrderCost;
    }

    public int getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public String getMaximumDiscountAmountString(){
        return "حداکثر مبلغ تخفیف "+moneyFormat(maximumDiscountAmount);
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

    public String getDiscountPercentString(){
        return "میزان تخفیف "+discountPercent+" درصد";
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isDiscountForAllFoods() {
        return discountForAllFoods;
    }

    public void setDiscountForAllFoods(boolean discountForAllFoods) {
        this.discountForAllFoods = discountForAllFoods;
    }

    public Map<Long, String> getFoodList() {
        return foodList;
    }

    public void setFoodList(Map<Long, String> foodList) {
        this.foodList = foodList;
    }

    private String moneyFormat(int cost){
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber+" تومان";
    }
}
