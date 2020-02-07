package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserAddressResponse {
    @SerializedName("hits")
    private List<UserAddressList> userAddressLists;
    @SerializedName("total")
    private int total;

    public List<UserAddressList> getUserAddressLists() {
        return userAddressLists;
    }

    public void setUserAddressLists(List<UserAddressList> userAddressLists) {
        this.userAddressLists = userAddressLists;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
