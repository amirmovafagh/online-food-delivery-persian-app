package ir.boojanco.onlinefoodorder.models.food;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllFoodResponse {
    @SerializedName("faveList")
    private List<Long> faveList;
    @SerializedName("foodList")
    private FoodList foodList;
    @SerializedName("foodTypeList")
    private FoodType foodTypeList;

    public List<Long> getFaveList() {
        return faveList;
    }

    public FoodList getFoodList() {
        return foodList;
    }

    public FoodType getFoodTypeList() {
        return foodTypeList;
    }
}
