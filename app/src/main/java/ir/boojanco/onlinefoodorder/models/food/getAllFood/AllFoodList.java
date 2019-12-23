package ir.boojanco.onlinefoodorder.models.food.getAllFood;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllFoodList {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("logo")
    private String logo;
    @SerializedName("point")
    private int point;
    @SerializedName("discount")
    private int discount;
    @SerializedName("cost")
    private int cost;
    @SerializedName("foodTypeList")
    private List<String> foodTypeList;

}
