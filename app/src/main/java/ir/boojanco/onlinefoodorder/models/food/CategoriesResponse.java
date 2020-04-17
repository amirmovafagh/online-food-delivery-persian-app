package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {
    @SerializedName("hits")
    private List<Categories> categories;

    @SerializedName("total")
    private int total;

    public List<Categories> getCategories() {
        return categories;
    }

    public int getTotal() {
        return total;
    }
}
