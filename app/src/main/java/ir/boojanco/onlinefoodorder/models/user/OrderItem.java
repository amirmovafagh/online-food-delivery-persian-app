package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class OrderItem {
    @SerializedName("id")
    private long id;
    @SerializedName("date")
    private long date;
    @SerializedName("foodList")
    private List<OrderFoodList> foodLists;
    @SerializedName("packageDiscount")
    private float packageDiscount;
    @SerializedName("taxAndService")
    private float taxAndService;
    @SerializedName("description")
    private String description;
    @SerializedName("orderStatus")
    private String orderStatus;
    @SerializedName("orderType")
    private String orderType;
    @SerializedName("paymentType")
    private String paymentType;
    @SerializedName("trackingCode")
    private String trackingCode;
    @SerializedName("restaurantName")
    private String restaurantName;
    @SerializedName("userAddress")
    private String userAddress;
    @SerializedName("paidCost")
    private int paidCost;
    @SerializedName("shippingCost")
    private int shippingCost;
    @SerializedName("packingCost")
    private int packingCost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<OrderFoodList> getFoodLists() {
        return foodLists;
    }

    public void setFoodLists(List<OrderFoodList> foodLists) {
        this.foodLists = foodLists;
    }

    public float getPackageDiscount() {
        return packageDiscount;
    }

    public void setPackageDiscount(float packageDiscount) {
        this.packageDiscount = packageDiscount;
    }

    public float getTaxAndService() {
        return taxAndService;
    }

    public void setTaxAndService(float taxAndService) {
        this.taxAndService = taxAndService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPaymentType() {
        return "پرداخت "+paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getPaidCost() {
        return paidCost;
    }

    public String getPaidCostString(){
        return "جمع سفارش: "+moneyFormat(paidCost);
    }

    public void setPaidCost(int paidCost) {
        this.paidCost = paidCost;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public String getShippingCostString(){
        return moneyFormat(shippingCost);
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getPackingCost() {
        return packingCost;
    }

    public String getPackingCostString(){
        return moneyFormat(packingCost);
    }

    public void setPackingCost(int packingCost) {
        this.packingCost = packingCost;
    }
    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";
    }
}
