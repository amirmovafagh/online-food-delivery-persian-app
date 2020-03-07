package ir.boojanco.onlinefoodorder.models.food;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteFoodsResponse {
    @SerializedName("hits")
    private List<FavoriteFoods> favoriteFoodsList;

    @SerializedName("total")
    private int total;

    public List<FavoriteFoods> getFavoriteFoodsList() {
        return favoriteFoodsList;
    }

    public void setFavoriteFoodsList(List<FavoriteFoods> favoriteFoodsList) {
        this.favoriteFoodsList = favoriteFoodsList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
