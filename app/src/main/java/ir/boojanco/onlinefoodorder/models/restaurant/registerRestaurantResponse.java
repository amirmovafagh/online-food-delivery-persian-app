package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

public class registerRestaurantResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("name")
    private String name;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("branch")
    private String branch;
    @SerializedName("minimumOrder")
    private String minimumOrder;



}
