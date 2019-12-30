package ir.boojanco.onlinefoodorder.models.food.getAllFood;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllFoodResponse {
    @SerializedName("mainList")
    private MainList mainList;
    @SerializedName("secondaryList")
    private List<String> secondaryList;

    public MainList getMainList() {
        return mainList;
    }

    public List<String> secondaryList() {
        return secondaryList;
    }
}
