package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserOrdersResponse {
    @SerializedName("hits")
    private List<orderItem> orderItemList;
    @SerializedName("total")
    private int total;

    public List<orderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<orderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
