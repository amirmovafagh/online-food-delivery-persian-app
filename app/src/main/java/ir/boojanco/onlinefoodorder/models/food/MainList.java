package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainList {
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
