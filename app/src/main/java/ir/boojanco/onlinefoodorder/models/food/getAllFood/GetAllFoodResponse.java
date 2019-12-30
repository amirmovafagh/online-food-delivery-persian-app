package ir.boojanco.onlinefoodorder.models.food.getAllFood;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllFoodResponse {
    @SerializedName("mainList")
    private List<MainList> mainList;
    @SerializedName("secondaryList")
    private List<String> secondaryList;

    public List<MainList> getMainList() {
        return mainList;
    }

    public List<String> secondaryList() {
        return secondaryList;
    }
}
