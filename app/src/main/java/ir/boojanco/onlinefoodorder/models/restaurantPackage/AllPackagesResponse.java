package ir.boojanco.onlinefoodorder.models.restaurantPackage;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPackagesResponse {
    @SerializedName("hits")
    private List<RestaurantPackageItem> packageItem;
    @SerializedName("total")
    private int total;

    public List<RestaurantPackageItem> getPackageItem() {
        return packageItem;
    }

    public void setPackageItem(List<RestaurantPackageItem> packageItem) {
        this.packageItem = packageItem;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
