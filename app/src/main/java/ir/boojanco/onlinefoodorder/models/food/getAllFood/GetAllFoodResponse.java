package ir.boojanco.onlinefoodorder.models.food.getAllFood;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllFoodResponse {
    @SerializedName("hits")
    private List<AllFoodList> allFoodList;
    @SerializedName("total")
    private int total;

    public List<AllFoodList> getAllFoodList() {
        return allFoodList;
    }

    public int getTotal() {
        return total;
    }
}
