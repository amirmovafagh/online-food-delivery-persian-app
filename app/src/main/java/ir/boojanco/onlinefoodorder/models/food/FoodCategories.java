package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

public class FoodCategories {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
