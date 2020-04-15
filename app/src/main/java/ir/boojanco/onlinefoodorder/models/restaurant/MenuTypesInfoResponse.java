package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuTypesInfoResponse {
    @SerializedName("hits")
    private List<MenuType> menuTypes;
    @SerializedName("total")
    private int total;

    public List<MenuType> getMenuType() {
        return menuTypes;
    }

    public void setMenuType(List<MenuType> menuTypes) {
        this.menuTypes = menuTypes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
