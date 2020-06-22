package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class CartOrderResponse {
    @SerializedName("date")
    private long date;
    @SerializedName("description")
    private String description;
    @SerializedName("discountCode")
    private String discountCode;
    @SerializedName("foodList")
    private Map<Long, Integer> foodLists; //long = id , int= count
    @SerializedName("orderType")
    private String orderType;
    @SerializedName("packageId")
    private long packageId;
    @SerializedName("packingCost")
    private int packingCost;
    @SerializedName("paymentType")
    private String paymentType;
    @SerializedName("restaurantId")
    private long restaurantId;
    @SerializedName("shippingAddressId")
    private long shippingAddressId;
    @SerializedName("shippingCost")
    private int shippingCost;
    @SerializedName("totalCost")
    private int totalCost;
    @SerializedName("userId")
    private long userId;

    public boolean isUseWalletBalance() {
        return useWalletBalance;
    }

    public void setUseWalletBalance(boolean useWalletBalance) {
        this.useWalletBalance = useWalletBalance;
    }

    @SerializedName("useWalletBalance")
    private boolean useWalletBalance;

    //for response
    @SerializedName("state")
    private String state;
    @SerializedName("token")
    private String token;

    public String getState() {
        return state;
    }

    public String getToken() {
        return token;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Map<Long, Integer> getFoodLists() {
        return foodLists;
    }

    public void setFoodLists(Map<Long, Integer> foodLists) {
        this.foodLists = foodLists;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public int getPackingCost() {
        return packingCost;
    }

    public void setPackingCost(int packingCost) {
        this.packingCost = packingCost;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public CartOrderResponse(long date, String description, String discountCode, Map<Long, Integer> foodLists, String orderType, long packageId, int packingCost, String paymentType, long restaurantId, long shippingAddressId, int shippingCost, int totalCost, long userId, boolean useWalletBalance) {
        this.date = date;
        this.description = description;
        this.discountCode = discountCode;
        this.foodLists = foodLists;
        this.orderType = orderType;
        this.packageId = packageId;
        this.packingCost = packingCost;
        this.paymentType = paymentType;
        this.restaurantId = restaurantId;
        this.shippingAddressId = shippingAddressId;
        this.shippingCost = shippingCost;
        this.totalCost = totalCost;
        this.userId = userId;
        this.useWalletBalance = useWalletBalance;
    }
}