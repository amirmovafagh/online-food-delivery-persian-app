package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodType {
    @SerializedName("hits")
    private List<String> typeList;
    @SerializedName("total")
    private int total;

    public List<String> getTypeList() {
        return typeList;
    }

    public int getTotal() {
        return total;
    }
}
