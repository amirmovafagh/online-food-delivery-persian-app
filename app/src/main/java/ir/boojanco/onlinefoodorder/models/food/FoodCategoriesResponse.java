package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodCategoriesResponse {
    @SerializedName("hits")
    private List<FoodCategories> categories;

    @SerializedName("total")
    private int total;

    public List<FoodCategories> getCategories() {
        return categories;
    }

    public int getTotal() {
        return total;
    }
}
